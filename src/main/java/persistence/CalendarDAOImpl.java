package persistence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;


/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */

@Repository
public class CalendarDAOImpl implements CalendarDAO {
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
    public ObservableList<Event> load() {
        ObservableList<Event> result = null;
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

    class DeleteTask extends Task {
        private final Event event;

        /**
         * @param event An event to be deleted.
         */
        DeleteTask(Event event) {
            this.event = event;
        }

        @Override
        protected Void call() throws Exception {
            try (Connection con = dataSource.getConnection()) {
                logger.info("Delete task initialize...");
                delete(con);
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
        }
    }

    class UpdateTask extends Task {
        private final Event event;

        /**
         * @param event An event to be updated.
         */
        UpdateTask(Event event) {
            this.event = event;
        }

        @Override
        protected Void call() throws Exception {
            try (Connection con = dataSource.getConnection()) {
                logger.info("Update task initialize...");
                update(con);
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
                    "SET name = ?, note = ?, tag = ?, startDate = ?, endDate = ?, color = ? " +
                    "WHERE id = ?";

            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, event.getName());
            statement.setString(2, event.getNote());
            statement.setString(3, event.getTag());
            statement.setString(4, Event.getDefaultDatePattern().format(event.getStart()));
            statement.setString(5, Event.getDefaultDatePattern().format(event.getEnd()));
            statement.setString(6, event.getColor().toString());
            statement.setInt(7, event.getId());
            statement.executeUpdate();
        }
    }

    class InsertTask extends Task {
        private final Event event;

        /**
         * @param event An event to be inserted.
         */
        InsertTask(Event event) {
            this.event = event;
        }

        @Override
        protected Void call() throws Exception {
            try (Connection con = dataSource.getConnection()) {
                logger.info("Insert task initialize...");
                insert(con);
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
            String sql = "INSERT INTO Events (name, note, tag, startDate, endDate, color) VALUES(?,?,?,?,?,?)";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, event.getName());
            statement.setString(2, event.getNote());
            statement.setString(3, event.getTag());
            statement.setString(4, Event.getDefaultDatePattern().format(event.getStart()));
            statement.setString(5, Event.getDefaultDatePattern().format(event.getEnd()));
            statement.setString(6, event.getColor().toString());
            statement.executeUpdate();
            statement.close();
        }


    }

    class LoadTask extends Task<ObservableList<Event>> {

        @Override
        protected ObservableList<Event> call() throws Exception {
            try (Connection con = dataSource.getConnection()) {
                logger.info("Load task initialize...");
                return load(con);
            }
        }

        /**
         * Issue a statement for obtaining a result set and then process the set.
         * This also set the auto increment key from sqlite_sequence table.
         *
         * @param con A database connection
         * @return List of events, ready to be set.
         * @throws SQLException Corruption occurs.
         */
        private ObservableList<Event> load(Connection con) throws SQLException {
            ObservableList<Event> events;
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


        /**
         * Call internally to process the result set, turn it into a list of events.
         *
         * @param set A set of records from a query.
         * @return List of events, ready to be set.
         * @throws SQLException Corruption occurs.
         */
        private ObservableList<Event> processEvents(ResultSet set) throws SQLException {
            ObservableList<Event> events = FXCollections.observableArrayList();

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
                events.add(event);
            }
            return events;
        }
    }

    class SetupTask extends Task {

        @Override
        protected Void call() throws SQLException {
            logger.info("Setup task initialize...");
            createDirectory();
            createDatabase();
            return null;
        }


        /**
         * Call internally to check existence of database directory.
         * If there is none, create one.
         */
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


        /**
         * Call internally to check existence of database file.
         * If there is none, create one and insert dummy event.
         *
         * @throws SQLException Corruption occurs.
         */
        private void createDatabase() throws SQLException {
            // check if the database exist. If not, create one.
            File eventDB = new File(System.getProperty("user.home") + "/CalendarDB", "Events.db");
            // create table and insert a welcome event
            if (!eventDB.exists()) {
                logger.info("Creating database...");
                try (Connection con = dataSource.getConnection()) {
                    createSchema(con);
                    insertWelcome(con);
                }
            }

        }


        /**
         * Call internally to create database schema.
         *
         * @param con A database connection
         * @throws SQLException Corruption occurs.
         */
        private void createSchema(Connection con) throws SQLException {
            logger.info("Creating schema...");
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
         */
        private void insertWelcome(Connection con) {
            logger.info("Insert initial event...");
            Event checkme = new Event();
            checkme.setName("checkMe");
            checkme.setNote("Welcome to our mini calendar");
            checkme.setTag("");
            checkme.setStart(LocalDate.now());
            checkme.setEnd(LocalDate.now());
            checkme.setColor(Color.valueOf(Event.getDefaultColor()));

            insert(checkme);
        }


    }


}
