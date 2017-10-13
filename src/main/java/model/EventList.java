package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class EventList {
    private final ObservableList<Event> events = FXCollections.observableArrayList();
    private final ObjectProperty<Event> currentEvent = new SimpleObjectProperty<>(null);
    private EventSource eventSource;

    public void loadEvent() {
        if (eventSource != null) eventSource.load();
    }

    public void addEvent(Event event) {
        Event.setPrimaryKey(Event.getPrimaryKey() + 1);
        event.setId(Event.getPrimaryKey());
        events.add(event);
        if (eventSource != null) new Thread(() -> eventSource.insert(event)).start();
    }

    public void removeEvent(int removeIndex) {
        Event event = events.get(removeIndex);
        events.remove(removeIndex);
        if (eventSource != null) new Thread(() -> eventSource.delete(event)).start();
    }

    public void cancelEvent(Event event) {
        event.setCancel(true);
        if (eventSource != null) new Thread(() -> eventSource.delete(event)).start();
    }

    public void editEvent(Event event) {
        if (eventSource != null) eventSource.update(event);
    }


    public ObservableList<Event> getEvents() {
        return events;
    }


    public void setEventSource(EventSource source) {
        this.eventSource = source;
    }


    public final Event getCurrentEvent() {
        return currentEvent.get();
    }

    public final void setCurrentEvent(Event event) {
        currentEvent.set(event);
    }

    public ObjectProperty<Event> currentEventProperty() {
        return currentEvent;
    }

}
