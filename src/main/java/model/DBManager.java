package model;

import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;


/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */
public class DBManager {
    private String DB_URL = null;
    private EventList eventList;

    public DBManager(EventList eventList) {
        this.eventList = eventList;
    }

    private void initDatabase() throws IOException {
        File calendar_db = new File(System.getProperty("user.home"), "CalendarDB");
        if (!calendar_db.exists()) {
            boolean success = calendar_db.mkdirs();
            if (!success) {
                throw new IOException("Create directory failed, program will close");
            }
        }
        File database_file = new File(System.getProperty("user.home") + "/CalendarDB", "Events.db");
        setDB_URL("jdbc:sqlite:" + System.getProperty("user.home") + "/CalendarDB/Events.db");
        if (!database_file.exists()) {
            createTable();
            insertCheckme();
        }
    }

    private void createTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS Events (\n"
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT ,\n"
                + "	name TEXT NOT NULL,\n"
                + "	note TEXT\n,"
                + " tag TEXT\n,"
                + "	startDate TEXT NOT NULL,\n"
                + " endDate TEXT NOT NULL,\n"
                + " color TEXT"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void insertCheckme() {
        Event checkme = new Event();
        checkme.setName("checkMe");
        checkme.setNote("This is an event viewer. You can add, edit, and removing event by placing a button.");
        checkme.setTag("");
        checkme.setStart(LocalDate.now());
        checkme.setEnd(LocalDate.now());
        checkme.setColor(Color.valueOf(Event.getDefaultColor()));
        insert(checkme);
    }


    /**
     * Get connection to sqlite database and
     * query the events data and process it into
     * an eventList
     */
    void load() {
        try {
            if (DB_URL == null) initDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT * FROM Events";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            processEvents(resultSet);
            statement.close();

            sql = "SELECT * FROM sqlite_sequence";
            statement = conn.createStatement();
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            Event.setPrimaryKey(resultSet.getInt("seq"));
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
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
            event.setStart(LocalDate.parse(set.getString("startDate"), Event.getDefaultDatePattern()));
            event.setEnd(LocalDate.parse(set.getString("endDate"), Event.getDefaultDatePattern()));
            if (set.getString("color") == null) {
                event.setColor(Color.valueOf(Event.getDefaultColor()));
            } else {
                event.setColor(Color.valueOf(set.getString("color")));
            }
            eventList.getEvents().add(event);
        }
    }

    /**
     * Get connection to sqlite database and
     * insert the record into the Events table
     *
     * @param event - event to be insert
     */
    void insert(Event event) {

        String sql = "INSERT INTO Events (name, note, tag, startDate, endDate, color) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, event.getName());
            statement.setString(2, event.getNote());
            statement.setString(3, event.getTag());
            statement.setString(4, Event.getDefaultDatePattern().format(event.getStart()));
            statement.setString(5, Event.getDefaultDatePattern().format(event.getEnd()));
            statement.setString(6, event.getColor().toString());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get connection to sqlite database and
     * delete(remove) the event's record based on primary key id
     *
     * @param event - Event to be removed
     */
    void delete(Event event) {
        int id = event.getId();
        String sql = String.format("DELETE FROM Events WHERE id = %d", id);
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get connection to sqlite database and
     * update(edit) the event's record
     *
     * @param event - event to be update
     */
    void update(Event event) {
        String sql = "UPDATE Events " +
                "SET name = ?, note = ?, tag = ?, startDate = ?, endDate = ?, color = ? " +
                "WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, event.getName());
            statement.setString(2, event.getNote());
            statement.setString(3, event.getTag());
            statement.setString(4, Event.getDefaultDatePattern().format(event.getStart()));
            statement.setString(5, Event.getDefaultDatePattern().format(event.getEnd()));
            statement.setString(6, event.getColor().toString());
            statement.setInt(7, event.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDB_URL(String db_url) {
        DB_URL = db_url;
    }

    public String getDB_URL() {
        return DB_URL;
    }

}
