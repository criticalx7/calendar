package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EventList {
    private ObservableList<Event> events = FXCollections.observableArrayList();

    public void addEvent(Event event) {
        events.add(event);
    }

    public ObservableList<Event> getEvents() {
        return events;
    }

    public void setEvents(ObservableList<Event> events) {
        this.events = events;
    }


}
