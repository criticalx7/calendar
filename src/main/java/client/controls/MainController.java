package client.controls;

import common.model.Event;
import common.services.CalendarService;
import javafx.scene.control.ButtonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
/*
 * @author Chatchapol Rasameluangon
 * id 5810404901
 */


/**
 * A controller class in charge of managing event's repository
 * and ui constructor.
 */

@Component
public class MainController {
    private EventManager eventManager;
    private ViewManager viewManager;
    private CalendarService calendarService;

    public MainController(EventManager eventManager, ViewManager viewManager) {
        this.eventManager = eventManager;
        this.viewManager = viewManager;
        this.viewManager.setupStageControl(this);
    }

    public void handleLoad() {
        eventManager.loadEvent(calendarService.loadEvent());
        viewManager.updateMonthView();
    }

    public void handleAdd(LocalDate occur) {
        EventAdapter temp = new EventAdapter(new Event(occur));
        if (viewManager.showEventEditor(temp)) {
            eventManager.addEvent(temp);
            calendarService.addEvent(temp.getBean());
            viewManager.updateMonthView();
        }
    }

    // currently hacked
    public void handleRemove(EventAdapter eventModel) {
        if (viewManager.showConfirmationDialog().filter(r -> r == ButtonType.OK).isPresent()) {
            eventManager.removeEvent(eventModel);
            calendarService.deleteEvent(eventModel.getBean());
            viewManager.updateMonthView();
        }
    }

    public void handleEdit(EventAdapter eventModel) {
        if (viewManager.showEventEditor(eventModel)) {
            calendarService.updateEvent(eventModel.getBean());
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

    @Autowired
    public void setCalendarService(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

}
