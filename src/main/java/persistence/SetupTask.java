package persistence;

import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import model.Event;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;


/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */
class SetupTask extends Task {

    @Override
    protected Void call() throws SQLException {
        try (Connection con = DBManager.getConnection()) {
            System.out.println("Setup task initialize...");
            createDirectory();
            createDatabase(con);
        }
        return null;
    }


    /**
     * Call internally to check existence of database directory.
     * If there is none, create one.
     */
    private void createDirectory() {
        File eventFolder = new File(System.getProperty("user.home"), "CalendarDB");
        if (!eventFolder.exists()) {
            // make directory
            boolean success = eventFolder.mkdirs();
            // if not success, just close the program.
            if (!success) {
                System.err.println("Create directory failed, program will close");
                System.exit(0);
            }
        }
    }


    /**
     * Call internally to check existence of database file.
     * If there is none, create one and insert dummy event.
     *
     * @param con A database connection
     * @throws SQLException Corruption occurs.
     */
    private void createDatabase(Connection con) throws SQLException {
        // check if the database exist. If not, create one.
        File eventDB = new File(System.getProperty("user.home") + "/CalendarDB", "Events.db");
        // create table and insert a welcome event
        if (!eventDB.exists()) {
            createSchema(con);
            insertWelcome(con);
        }

    }


    /**
     * Call internally to create database schema.
     *
     * @param con A database connection
     * @throws SQLException Corruption occurs.
     */
    private void createSchema(Connection con) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Events (\n"
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT ,\n"
                + "	name TEXT NOT NULL,\n"
                + "	note TEXT\n,"
                + " tag TEXT\n,"
                + "	startDate TEXT NOT NULL,\n"
                + " endDate TEXT NOT NULL,\n"
                + " color TEXT"
                + ");";

        Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);
    }


    /**
     * Call internally to insert a dummy event.
     *
     * @param con A database connection
     * @throws SQLException Corruptions occurs.
     */
    private void insertWelcome(Connection con) throws SQLException {
        Event checkme = new Event();
        checkme.setName("checkMe");
        checkme.setNote("Welcome to our mini calendar");
        checkme.setTag("");
        checkme.setStart(LocalDate.now());
        checkme.setEnd(LocalDate.now());
        checkme.setColor(Color.valueOf(Event.getDefaultColor()));

        String sql = "INSERT INTO Events (name, note, tag, startDate, endDate, color) VALUES(?,?,?,?,?,?)";

        PreparedStatement statement = con.prepareStatement(sql);
        statement.setString(1, checkme.getName());
        statement.setString(2, checkme.getNote());
        statement.setString(3, checkme.getTag());
        statement.setString(4, Event.getDefaultDatePattern().format(checkme.getStart()));
        statement.setString(5, Event.getDefaultDatePattern().format(checkme.getEnd()));
        statement.setString(6, checkme.getColor().toString());
        statement.executeUpdate();
        statement.close();
    }


}
