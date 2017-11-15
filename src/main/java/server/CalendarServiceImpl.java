package server;

import common.model.Event;
import common.services.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.persistence.CalendarDAO;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */
@Service
public class CalendarServiceImpl implements CalendarService {

    private final CalendarDAO<Event> dao;
    private final Logger logger = Logger.getLogger(CalendarServiceImpl.class.getName());

    @Autowired
    CalendarServiceImpl(CalendarDAO<Event> dao) {
        this.dao = dao;
    }

    @Override
    public List<Event> loadEvent() {
        List<Event> result = dao.load();
        logger.info(String.format("Size :%d", result.size()));
        return dao.load();
    }

    @Override
    public void addEvent(Event event) {
        dao.insert(event);
    }

    @Override
    public void deleteEvent(Event event) {
        dao.delete(event);
    }

    @Override
    public void updateEvent(Event event) {
        dao.update(event);
    }
}
