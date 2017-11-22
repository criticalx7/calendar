package client.controls;

import client.controls.search.MainSearcher;
import client.controls.search.Searcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/*
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


/**
 * A class in charge of manage all event's in-memory CRUD.
 */
@Component
public class EventManager {
    private final ObservableList<EventAdapter> events = FXCollections.observableArrayList();
    private final MainSearcher searcher = new MainSearcher();

    boolean add(EventAdapter eventModel) {
        return events.add(eventModel);
    }

    boolean remove(EventAdapter eventModel) {
        return events.remove(eventModel);
    }

    public ObservableList<EventAdapter> search(String target) {
        return search(target, searcher);
    }

    ObservableList<EventAdapter> search(String target, Searcher<EventAdapter> searcher) {
        return searcher.search(events, target);
    }


    //--------------------------- Accessor ----------------------------------

    public ObservableList<EventAdapter> getEvents(LocalDate from, LocalDate to) {
        return events.filtered(event -> event.inPeriod(from, to));
    }

    public ObservableList<EventAdapter> getEvents() {
        return events;
    }

}
