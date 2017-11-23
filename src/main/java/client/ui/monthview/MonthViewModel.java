package client.ui.monthview;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

import client.controls.ActionController;
import client.controls.EventAdapter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import java.time.LocalDate;


public class MonthViewModel {

    private final ActionController controller;
    private final ObjectProperty<LocalDate> currentDate = new SimpleObjectProperty<>(LocalDate.now());

    public MonthViewModel(ActionController controller) {
        this.controller = controller;
    }

    ObservableList<EventAdapter> query(LocalDate from, LocalDate to) {
        return controller.getEventManager().getEvents(from, to);
    }

    void add(LocalDate date) {
        controller.handleAdd(date);
    }

    void delete(EventAdapter eventModel) {
        controller.handleRemove(eventModel);
    }

    void edit(EventAdapter eventModel) {
        controller.handleEdit(eventModel);
    }

    void search(String target) {
        controller.handleSearch(target);
    }

    void nextMonth() {
        setCurrentDate(getCurrentDate().plusMonths(1));
    }

    void prevMonth() {
        setCurrentDate(getCurrentDate().minusMonths(1));
    }

    void refresh() {
        controller.handleLoad();
    }

    LocalDate getCurrentDate() {
        return currentDate.get();
    }

    void setCurrentDate(LocalDate currentDate) {
        this.currentDate.set(currentDate);
    }

    ObjectProperty<LocalDate> currentDateProperty() {
        return currentDate;
    }
}
