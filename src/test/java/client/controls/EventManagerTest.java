package client.controls;

import client.controls.search.Searcher;
import common.model.Event;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.EventsFactory;

import java.time.LocalDate;

import static org.junit.Assert.*;


/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

public class EventManagerTest {
    private EventManager eventManager;
    private EventAdapter event;
    private EventAdapter event1;
    private EventAdapter event2;
    private static Searcher<EventAdapter> byNameSearcher;

    @BeforeClass
    public static void setupSearcher() {
        byNameSearcher = new ByNameSearcher();
    }

    @Before
    public void setup() throws Exception {
        eventManager = new EventManager();
        event = new EventAdapter(EventsFactory.getMockEvent(999, "test"));
        event1 = new EventAdapter(EventsFactory.getMockEvent(0, "one"));
        event2 = new EventAdapter(EventsFactory.getMockEvent(0, "two"));
        eventManager.getEvents().add(event1);
        eventManager.getEvents().add(event2);
    }

    @After
    public void tearDown() throws Exception {
        eventManager = null;
        event = null;
        event1 = null;
        event2 = null;
    }


    @Test
    public void addEvent() throws Exception {
        boolean isChange = eventManager.add(event);
        ObservableList<EventAdapter> events = eventManager.getEvents();
        Event last = events.get(events.size() - 1).getBean();
        assertTrue(isChange);
        assertEquals(999, last.getId());
        assertEquals("test", last.getName());
    }

    @Test
    public void removeEvent() throws Exception {
        boolean isChange = eventManager.remove(event1);
        assertTrue(isChange);
        assertEquals(1, eventManager.getEvents().size());
        assertEquals(0, eventManager.getEvents().get(0).getBean().getId());
        isChange = eventManager.remove(event2);
        assertTrue(isChange);
        assertEquals(0, eventManager.getEvents().size());
    }

    @Test
    public void removeEmpty() throws Exception {
        boolean isChange = eventManager.remove(event);
        assertFalse(isChange);
    }


    @Test
    public void searchByStrategy() throws Exception {
        ObservableList<EventAdapter> list = eventManager.search("one", byNameSearcher);
        assertEquals(1, list.size());
        assertEquals(0, list.get(0).getBean().getId());
        assertEquals("one", list.get(0).nameProperty().get());
    }

    @Test
    public void searchFail() throws Exception {
        ObservableList<EventAdapter> list = eventManager.search("test", byNameSearcher);
        assertEquals(0, list.size());
    }


    @Test
    public void queryByDate() throws Exception {
        LocalDate date = LocalDate.now().minusMonths(1);
        ObservableList<EventAdapter> list = eventManager.getEvents(date, date.plusMonths(2));
        assertEquals(2, list.size());
        assertEquals("one", list.get(0).nameProperty().get());
        assertEquals("two", list.get(1).nameProperty().get());
    }

    @Test
    public void queryByDateFail() throws Exception {
        LocalDate date = LocalDate.now().plusMonths(1);
        ObservableList<EventAdapter> list = eventManager.getEvents(date, date.plusWeeks(1));
        assertEquals(0, list.size());
    }


    private static class ByNameSearcher implements Searcher<EventAdapter> {
        @Override
        public ObservableList<EventAdapter> search(ObservableList<EventAdapter> source, String target) {
            return source.filtered(event -> event.nameProperty().get().compareToIgnoreCase(target) == 0);
        }
    }

}