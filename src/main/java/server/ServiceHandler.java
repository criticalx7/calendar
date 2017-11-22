package server;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

import common.model.Event;
import common.services.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.persistence.SimpleDAO;

import java.util.List;


@Service
public class ServiceHandler implements CalendarService {
    private final SimpleDAO<Event> dao;

    @Autowired
    ServiceHandler(SimpleDAO<Event> dao) {
        this.dao = dao;
    }

    @Override
    public List<Event> loadEvent() {
        return dao.load();
    }

    @Override
    public Event addEvent(Event event) {
        dao.insert(event);
        return event;
    }

    @Override
    public Event deleteEvent(Event event) {
        dao.delete(event);
        return event;
    }

    @Override
    public Event updateEvent(Event event) {
        dao.update(event);
        return event;
    }
}
