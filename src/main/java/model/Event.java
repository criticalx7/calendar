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
    private int id;
    private final SimpleStringProperty name = new SimpleStringProperty(this, "name");
    private final SimpleStringProperty note = new SimpleStringProperty(this, "note");
    private final SimpleStringProperty tag = new SimpleStringProperty(this, "tag");
    private final SimpleObjectProperty<LocalDate> start = new SimpleObjectProperty<>(this, "start");
    private final SimpleObjectProperty<LocalDate> end = new SimpleObjectProperty<>(this, "end");
    private final SimpleObjectProperty<Color> color = new SimpleObjectProperty<>(this, "color");
    private final SimpleBooleanProperty cancel = new SimpleBooleanProperty(this, "cancel", false);

    /**
     * Basic constructor for blank event.
     */
    public Event() {
        setName("event");
        setNote("");
        setTag("");
        setStart(LocalDate.now());
        setEnd(LocalDate.now());
        setColor(Color.valueOf(defaultColor)); //nice blue color
    }


    /**
     * Construct an event with a start time.
     *
     * @param start - Event's start time
     */
    public Event(LocalDate start) {
        this();
        setStart(start);
        setEnd(start);
    }


    /**
     * Getter for default date pattern.
     * This might get migrated to Setting class.
     *
     * @return A default date pattern
     */
    public static DateTimeFormatter getDefaultDatePattern() {
        return defaultDatePattern;
    }


    /**
     * Getter for default event's color .
     * This might get migrated to Setting class.
     *
     * @return A default event's color
     */
    public static String getDefaultColor() {
        return defaultColor;
    }

    // ----------------------- Getter and Setter -----------------------

    /**
     * @return An event's id based on persistence service
     */
    public int getId() {
        return id;
    }


    /**
     * @param id An event's id to be set.
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * @return An event's name
     */
    public String getName() {
        return name.get();
    }


    /**
     * @param name An event's name to be set.
     */
    public void setName(String name) {
        this.name.set(name);
    }


    /**
     * @return An event's note and description
     */
    public String getNote() {
        return note.get();
    }


    /**
     * @param note An event's note and description to be set.
     */
    public void setNote(String note) {
        this.note.set(note);
    }


    /**
     * @return An event's tag
     */
    public String getTag() {
        return tag.get();
    }


    /**
     * @param tag An event's tag to be set.
     */
    public void setTag(String tag) {
        this.tag.set(tag);
    }


    /**
     * @return An event's start time
     */
    public LocalDate getStart() {
        return start.get();
    }


    /**
     * @param start An event's start time to be set.
     */
    public void setStart(LocalDate start) {
        this.start.set(start);
    }


    /**
     * @return An event's end time
     */
    public LocalDate getEnd() {
        return end.get();
    }


    /**
     * @param end An event's end time to be set.
     */
    public void setEnd(LocalDate end) {
        this.end.set(end);
    }

    /**
     * @return An event's color.
     */
    public Color getColor() {
        return color.get();
    }

    /**
     * @param color An event's color to be set.
     */
    public void setColor(Color color) {
        this.color.set(color);
    }

    /**
     * @return An event's cancel state
     */
    public boolean isCancel() {
        return cancel.get();
    }


    /**
     * @param cancel cancel state
     */
    public void setCancel(boolean cancel) {
        this.cancel.set(cancel);
    }

    // ----------------------- Properties -----------------------

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty noteProperty() {
        return note;
    }

    public SimpleStringProperty tagProperty() {
        return tag;
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

    public SimpleBooleanProperty cancelProperty() {
        return cancel;
    }

}
