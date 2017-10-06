package model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class Event {

    private static DateTimeFormatter defaultDatePattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static String defaultColor = "0x7290c1ff";
    private static int primaryKey;
    private int id;
    private final SimpleStringProperty name = new SimpleStringProperty(this, "name");
    private final SimpleStringProperty note = new SimpleStringProperty(this, "note");
    private final SimpleStringProperty tag = new SimpleStringProperty(this, "tag");
    private final SimpleObjectProperty<LocalDate> start = new SimpleObjectProperty<>(this, "start");
    private final SimpleObjectProperty<LocalDate> end = new SimpleObjectProperty<>(this, "end");
    private final SimpleObjectProperty<Color> color = new SimpleObjectProperty<>(this, "color");
    private SimpleBooleanProperty cancel = new SimpleBooleanProperty(this, "cancel", false);

    public Event() {
        setName("");
        setNote("");
        setTag("");
        setStart(LocalDate.now());
        setEnd(LocalDate.now());
        setColor(Color.valueOf(defaultColor)); //nice blue color
    }

    public Event(LocalDate start) {
        this();
        setStart(start);
        setEnd(start);
    }

    public static DateTimeFormatter getDefaultDatePattern() {
        return defaultDatePattern;
    }

    public static String getDefaultColor() {
        return defaultColor;
    }

    public static int getPrimaryKey() {
        return primaryKey;
    }

    public static void setPrimaryKey(int primaryKey) {
        Event.primaryKey = primaryKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getNote() {
        return note.get();
    }

    public SimpleStringProperty noteProperty() {
        return note;
    }

    public void setNote(String note) {
        this.note.set(note);
    }

    public String getTag() {
        return tag.get();
    }

    public SimpleStringProperty tagProperty() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag.set(tag);
    }

    public LocalDate getStart() {
        return start.get();
    }

    public SimpleObjectProperty<LocalDate> startProperty() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start.set(start);
    }

    public LocalDate getEnd() {
        return end.get();
    }

    public SimpleObjectProperty<LocalDate> endProperty() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end.set(end);
    }

    public Color getColor() {
        return color.get();
    }

    public SimpleObjectProperty<Color> colorProperty() {
        return color;
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public boolean isCancel() {
        return cancel.get();
    }

    public SimpleBooleanProperty cancelProperty() {
        return cancel;
    }

    public void setCancel(boolean cancel) {
        this.cancel.set(cancel);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name=" + name +
                ", note=" + note +
                ", tag=" + tag +
                ", start=" + start +
                ", end=" + end +
                ", color=" + color +
                ", cancel=" + cancel +
                '}';
    }
}
