package client.ui.monthview;

import client.controls.MainController;
import common.model.Event;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;

import java.time.LocalDate;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */
public class MonthViewModel {

    private MainController controller;
    private ObjectProperty<LocalDate> currentDate = new SimpleObjectProperty<>(LocalDate.now());

    public MonthViewModel(MainController controller) {
        this.controller = controller;
    }

    ObservableList<Event> query(LocalDate from, LocalDate to) {
        return controller.getEventManager().getEvents(from, to);
    }

    void add(LocalDate date) {
        controller.handleAdd(date);
    }

    void delete(Event event) {
        controller.handleRemove(event);
    }

    void edit(Event event) {
        controller.handleEdit(event);
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

    LocalDate getCurrentDate() {
        return currentDate.get();
    }

    private void setCurrentDate(LocalDate currentDate) {
        this.currentDate.set(currentDate);
    }

    ObjectProperty<LocalDate> currentDateProperty() {
        return currentDate;
    }
}
