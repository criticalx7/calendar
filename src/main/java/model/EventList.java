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

    private DBManager databaseManager;

    public void loadEvent() {
        databaseManager.load();
    }

    public void addEvent(Event event) {
        Event.setPrimaryKey(Event.getPrimaryKey() + 1);
        event.setId(Event.getPrimaryKey());
        events.add(event);
        if (databaseManager != null) databaseManager.insert(event);
    }

    public void removeEvent(int removeIndex) {
        int removeKey = events.get(removeIndex).getId();
        events.remove(removeIndex);
        if (databaseManager != null) databaseManager.delete(removeKey);

    }

    public void editEvent(Event event) {
        if (databaseManager != null) databaseManager.update(event);
    }


    public ObservableList<Event> getEvents() {
        return events;
    }


    public void setDatabaseManager(DBManager dbManager) {
        this.databaseManager = dbManager;
    }

    public DBManager getDatabaseManager() {
        return databaseManager;
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
