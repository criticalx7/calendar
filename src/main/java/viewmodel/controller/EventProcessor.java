package viewmodel.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import model.Event;
import utility.Utility;
import view.ViewManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class EventProcessor {

    @FXML
    protected TextField nameField;
    @FXML
    protected TextField tagField;
    @FXML
    protected TextArea noteArea;
    @FXML
    protected Button okButton;
    @FXML
    protected DatePicker startPicker;
    @FXML
    protected DatePicker endPicker;
    @FXML
    protected ColorPicker colorPicker;
    @FXML
    protected AnchorPane colorPane;

    private DateTimeFormatter dateTimeFormatter;
    private Event currentEvent;
    private boolean confirmation = false;
    private ViewManager viewManager;

    public void initialize() {
        setupDatePicker();
        startPicker.setValue(LocalDate.now());
        endPicker.setValue(LocalDate.now());
    }


    private void setupDatePicker() {
        StringConverter<LocalDate> dateStringConverter = new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? dateTimeFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string, dateTimeFormatter) : null;
            }
        };
        startPicker.setConverter(dateStringConverter);
        endPicker.setConverter(dateStringConverter);
    }


    @FXML
    public void handleAdd() {
        if (nameField.getText().isEmpty()) {
            viewManager.showErrorDialog(
                    "There was an ERROR",
                    "No event's name",
                    "Please put at least one character in the name field");
            nameField.requestFocus();
        } else if (startPicker.getValue().isAfter(endPicker.getValue())) {
            viewManager.showErrorDialog(
                    "There was an ERROR",
                    "No event's name",
                    "Your event's end time is before start time. Please change times" +
                            "so that start time is equal to or before end time");

        } else {
            currentEvent.setName(nameField.getText());
            currentEvent.setStart(startPicker.getValue());
            currentEvent.setEnd(endPicker.getValue());
            currentEvent.setTag(tagField.getText());
            currentEvent.setColor(colorPicker.getValue());
            currentEvent.setNote(noteArea.getText());
            confirmation = true;
            okButton.getScene().getWindow().hide();
        }
    }

    @FXML
    public void changeColor() {
        Color c = colorPicker.getValue();
        colorPane.setStyle(Utility.getBackgroundColorFX(c));
    }

    public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;

        nameField.setText(currentEvent.getName());
        noteArea.setText(currentEvent.getNote());
        tagField.setText(currentEvent.getTag());
        startPicker.setValue(currentEvent.getStart());
        endPicker.setValue(currentEvent.getEnd());
        colorPicker.setValue(currentEvent.getColor());
        changeColor();
    }

    public boolean isConfirm() {
        return confirmation;
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }
}
