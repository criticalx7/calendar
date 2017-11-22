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
public class ActionController {
    private final EventManager eventManager;
    private final ViewManager viewManager;
    private CalendarService calendarService;

    public ActionController(EventManager eventManager, ViewManager viewManager) {
        this.eventManager = eventManager;
        this.viewManager = viewManager;
        this.viewManager.setupStageControl(this);
    }

    public void handleLoad() {
        eventManager.getEvents().clear();
        calendarService.loadEvent().forEach(e -> eventManager.getEvents().add(new EventAdapter(e)));
        viewManager.updateMonthView();
    }

    public void handleAdd(LocalDate occur) {
        EventAdapter temp = new EventAdapter(new Event(occur));
        if (viewManager.showEventEditor(temp)) {
            Event event = calendarService.addEvent(temp.getBean());
            temp.setAndReload(event);
            eventManager.add(temp);
            viewManager.updateMonthView();
        }
    }

    // currently hacked
    public void handleRemove(EventAdapter eventModel) {
        if (viewManager.showConfirmationDialog().filter(r -> r == ButtonType.OK).isPresent()) {
            Event event = calendarService.deleteEvent(eventModel.getBean());
            eventModel.setAndReload(event);
            eventManager.remove(eventModel);
            viewManager.updateMonthView();
        }
    }

    public void handleEdit(EventAdapter eventModel) {
        EventAdapter temp = new EventAdapter(eventModel.getBean());
        if (viewManager.showEventEditor(temp)) {
            eventModel.reload();
            calendarService.updateEvent(eventModel.getBean());
            viewManager.updateMonthView();
        }
    }

    public void handleSearch(String text) {
        viewManager.showSearchDialog(this, text);
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
