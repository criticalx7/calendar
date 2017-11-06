package controller;

import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import model.Event;

import java.time.LocalDate;

public class MainController {
    private EventManager eventManager;
    private ViewManager viewManager;

    public MainController(EventManager em, ViewManager vm) {
        eventManager = em;
        viewManager = vm;
        eventManager.loadEvent();
        viewManager.setupStageControl(this);
    }


    public ObservableList<Event> getEvents(LocalDate from, LocalDate to) {
        return eventManager.getEvents().filtered(e -> e.getStart().isAfter(from) && e.getStart().isBefore(to));
    }

    public boolean handleAdd(Event event) {
        boolean confirm = viewManager.showEventEditor(event);
        if (confirm) eventManager.addEvent(event);
        return confirm;
    }

    // currently hacked
    public void handleRemove(Event event) {
        boolean confirm = viewManager.showConfirmationDialog().filter(r -> r == ButtonType.OK).isPresent();
        if (confirm) eventManager.removeEvent(event);
    }


    public void handleEdit(Event event) {
        boolean confirm = viewManager.showEventEditor(event);
        if (confirm) eventManager.updateEvent(event);
    }

    public void handleSearch(String text) {
        viewManager.showSearchDialog(this, text);
    }

    public void start() {
        viewManager.show();
    }


    public EventManager getEventManager() {
        return eventManager;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public ViewManager getViewManager() {
        return viewManager;
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

}
