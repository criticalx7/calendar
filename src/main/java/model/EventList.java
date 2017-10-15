package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.EventSource;

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
        events.add(event);
        if (eventSource != null) eventSource.insert(event);
    }

    public void removeEvent(int removeIndex) {
        Event event = events.get(removeIndex);
        events.remove(removeIndex);
        if (eventSource != null) eventSource.delete(event);
    }

    public void cancelEvent(Event event) {
        event.setCancel(true);
        if (eventSource != null) eventSource.delete(event);
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
