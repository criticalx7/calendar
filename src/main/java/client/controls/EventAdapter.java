package client.controls;

import client.config.Setting;
import common.model.Event;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

import java.time.LocalDate;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */


/**
 * The POJO wrapper to be used in Views layer.
 * This provide properties for various bindings.
 */
public class EventAdapter {
    private Event bean;
    private final SimpleStringProperty name = new SimpleStringProperty(this, "name");
    private final SimpleStringProperty note = new SimpleStringProperty(this, "note");
    private final SimpleObjectProperty<LocalDate> start = new SimpleObjectProperty<>(this, "start");
    private final SimpleObjectProperty<LocalDate> end = new SimpleObjectProperty<>(this, "end");
    private final SimpleObjectProperty<Color> color = new SimpleObjectProperty<>(this, "color");
    private final SimpleBooleanProperty recurred = new SimpleBooleanProperty(this, "recurred");
    private final SimpleBooleanProperty yearly = new SimpleBooleanProperty(this, "yearly");
    private final SimpleBooleanProperty monthly = new SimpleBooleanProperty(this, "monthly");

    public EventAdapter() {
        // do nothing
    }

    public EventAdapter(Event bean) {
        this.bean = bean;
        reload();
    }

    public void reset() {
        name.set("");
        note.set("");
        start.set(LocalDate.now());
        end.set(LocalDate.now());
        color.set(Color.valueOf(Setting.getDefaultColor()));
        recurred.set(false);
        yearly.set(false);
        monthly.set(false);
    }

    public void reload() {
        name.set(bean.getName());
        note.set(bean.getNote());
        start.set(bean.getStart());
        end.set(bean.getEnd());
        color.set(Color.valueOf(bean.getColor()));
        recurred.set(bean.isRecurred());
        yearly.set(bean.isYearly());
        monthly.set(bean.isMonthly());
    }

    public void save() {
        if (bean != null) {
            bean.setName(name.get());
            bean.setNote(note.get());
            bean.setStart(start.get());
            bean.setEnd(end.get());
            bean.setColor(color.get().toString());
            bean.setRecurred(recurred.get());
            bean.setYearly(yearly.get());
            bean.setMonthly(monthly.get());
        }
    }

    boolean inPeriod(LocalDate from, LocalDate to) {
        return start.get().isAfter(from) && start.get().isBefore(to);
    }

    // -----------------------  Accessor -----------------------

    Event getBean() {
        return bean;
    }

    void setBean(Event bean) {
        this.bean = bean;
    }

    void setAndReload(Event bean) {
        this.bean = bean;
        reload();
    }

    // -----------------------  Property -----------------------

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty noteProperty() {
        return note;
    }

    public SimpleObjectProperty<LocalDate> startProperty() {
        return start;
    }

    public SimpleObjectProperty<LocalDate> endProperty() {
        return end;
    }

    public SimpleObjectProperty<Color> colorProperty() {
        return color;
    }

    public SimpleBooleanProperty recurredProperty() {
        return recurred;
    }

    public SimpleBooleanProperty yearlyProperty() {
        return yearly;
    }

    public SimpleBooleanProperty monthlyProperty() {
        return monthly;
    }
}
