package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.assertEquals;

public class DBTest {
    private EventList eventList;
    private Event dummyEvent;
    private DBManager dbManager;

    @Before
    public void setUp() throws Exception {
        eventList = new EventList();
        dummyEvent = new Event();
        dummyEvent.setName("DBTEST");
        dbManager = new DBManager(eventList, "jdbc:sqlite:EventsTest.db");
        eventList.setDatabaseManager(dbManager);

    }

    @After
    public void tearDown() throws Exception {
        eventList = null;
        dummyEvent = null;
        dbManager = null;
    }

    @Test
    public void load() throws Exception {
        eventList.loadEvent();

        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(dbManager.getDB_URL());
            assertEquals(1, getRowCount(conn));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    @Test
    public void addEvent() throws Exception {
        eventList.addEvent(dummyEvent);

        Connection conn = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(dbManager.getDB_URL());


            String sql = "SELECT COUNT(*) FROM Events";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            assertEquals(2, resultSet.getInt(1));
            resultSet.close();

            sql = "DELETE FROM Events WHERE id = (SELECT MAX(id) FROM Events)";
            statement = conn.createStatement();
            statement.executeUpdate(sql);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    private static int getRowCount(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Events";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        int lastRow = resultSet.getInt(1);
        resultSet.close();
        return lastRow;
    }

}