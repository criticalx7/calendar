package server.persistence.sql;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

import common.model.Event;
import de.saxsys.javafx.test.JfxRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sqlite.SQLiteDataSource;
import util.EventsFactory;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Simple test of CRUD database operations.
 */
@RunWith(JfxRunner.class)
public class SQLTaskTest {
    private static SQLiteDataSource dataSource;
    private Connection con;

    @BeforeClass
    public static void setupDatasource() {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:EventsTest.db");
    }

    @Before
    public void setup() throws SQLException {
        con = dataSource.getConnection();
        con.setAutoCommit(false);
    }

    @After
    public void tearDown() throws SQLException {
        if (!con.isClosed()) {
            con.rollback();
            con.close();
        }
    }

    @Test
    public void loadTask() throws Exception {
        LoadTask loadTask = new LoadTask(con);
        List<Event> events = loadTask.call();
        assertEquals(1, events.size());
        assertEquals("test", events.get(0).getName());
    }

    @Test
    public void insertTask() throws Exception {
        Event event = EventsFactory.getMockEvent(0, "inserted");
        InsertTask insertTask = new InsertTask(event, con);
        insertTask.insert(con);

        // test
        ResultSet set = findAll(con);
        String name = "";
        while (set.next()) {
            name = set.getString("name");
        }
        assertEquals("inserted", name);
    }

    @Test
    public void deleteTask() throws Exception {
        Event event = EventsFactory.getMockEvent(1, "test");
        DeleteTask deleteTask = new DeleteTask(event, con);
        deleteTask.delete(con);

        // test
        ResultSet set = findAll(con);
        int count = 0;
        while (findAll(con).next()) {
            count++;
        }
        assertTrue(count == 0);
    }

    @Test
    public void updateTask() throws Exception {
        Event event = EventsFactory.getMockEvent(1, "edited");
        UpdateTask updateTask = new UpdateTask(event, con);
        updateTask.update(con);

        // test
        ResultSet set = findByID(con);
        set.next();
        assertEquals(1, set.getInt("id"));
        assertEquals("edited", set.getString("name"));
    }


    private ResultSet findAll(Connection con) throws SQLException {
        String sql = "SELECT * FROM events";
        Statement statement = con.createStatement();
        return statement.executeQuery(sql);
    }

    private ResultSet findByID(Connection con) throws SQLException {
        String sql = "SELECT * FROM events WHERE id = ?";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setInt(1, 1);
        return statement.executeQuery();
    }
}

