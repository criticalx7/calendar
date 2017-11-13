package client.ui;

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
    private Event event;
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

    public EventAdapter(Event event) {
        this.event = event;
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
        name.set(event.getName());
        note.set(event.getNote());
        start.set(event.getStart());
        end.set(event.getEnd());
        color.set(Color.valueOf(event.getColor()));
        recurred.set(event.isRecurred());
        yearly.set(event.isYearly());
        monthly.set(event.isMonthly());
    }

    public void save() {
        if (event != null) {
            event.setName(name.get());
            event.setNote(note.get());
            event.setStart(start.get());
            event.setEnd(end.get());
            event.setColor(color.get().toString());
            event.setRecurred(recurred.get());
            event.setYearly(yearly.get());
            event.setMonthly(monthly.get());
        }
    }

    // -----------------------  Accessor -----------------------

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
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
