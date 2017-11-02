package model;

import controller.EventManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import persistence.CalendarDAO;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class EventManagerTest {
    private EventManager eventManager;
    private Event event1;
    private Event event2;

    @Before
    public void setUp() throws Exception {
        eventManager = new EventManager(new MockDatabase());
        event1 = new Event();
        event2 = new Event();
        event1.setId(999);
    }


    @After
    public void tearDown() throws Exception {
        eventManager = null;
        event1 = null;
        event2 = null;
    }


    @Test
    public void loadEvent() throws Exception {
        eventManager.loadEvent();
        assertEquals(3, eventManager.getEvents().size());
        assertEquals("t1", eventManager.getEvents().get(0).getName());
        assertEquals("t2", eventManager.getEvents().get(1).getName());
        assertEquals("t3", eventManager.getEvents().get(2).getName());
    }


    @Test
    public void addEvent() throws Exception {
        eventManager.addEvent(event1);
        eventManager.addEvent(event2);
        assertEquals(999, eventManager.getEvents().get(0).getId());
        assertEquals(0, eventManager.getEvents().get(1).getId());
    }


    @Test
    public void removeEvent() throws Exception {
        eventManager.getEvents().add(event1);
        eventManager.getEvents().add(event2);
        eventManager.getEvents().remove(0);
        assertEquals(1, eventManager.getEvents().size());
        assertEquals(0, eventManager.getEvents().get(0).getId());

    }


    @Test
    public void getCurrentEvent() throws Exception {
        eventManager.setCurrentEvent(event1);
        assertEquals(event1, eventManager.getCurrentEvent());
    }


    @Test
    public void setCurrentEvent() throws Exception {
        eventManager.setCurrentEvent(event1);
        assertNotNull(eventManager.getCurrentEvent());
        assertEquals(event1, eventManager.getCurrentEvent());
    }


    // mock database class which do nothing
    class MockDatabase implements CalendarDAO {

        @Override
        public void setup() {
            // no implementation
        }

        @Override
        public ObservableList<Event> load() {
            Event ev = new Event();
            Event ev2 = new Event();
            Event ev3 = new Event();
            ev.setName("t1");
            ev2.setName("t2");
            ev3.setName("t3");
            return FXCollections.observableArrayList(List.of(ev, ev2, ev3));
        }

        @Override
        public void insert(Event event) {
            // no implementation
        }

        @Override
        public void delete(Event event) {
            // no implementation
        }

        @Override
        public void update(Event event) {
            // no implementation
        }

        @Override
        public void close() {
            // no implementation
        }
    }


}