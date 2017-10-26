package persistence;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import model.Event;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */
public class DatabaseManager implements EventSource {
    static final AtomicInteger primaryKey = new AtomicInteger(0);
    private final ExecutorService executor = Executors.newFixedThreadPool(1);
    private static DataSource dataSource;
    private Task currentTask;
    private Future taskFuture;


    /**
     * Basic constructor which accept model.
     * This uses default URL based on user home directory.
     */
    public DatabaseManager(DataSource source) {
        dataSource = source;
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
    public ObservableList<Event> load() {
        ObservableList<Event> result = null;
        LoadTask loadTask = new LoadTask();
        //loadTask.setOnSucceeded(e -> events.addAll(loadTask.getValue()));
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
     * based on primary key.
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
     * based on primary key.
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
        executor.shutdown();
    }


    /**
     * Fetch a database connection.
     *
     * @return A database connection
     * @throws SQLException connection can't be established.
     */
    static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
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
