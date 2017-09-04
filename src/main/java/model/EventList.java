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

    public void addEvent(Event event) {
        events.add(event);
    }

    public void removeEvent(int removeIndex) {
        events.remove(removeIndex);
    }

    public ObservableList<Event> getEvents() {
        return events;
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
