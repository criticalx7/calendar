package persitence;

import javafx.concurrent.Task;
import model.Event;
import model.EventList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */
public class DBManager implements EventSource {
    private static String DB_URL;
    private final EventList eventList;
    private final ExecutorService executor;
    private Task currentTask;
    private Future taskFuture;

    /**
     * Basic constructor which accept model.
     * This uses default URL based on user home directory.
     *
     * @param eventList A Event model
     */
    public DBManager(EventList eventList) {
        DB_URL = String.format("jdbc:sqlite:%s/CalendarDB/Events.db", System.getProperty("user.home"));
        this.eventList = eventList;
        executor = Executors.newFixedThreadPool(1);
    }


    /**
     * Constructor which accepts both model and URL.
     * This allows custom source location to be specify.
     *
     * @param eventList A Event model
     * @param url       Database driver url
     */
    public DBManager(EventList eventList, String url) {
        this(eventList);
        DB_URL = url;
    }


    /**
     * Issues a task to setup directory and database file.
     */
    public void setup() {
        currentTask = new SetupTask();
        taskFuture = executor.submit(currentTask);
    }


    /**
     * Issues a task to query the event records,
     * process them into objects and set them into the events list.
     */
    public void load() {
        LoadTask loadTask = new LoadTask();
        loadTask.setOnSucceeded(event -> eventList.getEvents().setAll(loadTask.getValue()));
        currentTask = loadTask;
        // block the thread because current viewModel still not allow for immediate update.
        taskFuture = executor.submit(currentTask);
    }


    /**
     * Issue a task to insert the event records.
     *
     * @param event - event to be insert
     */
    public void insert(Event event) {
        currentTask = new InsertTask(event);
        executor.submit(currentTask);
    }


    /**
     * Issue a task to delete the event records
     * based on primary key.
     *
     * @param event - Event to be removed
     */
    public void delete(Event event) {
        currentTask = new DeleteTask(event);
        executor.submit(currentTask);
    }


    /**
     * Issue a task to update the event records
     * based on primary key.
     *
     * @param event - event to be update
     */
    public void update(Event event) {
        currentTask = new UpdateTask(event);
        executor.submit(currentTask);
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

    public Future getTaskFuture() {
        return taskFuture;
    }
}
