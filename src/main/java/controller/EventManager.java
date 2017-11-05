package controller;

import common.search.MainSearcher;
import common.search.Searcher;
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
    private final ObservableList<Event> events;
    private CalendarDAO eventSource;

    @Autowired
    public EventManager(CalendarDAO eventSource) {
        this.eventSource = eventSource;
        events = FXCollections.observableArrayList();
    }

    //--------------------------- Simple CRUD Operation ---------------------------
    public void loadEvent() {
        events.setAll(eventSource.load());
    }

    public void addEvent(Event event) {
        events.add(event);
        eventSource.insert(event);
    }

    public void removeEvent(Event event) {
        boolean isRemove = events.remove(event);
        if (isRemove) eventSource.delete(event);
    }


    public void updateEvent(Event event) {
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

    public CalendarDAO getEventSource() {
        return eventSource;
    }


}
