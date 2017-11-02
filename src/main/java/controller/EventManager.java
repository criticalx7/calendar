package controller;

import common.search.MainSearcher;
import common.search.Searcher;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import persistence.CalendarDAO;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


@Service
public class EventManager {
    private final ObservableList<Event> events = FXCollections.observableArrayList();
    private final ObjectProperty<Event> currentEvent = new SimpleObjectProperty<>(null);
    private CalendarDAO eventSource;

    @Autowired
    public EventManager(CalendarDAO eventSource) {
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
        eventSource.update(event);
    }

    //--------------------------- Feature Related ---------------------------
    public ObservableList<Event> search(String target) {
        return search(target, new MainSearcher());
    }

    public ObservableList<Event> search(String target, Searcher searcher) {
        return searcher.search(events, target);
    }


    //--------------------------- Accessor ----------------------------------

    public ObservableList<Event> getEvents() {
        return events;
    }

    public void setEventSource(CalendarDAO source) {
        this.eventSource = source;
    }

    public CalendarDAO getEventSource() {
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
