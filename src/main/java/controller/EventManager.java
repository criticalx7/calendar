package controller;

import common.MainSearcher;
import common.Searcher;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import persistence.EventSource;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


@Component
public class EventManager {
    private final ObservableList<Event> events = FXCollections.observableArrayList();
    private final ObjectProperty<Event> currentEvent = new SimpleObjectProperty<>(null);
    private EventSource eventSource;

    @Autowired
    public EventManager(EventSource eventSource) {
        this.eventSource = eventSource;
    }

    //--------------------------- Simple CRUD Operation ---------------------------
    public void loadEvent() {
        events.setAll(eventSource.load());
    }

    public void addEvent(Event event) {
        events.add(event);
        eventSource.insert(event);
    }

    public void removeEvent(int removeIndex) {
        Event event = events.get(removeIndex);
        events.remove(removeIndex);
        eventSource.delete(event);
    }

    public void cancelEvent(Event event) {
        event.setCancel(true);
        eventSource.delete(event);
    }

    public void editEvent(Event event) {
        if (eventSource != null) eventSource.update(event);
    }

    //--------------------------- Feature Related ---------------------------
    public ObservableList<Event> search(String target) {
        return (new MainSearcher()).search(events, target);
    }

    public ObservableList<Event> search(String target, Searcher searcher) {
        return searcher.search(events, target);
    }


    //--------------------------- Accessor ----------------------------------

    public ObservableList<Event> getEvents() {
        return events;
    }

    public void setEventSource(EventSource source) {
        this.eventSource = source;
    }

    public EventSource getEventSource() {
        return eventSource;
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
