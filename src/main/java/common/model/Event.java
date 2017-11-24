package common.model;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

import client.config.Setting;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * An basic event model.
 */
public class Event implements Serializable {
    private static final long serialVersionUID = 8364052920806408575L;
    private int id;
    private String name;
    private String note;
    private LocalDate start;
    private LocalDate end;
    private String color;
    private boolean recurred;
    private boolean yearly;
    private boolean monthly;

    public Event() {
        name = "";
        note = "";
        start = LocalDate.now();
        end = LocalDate.now();
        color = Setting.getDefaultColor();
        recurred = false;
        yearly = false;
        monthly = false;
    }

    public Event(LocalDate occurrence) {
        this();
        start = occurrence;
        end = occurrence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return id == event.getId();
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("Event:%n" +
                        " <id>     %d%n" +
                        " <name>   %s%n" +
                        " <date>   %s  -  %s%n" +
                        " <recur>  %s [Y: %s], [M: %s]%n",
                id, name, start, end, recurred, yearly, monthly);
    }

    // ----------------------- Getter and Setter -----------------------


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isRecurred() {
        return recurred;
    }

    public void setRecurred(boolean recurred) {
        this.recurred = recurred;
    }

    public boolean isYearly() {
        return yearly;
    }

    public void setYearly(boolean rYear) {
        this.yearly = rYear;
    }

    public boolean isMonthly() {
        return monthly;
    }

    public void setMonthly(boolean monthly) {
        this.monthly = monthly;
    }


}
