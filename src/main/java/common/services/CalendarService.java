package common.services;

import common.model.Event;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */


@Service
public interface CalendarService {

    List<Event> loadEvent();

    void addEvent(Event event);

    void deleteEvent(Event event);

    void updateEvent(Event event);
}