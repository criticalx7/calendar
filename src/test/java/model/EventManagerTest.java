package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class EventManagerTest {
    private EventManager eventManager;
    private Event dummyEvent;

    @Before
    public void setUp() throws Exception {
        eventManager = new EventManager();
        dummyEvent = new Event();
    }

    @After
    public void tearDown() throws Exception {
        eventManager = null;
        dummyEvent = null;
    }

    @Test
    public void addEvent() throws Exception {
        eventManager.addEvent(dummyEvent);
        assertEquals(dummyEvent.getId(), eventManager.getEvents().get(0).getId());
    }

    @Test
    public void removeEvent() throws Exception {
        eventManager.addEvent(dummyEvent);
        eventManager.removeEvent(0);
        assertEquals(0, eventManager.getEvents().size());
    }


    @Test
    public void getCurrentEvent() throws Exception {
        eventManager.setCurrentEvent(dummyEvent);
        assertEquals(dummyEvent, eventManager.getCurrentEvent());
    }

    @Test
    public void setCurrentEvent() throws Exception {
        eventManager.setCurrentEvent(dummyEvent);
        assertNotNull(eventManager.getCurrentEvent());
        assertEquals(dummyEvent, eventManager.getCurrentEvent());
    }


}