package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EventListTest {
    private EventList eventList;
    private Event dummyEvent;

    @Before
    public void setUp() throws Exception {
        eventList = new EventList();
        dummyEvent = new Event();
    }

    @After
    public void tearDown() throws Exception {
        eventList = null;
        dummyEvent = null;
    }

    @Test
    public void addEvent() throws Exception {
        eventList.addEvent(dummyEvent);
        assertEquals(dummyEvent.getId(), eventList.getEvents().get(0).getId());
    }

    @Test
    public void removeEvent() throws Exception {
        eventList.addEvent(dummyEvent);
        eventList.removeEvent(0);
        assertEquals(0, eventList.getEvents().size());
    }


    @Test
    public void getCurrentEvent() throws Exception {
        eventList.setCurrentEvent(dummyEvent);
        assertEquals(dummyEvent, eventList.getCurrentEvent());
    }

    @Test
    public void setCurrentEvent() throws Exception {
        eventList.setCurrentEvent(dummyEvent);
        assertNotNull(eventList.getCurrentEvent());
        assertEquals(dummyEvent, eventList.getCurrentEvent());
    }


}