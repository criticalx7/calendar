package persistence;

import javafx.application.Application;
import javafx.stage.Stage;
import model.EventManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

public class LoadTaskTest extends Application {
    private static final Logger LOGGER = Logger.getLogger(LoadTaskTest.class.getName());
    private DBManager dbManager;
    private EventManager eventManager;

    @BeforeClass
    public static void setUpClass() throws InterruptedException {
        // Initialise Java FX

        LOGGER.info("About to launch FX App");
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(LoadTaskTest.class);
            }
        };
        t.setDaemon(true);
        t.start();
        LOGGER.info("JavaFX thread started");
        Thread.sleep(10000);
    }

    @Before
    public void setup() throws Exception {
        eventManager = new EventManager();
        dbManager = new DBManager(eventManager, "jdbc:sqlite:EventsTest.db");
        eventManager.setEventSource(dbManager);
    }

    @Test
    public void load() throws Exception {
        dbManager.load();
        while (!dbManager.getTaskFuture().isDone()) {
            // do nothing
        }
        int size = eventManager.getEvents().size();
        String name = eventManager.getEvents().get(0).getName();
        assertEquals( 1, size);
        assertEquals( "test", name);
        LOGGER.info(String.format("Size: %d%nName: %s", size, name));

    }

    @After
    public void after() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // dont need to implement
    }
}