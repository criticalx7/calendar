package server.persistence.sql;

//TODO migrate log to facade
/*
 * @author : Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */

import common.model.Event;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import server.persistence.BaseEventsDAO;
import server.persistence.SimpleDAO;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;


@Repository
public class EventsDAO extends BaseEventsDAO {
    private final Logger logger = Logger.getLogger(SimpleDAO.class.getName());
    private final AtomicInteger primaryKey = new AtomicInteger(0);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final DataSource dataSource;


    /**
     * @param dataSource An event's SQL data source
     */
    @Autowired
    public EventsDAO(DataSource dataSource) {
        this.dataSource = dataSource;
        setupLogger();
    }

    // currently hack to be readable, will move to facade
    private void setupLogger() {
        logger.setUseParentHandlers(false);
        Handler handler = new ConsoleHandler();
        handler.setFormatter(new Formatter() {
            private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            @Override
            public String format(LogRecord record) {
                return String.format("%s [%s] %s:%s - %s%n",
                        timeFormatter.format(LocalDateTime.ofInstant(record.getInstant(), ZoneId.systemDefault())),
                        record.getLevel(),
                        record.getSourceClassName(),
                        record.getSourceMethodName(),
                        record.getMessage());
            }
        });
        logger.addHandler(handler);
    }

    /**
     * Setup directory and database file.
     */
    @Override
    public void setup() {
        createDirectory();
        File eventDB = new File(System.getProperty("user.home") + "/CalendarDB", "Events.db");
        if (!eventDB.exists()) {
            try (Connection con = getConnection()) {
                createSchema(con);
                insertDummy();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try (Connection con = getConnection()) {
                setPrimaryKey(con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void createSchema(Connection con) throws SQLException {
        logger.info("Creating schema...");
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
    }

    private void setPrimaryKey(Connection con) throws SQLException {
        String sql = "SELECT * FROM sqlite_sequence";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        primaryKey.set(resultSet.getInt("seq"));
    }

    @Override
    public List<Event> load() {
        LoadTask loadTask = new LoadTask(getConnection());
        executor.submit(loadTask);
        try {
            return loadTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("task initiated.");
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


    @Override
    public void close() {
        logger.info("Shutdown all the tasks...");
        executor.shutdown();
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
