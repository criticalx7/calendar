package client.controls;

import client.controls.search.MainSearcher;
import client.controls.search.Searcher;
import common.model.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

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


    //--------------------------- Simple CRUD Operation ---------------------------
    public void loadEvent(List<Event> source) {
        events.clear();
        source.forEach(event -> events.add(new EventAdapter(event)));
    }

    public void addEvent(EventAdapter eventModel) {
        events.add(eventModel);
    }

    public void removeEvent(EventAdapter eventModel) {
        events.remove(eventModel);
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

}
