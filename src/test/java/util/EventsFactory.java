package util;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

import common.model.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventsFactory {

    private EventsFactory() {}

    public static List<Event> getMockEvents(String... names) {
        int key = 1;
        List<Event> events = new ArrayList<>();
        for (String name : names) {
            Event event = new Event(LocalDate.now());
            event.setId(key);
            event.setName(name);
            events.add(event);
            key++;
        }
        return events;
    }

    public static Event getMockEvent(int id, String name) {
        Event event = new Event(LocalDate.now());
        event.setId(id);
        event.setName(name);
        return event;
    }
}
