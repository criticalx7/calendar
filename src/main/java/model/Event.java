package model;

import javafx.scene.paint.Color;

import java.time.LocalDate;

public class Event {
    private String title;
    private String note;
    private String tag;
    private LocalDate start;
    private LocalDate end;
    private Color color;


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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {

        return note;
    }

    public void setNote(String note) {

        this.note = note;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", note='" + note + '\'' +
                ", tag='" + tag + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", color=" + color +
                '}';
    }
}
