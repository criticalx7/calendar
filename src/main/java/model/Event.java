package model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

import java.time.LocalDate;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class Event {
    private static int primaryKey;
    private int id;
    private SimpleStringProperty name = new SimpleStringProperty(this, "name");
    private SimpleStringProperty note = new SimpleStringProperty(this, "note");
    private SimpleStringProperty tag = new SimpleStringProperty(this, "tag");
    private SimpleObjectProperty<LocalDate> start = new SimpleObjectProperty<>(this, "start");
    private SimpleObjectProperty<LocalDate> end = new SimpleObjectProperty<>(this, "end");
    private SimpleObjectProperty<Color> color = new SimpleObjectProperty<>(this, "color");


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


}
