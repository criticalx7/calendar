package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.assertEquals;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class DBTest {
    private EventList eventList;
    private Event dummyEvent;
    private DBManager dbManager;
    private Connection conn;

    @Before
    public void setUp() throws Exception {
        eventList = new EventList();
        dummyEvent = new Event();
        dummyEvent.setName("DBTEST");
        dbManager = new DBManager(eventList);
        dbManager.setDB_URL("jdbc:sqlite:EventsTest.db");
        eventList.setDatabaseManager(dbManager);
        eventList.loadEvent();
        conn = DriverManager.getConnection(dbManager.getDB_URL());
    }

    @After
    public void tearDown() throws Exception {
        eventList = null;
        dummyEvent = null;
        dbManager = null;
        conn = null;
    }

    @Test
    public void load() throws Exception {
        assertEquals(true, eventList.getEvents().size() > 0);
        assertEquals("test", eventList.getEvents().get(0).getName());
    }

    @Test
    public void addEvent() throws Exception {
        eventList.addEvent(dummyEvent);

        String sql = "SELECT * FROM Events WHERE id =(SELECT  max(id) FROM Events)";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();

        assertEquals("DBTEST", resultSet.getString("name"));
        assertEquals(2, getRowCount(conn));

        // cleanup
        resultSet.close();
        eventList.removeEvent(eventList.getEvents().size() - 1);

    }

    @Test
    public void removeEvent() throws Exception {
        eventList.addEvent(dummyEvent);
        eventList.removeEvent(eventList.getEvents().size() - 1);
        assertEquals(1, getRowCount(conn));
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