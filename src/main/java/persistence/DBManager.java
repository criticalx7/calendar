package persistence;

import javafx.concurrent.Task;
import model.Event;
import model.EventManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */
public class DBManager implements EventSource {
    private static final AtomicInteger primaryKey = new AtomicInteger(0);
    private static String DB_URL;
    private final EventManager eventManager;
    private final ExecutorService executor;
    private Task currentTask;
    private Future taskFuture;

    /**
     * Basic constructor which accept model.
     * This uses default URL based on user home directory.
     *
     * @param eventManager A Event model
     */
    public DBManager(EventManager eventManager) {
        DB_URL = String.format("jdbc:sqlite:%s/CalendarDB/Events.db", System.getProperty("user.home"));
        this.eventManager = eventManager;
        executor = Executors.newFixedThreadPool(1);
    }


    /**
     * Constructor which accepts both model and URL.
     * This allows custom source location to be specify.
     *
     * @param eventManager A Event model
     * @param url          Database driver url
     */
    DBManager(EventManager eventManager, String url) {
        this(eventManager);
        DB_URL = url;
    }


    /**
     * Issues a task to setup directory and database file.
     */
    @Override
    public void setup() {
        currentTask = new SetupTask();
        taskFuture = executor.submit(currentTask);
    }


    /**
     * Issues a task to query the event records,
     * process them into objects and set them into the events list.
     */
    @Override
    public void load() {
        LoadTask loadTask = new LoadTask();
        loadTask.setOnSucceeded(event -> eventManager.getEvents().setAll(loadTask.getValue()));
        currentTask = loadTask;
        taskFuture = executor.submit(currentTask);
    }


    /**
     * Issue a task to insert the event records.
     *
     * @param event - event to be insert
     */
    @Override
    public void insert(Event event) {
        event.setId(primaryKey.incrementAndGet());
        currentTask = new InsertTask(event);
        taskFuture = executor.submit(currentTask);
    }


    /**
     * Issue a task to delete the event records
     * based on primary key.
     *
     * @param event - Event to be removed
     */
    @Override
    public void delete(Event event) {
        currentTask = new DeleteTask(event);
        taskFuture = executor.submit(currentTask);
    }


    /**
     * Issue a task to update the event records
     * based on primary key.
     *
     * @param event - event to be update
     */
    @Override
    public void update(Event event) {
        currentTask = new UpdateTask(event);
        taskFuture = executor.submit(currentTask);
    }


    /**
     * Shutdown all the threads running.
     */
    @Override
    public void close() {
        executor.shutdown();
    }


    /**
     * Fetch a database connection.
     *
     * @return A database connection
     * @throws SQLException connection can't be established.
     */
    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Getter of a primary key.
     *
     * @return A table's primary key
     */
    static AtomicInteger getPrimaryKey() {
        return primaryKey;
    }


    /**
     * Get a current task Future for observation.
     *
     * @return Current task's Future
     */
    public Future getTaskFuture() {
        return taskFuture;
    }

}
