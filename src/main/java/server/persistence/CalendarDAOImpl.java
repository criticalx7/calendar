package server.persistence;

import common.model.Event;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;

// TODO - migrate to Log4j
/*
 * @author : Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */
@Repository
public class CalendarDAOImpl implements CalendarDAO<Event> {
    private final DateTimeFormatter defaultDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final String defaultColor = Color.VIOLET.toString();
    private final Logger logger = Logger.getLogger(CalendarDAO.class.getName());
    private final AtomicInteger primaryKey = new AtomicInteger(0);
    private final ExecutorService executor = Executors.newFixedThreadPool(1);
    private final DataSource dataSource;
    private Task currentTask;
    private Future taskFuture;


    /**
     * @param dataSource An event's SQL data source
     */
    @Autowired
    public CalendarDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        setupLogger();
    }

    private void setupLogger() {
        logger.setUseParentHandlers(false);
        Handler handler = new ConsoleHandler();
        handler.setFormatter(new Formatter() {
            private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            @Override
            public String format(LogRecord record) {
                return String.format("%s %s: %s - %s%n",
                        timeFormatter.format(LocalDateTime.ofInstant(record.getInstant(), ZoneId.systemDefault())),
                        record.getLevel(),
                        record.getSourceMethodName(),
                        record.getMessage());
            }
        });
        logger.addHandler(handler);
    }

    /**
     * Issues a task to setup directory and database file.
     */
    @Override
    public void setup() {
        currentTask = new SetupTask();
        taskFuture = executor.submit(currentTask);
        try {
            taskFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
            close();
        }
    }


    /**
     * Issues a task to query the event records,
     *
     * @return A list of events instance
     */
    @Override
    public List<Event> load() {
        List<Event> result = null;
        LoadTask loadTask = new LoadTask();
        currentTask = loadTask;
        taskFuture = executor.submit(currentTask);
        try {
            result = loadTask.get();
        } catch (Exception e) {
            e.printStackTrace();
            close();
        }
        return result;
    }


    /**
     * Issue a task to insert the event records,
     * increment primaryKey then set it to the event.
     *
     * @param event - Event to be insert
     */
    @Override
    public void insert(final Event event) {
        event.setId(primaryKey.incrementAndGet());
        currentTask = new InsertTask(event);
        taskFuture = executor.submit(currentTask);
    }


    /**
     * Issue a task to delete the event records
     *
     * @param event - Event to be removed
     */
    @Override
    public void delete(final Event event) {
        currentTask = new DeleteTask(event);
        taskFuture = executor.submit(currentTask);
    }


    /**
     * Issue a task to update the event records
     *
     * @param event - event to be update
     */
    @Override
    public void update(final Event event) {
        currentTask = new UpdateTask(event);
        taskFuture = executor.submit(currentTask);
    }


    /**
     * Shutdown all the threads running.
     */
    @Override
    public void close() {
        logger.info("Shutdown all the tasks...");
        executor.shutdown();
    }


    /**
     * Get a current task Future for observation.
     *
     * @return Current task's Future
     */
    public Future getTaskFuture() {
        return taskFuture;
    }


    //---------------------------- DATA Access tasks ------------------------

    /**
     * A task handles delete event's record.
     */
    class DeleteTask extends Task {
        private final Event event;

        DeleteTask(final Event event) {
            this.event = event;
        }

        @Override
        protected Void call() {
            try (Connection con = dataSource.getConnection()) {
                delete(con);
            } catch (SQLException e) {
                logger.config("Conflict: " + e.getMessage());
            }
            return null;
        }

        /**
         * Call internally to make a delete statement of selected event's record.
         *
         * @param con A database connection
         * @throws SQLException Corruption occurs.
         */
        private void delete(Connection con) throws SQLException {
            int id = event.getId();
            String sql = String.format("DELETE FROM Events WHERE id = %d", id);
            Statement statement = con.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            logger.info(event.toString());
        }
    }

    /**
     * A task handles update event's record.
     */
    class UpdateTask extends Task {
        private final Event event;

        UpdateTask(final Event event) {
            this.event = event;
        }

        @Override
        protected Void call() {
            try (Connection con = dataSource.getConnection()) {
                update(con);
            } catch (SQLException e) {
                logger.config("Conflict: " + e.getMessage());
            }

            return null;
        }

        /**
         * Call internally to make an update statement of selected event's record.
         *
         * @param con A database connection
         * @throws SQLException Corruption occurs.
         */
        private void update(Connection con) throws SQLException {
            String sql = "UPDATE Events " +
                    "SET name = ?, note = ?, startDate = ?, endDate = ?, color = ?, recurred = ?, yearly = ?, monthly = ?" +
                    "WHERE id = ?";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, event.getName());
            statement.setString(2, event.getNote());
            statement.setString(3, event.getStart().format(defaultDateFormat));
            statement.setString(4, event.getEnd().format(defaultDateFormat));
            statement.setString(5, event.getColor());
            statement.setInt(6, event.isRecurred() ? 1 : 0);
            statement.setInt(7, event.isYearly() ? 1 : 0);
            statement.setInt(8, event.isMonthly() ? 1 : 0);
            statement.setInt(9, event.getId());
            statement.executeUpdate();
            logger.info(event.toString());
        }
    }

    /**
     * A task handles insert event's record.
     */
    class InsertTask extends Task {
        private final Event event;

        InsertTask(final Event event) {
            this.event = event;
        }

        @Override
        protected Void call() throws Exception {
            try (Connection con = dataSource.getConnection()) {
                insert(con);
            } catch (SQLException e) {
                logger.config("Conflict: " + e.getMessage());
            }
            return null;
        }

        /**
         * Call internally to make a insert statement of new event's record.
         *
         * @param con A database connection
         * @throws SQLException Corruption occurs.
         */
        private void insert(Connection con) throws SQLException {
            String sql = "INSERT INTO Events (name, note, startDate, endDate, color, recurred, yearly, monthly) " +
                    "VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, event.getName());
            statement.setString(2, event.getNote());
            statement.setString(3, event.getStart().format(defaultDateFormat));
            statement.setString(4, event.getEnd().format(defaultDateFormat));
            statement.setString(5, event.getColor());
            statement.setInt(6, event.isRecurred() ? 1 : 0);
            statement.setInt(7, event.isYearly() ? 1 : 0);
            statement.setInt(8, event.isMonthly() ? 1 : 0);
            statement.executeUpdate();
            statement.close();
            logger.info(event.toString());
        }
    }

    /**
     * A task handles load/query all events record.
     */
    class LoadTask extends Task<List<Event>> {

        @Override
        protected List<Event> call() throws Exception {
            try (Connection con = dataSource.getConnection()) {
                return load(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Issue a statement for obtaining a result set and then process the set.
         * This also set the auto increment key from sqlite_sequence table.
         *
         * @param con A database connection
         * @return List of events, ready to be set.
         * @throws SQLException Corruption occurs.
         */
        private List<Event> load(Connection con) throws SQLException {
            List<Event> events;
            String sql = "SELECT * FROM Events";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            events = processEvents(resultSet);
            statement.close();

            sql = "SELECT * FROM sqlite_sequence";
            statement = con.createStatement();
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            primaryKey.set(resultSet.getInt("seq"));
            statement.close();
            return events;
        }


        private ArrayList<Event> processEvents(ResultSet set) throws SQLException {
            ArrayList<Event> events = new ArrayList<>();
            while (set.next()) {
                Event event = new Event();
                event.setId(set.getInt("id"));
                event.setName(set.getString("name"));
                event.setNote(set.getString("note"));
                event.setStart(LocalDate.parse(set.getString("startDate"), defaultDateFormat));
                event.setEnd(LocalDate.parse(set.getString("endDate"), defaultDateFormat));

                String color = set.getString("color");
                event.setColor(color == null ? defaultColor : color);

                event.setRecurred(set.getInt("recurred") == 1);
                event.setYearly(set.getInt("yearly") == 1);
                event.setMonthly(set.getInt("monthly") == 1);
                events.add(event);
            }
            return events;
        }
    }

    /**
     * a Task handles directory and database initialization.
     */
    class SetupTask extends Task {

        @Override
        protected Void call() throws SQLException {
            logger.info("Setup task initialize...");
            createDirectory();
            createDatabase();
            return null;
        }

        private void createDirectory() {
            File eventFolder = new File(System.getProperty("user.home"), "CalendarDB");
            if (!eventFolder.exists()) {
                logger.info("Creating folder...");
                boolean success = eventFolder.mkdirs();
                // if not success, just close the program.
                if (!success) {
                    System.err.println("Create directory failed, program will close");
                    System.exit(0);
                }
            }
        }

        private void createDatabase() throws SQLException {
            // check if the database exist. If not, create one.
            File eventDB = new File(System.getProperty("user.home") + "/CalendarDB", "Events.db");
            // create table and insert a welcome event
            if (!eventDB.exists()) {
                logger.info("Creating database...");
                try (Connection con = dataSource.getConnection()) {
                    createSchema(con);
                    insertFirstEvent(con);
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
                    + " yearly INTEGER,\n"
                    + " monthly INTEGER"
                    + ");";

            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);
        }

        private void insertFirstEvent(Connection con) {
            logger.info("Insert initial event...");
            Event checkme = new Event();
            checkme.setName("checkMe");
            checkme.setNote("This is example events");
            checkme.setStart(LocalDate.now());
            checkme.setEnd(LocalDate.now());
            checkme.setColor(defaultColor);
            checkme.setRecurred(false);
            checkme.setYearly(false);
            checkme.setYearly(false);
            insert(checkme);
        }
    }
}
