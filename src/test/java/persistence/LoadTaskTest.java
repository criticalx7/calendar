package persistence;

import de.saxsys.javafx.test.JfxRunner;
import javafx.collections.ObservableList;
import model.Event;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sqlite.SQLiteDataSource;

import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

@RunWith(JfxRunner.class)
public class LoadTaskTest {
    private static final Logger LOGGER = Logger.getLogger(LoadTaskTest.class.getName());
    private CalendarDAOImpl dbManager;
    private ObservableList<Event> mockEventList;


    @Before
    public void setup() throws Exception {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:EventsTest.db");
        dbManager = new CalendarDAOImpl(dataSource);
    }

    @Test
    public void load() throws Exception {
        mockEventList = dbManager.load();
        assertEquals(1, mockEventList.size());
        assertEquals("test", mockEventList.get(0).getName());
        LOGGER.info(String.format("Size: %d%nName: %s", mockEventList.size(), mockEventList.get(0)));
    }

    @After
    public void after() {
        dbManager.close();
        dbManager = null;
        mockEventList = null;
    }

}