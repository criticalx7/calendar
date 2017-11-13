package client.controls;

import client.controls.search.MainSearcher;
import client.controls.search.Searcher;
import common.model.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.persistence.CalendarDAO;

import java.time.LocalDate;

/*
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


/**
 * A class in charge of manage all event's in-memory CRUD.
 */
@Service
public class EventManager {
    private final ObservableList<Event> events;
    private CalendarDAO<Event> eventSource;

    @Autowired
    public EventManager(CalendarDAO<Event> eventSource) {
        this.eventSource = eventSource;
        events = FXCollections.observableArrayList();
    }

    //--------------------------- Simple CRUD Operation ---------------------------
    public void loadEvent() {
        events.addAll(eventSource.load());
    }

    public void addEvent(Event event) {
        events.add(event);
        eventSource.insert(event);
    }

    public void removeEvent(Event event) {
        boolean isRemove = events.remove(event);
        if (isRemove) eventSource.delete(event);
    }


    @SuppressWarnings("WeakerAccess")
    public void updateEvent(Event event) {
        eventSource.update(event);
    }


    //--------------------------- Feature Related ---------------------------
    public ObservableList<Event> search(String target) {
        return search(target, new MainSearcher());
    }

    @SuppressWarnings("WeakerAccess")
    public ObservableList<Event> search(String target, Searcher searcher) {
        return searcher.search(events, target);
    }


    //--------------------------- Accessor ----------------------------------

    public ObservableList<Event> getEvents(LocalDate from, LocalDate to) {
        return events.filtered(event -> event.getStart().isAfter(from) && event.getStart().isBefore(to));
    }

    public ObservableList<Event> getEvents() {
        return events;
    }

    public CalendarDAO<Event> getEventSource() {
        return eventSource;
    }


}
