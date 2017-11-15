package common.model;

import client.controls.EventAdapter;
import client.controls.EventManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.persistence.CalendarDAO;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


class EventManagerTest {
    private EventManager eventManager;
    private EventAdapter event1;
    private EventAdapter event2;

    @Before
    public void setUp() {
        eventManager = new EventManager(new MockDatabase());
        event1 = new EventAdapter(new Event());
        event2 = new EventAdapter(new Event());
        event1.getBean().setId(999);
    }


    @After
    public void tearDown() {
        eventManager = null;
        event1 = null;
        event2 = null;
    }


    @Test
    public void loadEvent() {
        eventManager.loadEvent();
        assertEquals(3, eventManager.getEvents().size());
        assertEquals("t1", eventManager.getEvents().get(0).nameProperty().get());
        assertEquals("t2", eventManager.getEvents().get(1).nameProperty().get());
        assertEquals("t3", eventManager.getEvents().get(2).nameProperty().get());
    }


    @Test
    public void addEvent() {
        eventManager.addEvent(event1);
        eventManager.addEvent(event2);
        assertEquals(999, eventManager.getEvents().get(0).getBean().getId());
        assertEquals(0, eventManager.getEvents().get(1).getBean().getId());
    }


    @Test
    public void removeEvent() {
        eventManager.getEvents().add(event1);
        eventManager.getEvents().add(event2);
        eventManager.removeEvent(event1);
        assertEquals(1, eventManager.getEvents().size());
        assertEquals(0, eventManager.getEvents().get(0).getBean().getId());

    }

    @Test
    void updateEvent() {
        // to be implemented
    }


    // mock database class which do nothing
    class MockDatabase implements CalendarDAO<Event> {

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