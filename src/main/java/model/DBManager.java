package model;

import javafx.scene.paint.Color;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;


/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */
public class DBManager {
    private String DB_URL = "jdbc:sqlite:Events.db";
    private EventList eventList;

    public DBManager(EventList eventList) {
        this.eventList = eventList;
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
            Color c = event.getColor();
            int r = (int) (c.getRed() * 255);
            int g = (int) (c.getGreen() * 255);
            int b = (int) (c.getBlue() * 255);
            String color = String.format("%d,%d,%d", r, g, b);
            String sql = "INSERT INTO Events (name, note, tag, startDate, endDate, color) VALUES(?,?,?,?,?,?)";
            statement = conn.prepareStatement(sql);
            statement.setString(1, event.getName());
            statement.setString(2, event.getNote());
            statement.setString(3, event.getTag());
            statement.setString(4, DateTimeFormatter.ofPattern("dd-MM-yyyy").format(event.getStart()));
            statement.setString(5, DateTimeFormatter.ofPattern("dd-MM-yyyy").format(event.getEnd()));
            statement.setString(6, color);
            statement.executeUpdate();


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get connection to sqlite database and
     * update(edit) the event's record
     *
     * @param event - event to be update
     */
    void update(Event event) {
        Connection conn = null;
        PreparedStatement statement;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);

            // execute SQL statement
            Color c = event.getColor();
            int r = (int) (c.getRed() * 255);
            int g = (int) (c.getGreen() * 255);
            int b = (int) (c.getBlue() * 255);
            String color = String.format("%d,%d,%d", r, g, b);
            String sql = "UPDATE Events " +
                    "SET name = ?, note = ?, tag = ?, startDate = ?, endDate = ?, color = ? " +
                    "WHERE id = ?";
            statement = conn.prepareStatement(sql);
            statement.setString(1, event.getName());
            statement.setString(2, event.getNote());
            statement.setString(3, event.getTag());
            statement.setString(4, DateTimeFormatter.ofPattern("dd-MM-yyyy").format(event.getStart()));
            statement.setString(5, DateTimeFormatter.ofPattern("dd-MM-yyyy").format(event.getEnd()));
            statement.setString(6, color);
            statement.setInt(7, event.getId());
            statement.executeUpdate();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
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
            event.setStart(LocalDate.parse(set.getString("startDate"), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            event.setEnd(LocalDate.parse(set.getString("endDate"), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
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
            eventList.getEvents().add(event);
        }
    }
}
