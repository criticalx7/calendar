package controller;

import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import model.Event;

import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


class DBManager {
    private String DB_URL = "jdbc:sqlite:Events.db";
    private MainController controller;

    DBManager(MainController controller) {
        this.controller = controller;
    }

    /**
     * Get connection to sqlite database and
     * query the events data and process it into
     * an eventList
     */
    void load() {
        Connection conn = null;
        Statement statement;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);

            // execute SQL statement
            System.out.println("Loading...");
            String sql = "SELECT * FROM Events";
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            processEvents(resultSet);

            resultSet.close();
        } catch (ClassNotFoundException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("There was an ERROR");
            alert.setHeaderText("DATABASE ERROR");
            alert.setContentText("Unable to load data from database");
            alert.showAndWait();
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * Get connection to sqlite database and
     * insert the record into the Events table
     *
     * @param event - event to be insert
     */
    void insert(Event event) {
        Connection conn = null;
        PreparedStatement statement;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);

            // execute SQL statement
            String name = event.getName();
            String note = event.getNote();
            String tag = event.getTag();
            String startDate = controller.getDateFormatter().format(event.getStart());
            String endDate = controller.getDateFormatter().format(event.getEnd());
            Color c = event.getColor();
            int r = (int) (c.getRed() * 255);
            int g = (int) (c.getGreen() * 255);
            int b = (int) (c.getBlue() * 255);
            String color = String.format("%d,%d,%d", r, g, b);
            String sql = "INSERT INTO Events (name, note, tag, startDate, endDate, color) VALUES(?,?,?,?,?,?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, note);
            statement.setString(3, tag);
            statement.setString(4, startDate);
            statement.setString(5, endDate);
            statement.setString(6, color);
            statement.executeUpdate();


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * Get connection to sqlite database and
     * delete(remove) the event's record based on primary key id
     *
     * @param id - primary id of the event
     */
    void delete(int id) {
        Connection conn = null;
        Statement statement;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);

            // execute SQL statement
            String sql = String.format("DELETE FROM Events WHERE id = %d", id);
            statement = conn.createStatement();
            statement.executeUpdate(sql);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * Convert result set into a set of events and put it
     * into event list
     *
     * @param set - a result set from query statement
     * @throws SQLException - transaction fail
     */
    private void processEvents(ResultSet set) throws SQLException {
        while (set.next()) {
            Event event = new Event();
            event.setId(set.getInt("id"));
            event.setName(set.getString("name"));
            event.setNote(set.getString("note"));
            event.setTag(set.getString("tag"));
            event.setStart(LocalDate.parse(set.getString("startDate"), controller.getDateFormatter()));
            event.setEnd(LocalDate.parse(set.getString("endDate"), controller.getDateFormatter()));
            if (set.getString("color") == null) {
                event.setColor(Color.valueOf("#7290c1"));
            } else {
                int[] color = Arrays.stream(set.getString("color")
                        .split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                event.setColor(Color.rgb(color[0], color[1], color[2]));
            }
            Event.setPrimaryKey(set.getInt("id"));
            controller.getEventList().addEvent(event);
        }
    }
}
