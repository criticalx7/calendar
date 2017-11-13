package client.controls;

import client.config.Setting;
import common.model.Event;
import javafx.scene.control.ButtonType;

import java.time.LocalDate;
/*
 * @author Chatchapol Rasameluangon
 * id 5810404901
 */


/**
 * A controller class in charge of managing event's repository
 * and ui constructor.
 */
public class MainController {
    private Setting setting;
    private EventManager eventManager;
    private ViewManager viewManager;

    public MainController(EventManager eventManager, ViewManager viewManager) {
        this.eventManager = eventManager;
        this.viewManager = viewManager;
        this.eventManager.loadEvent();
        this.viewManager.setupStageControl(this);
    }

    public void handleAdd(LocalDate occur) {
        Event temp = new Event(occur);
        if (viewManager.showEventEditor(temp)) {
            eventManager.addEvent(temp);
            viewManager.updateMonthView();
        }
    }

    // currently hacked
    public void handleRemove(Event event) {
        if (viewManager.showConfirmationDialog().filter(r -> r == ButtonType.OK).isPresent()) {
            eventManager.removeEvent(event);
            viewManager.updateMonthView();
        }
    }

    public void handleEdit(Event event) {
        if (viewManager.showEventEditor(event)) {
            eventManager.updateEvent(event);
            viewManager.updateMonthView();
        }
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


    public ViewManager getViewManager() {
        return viewManager;
    }

}
