package server;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

import common.model.Event;
import org.junit.Before;
import org.junit.Test;
import server.persistence.SimpleDAO;
import util.EventsFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ServiceHandlerTest {
    private ServiceHandler serviceHandler;
    private MockDAO dao;

    @Before
    public void setUp() throws Exception {
        dao = new MockDAO();
        serviceHandler = new ServiceHandler(dao);
    }

    @Test
    public void loadEvent() throws Exception {
        List<Event> events = serviceHandler.loadEvent();
        assertEquals(3, events.size());
        assertTrue(dao.loaded);
    }

    @Test
    public void addEvent() throws Exception {
        serviceHandler.addEvent(new Event());
        assertTrue(dao.inserted);
    }

    @Test
    public void deleteEvent() throws Exception {
        serviceHandler.deleteEvent(new Event());
        assertTrue(dao.deleted);
    }

    @Test
    public void updateEvent() throws Exception {
        serviceHandler.updateEvent(new Event());
        assertTrue(dao.updated);
    }

    private class MockDAO implements SimpleDAO<Event> {
        List<Event> repository = new ArrayList<>();
        boolean loaded = false;
        boolean inserted = false;
        boolean deleted = false;
        boolean updated = false;

        MockDAO() {
            setup();
        }

        @Override
        public void setup() {
            repository.addAll(EventsFactory.getMockEvents("test1", "test2", "test3"));
        }

        @Override
        public List<Event> load() {
            List<Event> result = new ArrayList<>();
            result.addAll(repository);
            loaded = true;
            return result;
        }

        @Override
        public void insert(Event object) {
            inserted = true;
        }

        @Override
        public void delete(Event object) {
            deleted = true;
        }

        @Override
        public void update(Event object) {
            updated = true;
        }

        @Override
        public void close() {
            // nothing
        }
    }

}