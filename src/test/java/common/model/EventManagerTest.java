package common.model;

import client.controls.EventAdapter;
import client.controls.EventManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class EventManagerTest {
    private EventManager eventManager;
    private EventAdapter event1;
    private EventAdapter event2;

    @Before
    public void setUp() {
        eventManager = new EventManager();
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
        eventManager.loadEvent(getMockSource());
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

    private List<Event> getMockSource() {
        List<Event> source = new ArrayList<>();
        Event t1 = new Event();
        Event t2 = new Event();
        Event t3 = new Event();
        t1.setName("t1");
        t2.setName("t2");
        t3.setName("t3");
        source.add(t1);
        source.add(t2);
        source.add(t3);
        return source;
    }

}