package server.persistence.sql;

/*
 * @author : Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */

import common.model.Event;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import server.persistence.BaseEventsDAO;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


@Repository
public class EventsDAO extends BaseEventsDAO {
    private final Logger logger = LogManager.getLogger(EventsDAO.class);
    private final AtomicInteger primaryKey = new AtomicInteger(0);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final DataSource dataSource;


    @Autowired
    public EventsDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void setup() throws Exception {
        logger.info("Configure Event's database...");
        createDirectory();
        File eventDB = new File(System.getProperty("user.home") + "/CalendarDB", "Events.db");
        if (!eventDB.exists()) {
            createSchema(getConnection());
            insertDummy();
        } else {
            setPrimaryKey(getConnection());
        }
    }


    private void createSchema(Connection connection) {
        try (Connection con = connection) {
            logger.trace("Creating schema...");
            String sql = "CREATE TABLE IF NOT EXISTS Events (\n"
                    + "	id INTEGER PRIMARY KEY AUTOINCREMENT ,\n"
                    + "	name TEXT NOT NULL,\n"
                    + "	note TEXT\n,"
                    + "	startDate TEXT NOT NULL,\n"
                    + " endDate TEXT NOT NULL,\n"
                    + " color TEXT,\n"
                    + " recurred INTEGER NOT NULL,\n"
                    + " yearly INTEGER NOT NULL, \n"
                    + " monthly INTEGER NOT NULL"
                    + ");";

            Statement statement = con.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void setPrimaryKey(Connection connection) {
        try (Connection con = connection) {
            logger.trace("Setting primary key...");
            String sql = "SELECT * FROM sqlite_sequence";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            primaryKey.set(resultSet.getInt("seq"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Event> load() {
        LoadTask loadTask = new LoadTask(getConnection());
        executor.submit(loadTask);
        logger.info("Load task initialized...");
        try {
            return loadTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insert(final Event event) {
        event.setId(primaryKey.incrementAndGet());
        Task task = new InsertTask(event, getConnection());
        executor.submit(task);
        logger.info(event.toString());
    }

    @Override
    public void delete(final Event event) {
        Task task = new DeleteTask(event, getConnection());
        executor.submit(task);
        logger.info(event.toString());

    }

    @Override
    public void update(final Event event) {
        Task task = new UpdateTask(event, getConnection());
        executor.submit(task);
        logger.info(event.toString());

    }

    private Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
