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
    private final ObservableList<EventAdapter> events;
    private CalendarDAO<Event> eventSource;

    @Autowired
    public EventManager(CalendarDAO<Event> eventSource) {
        this.eventSource = eventSource;
        events = FXCollections.observableArrayList();
    }

    //--------------------------- Simple CRUD Operation ---------------------------
    public void loadEvent() {
        eventSource.load().forEach(event -> events.add(new EventAdapter(event)));
    }

    public void addEvent(EventAdapter eventModel) {
        events.add(eventModel);
        eventSource.insert(eventModel.getBean());
    }

    public void removeEvent(EventAdapter eventModel) {
        boolean isRemove = events.remove(eventModel);
        if (isRemove) eventSource.delete(eventModel.getBean());
    }


    @SuppressWarnings("WeakerAccess")
    public void updateEvent(EventAdapter eventModel) {
        eventSource.update(eventModel.getBean());
    }


    //--------------------------- Feature Related ---------------------------
    public ObservableList<EventAdapter> search(String target) {
        return search(target, new MainSearcher());
    }

    @SuppressWarnings("WeakerAccess")
    public ObservableList<EventAdapter> search(String target, Searcher<EventAdapter> searcher) {
        return searcher.search(events, target);
    }


    //--------------------------- Accessor ----------------------------------

    public ObservableList<EventAdapter> getEvents(LocalDate from, LocalDate to) {
        return events.filtered(event -> event.inPeriod(from, to));
    }

    public ObservableList<EventAdapter> getEvents() {
        return events;
    }

    public CalendarDAO<Event> getEventSource() {
        return eventSource;
    }


}
