package common.model;

import client.config.Setting;

import java.io.Serializable;
import java.time.LocalDate;


/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

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

    @Override
    public String toString() {
        return String.format("Event" + " id = %d%n" +
                        " name = '%s'%n" +
                        " note = '%s'%n" +
                        " start = %s end = %s%n" +
                        " color = '%s'%n" +
                        " recurred = %s" + " yearly = %s, monthly = %s",
                id, name, note, start, end, color, recurred, yearly, monthly);
    }
}
