package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Event;
import utility.Utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class ProcessEventController {

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
    private Stage dialogStage;
    private boolean confirmation = false;

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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("There was an ERROR");
            alert.setHeaderText("No event's name");
            alert.setContentText("Please put at least one character in the name field");
            alert.showAndWait();
            nameField.requestFocus();
        } else if (startPicker.getValue().isAfter(endPicker.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("There was an ERROR");
            alert.setHeaderText("End is before start");
            alert.setContentText("Your event's end time is before start time. Please change times" +
                    "so that start time is equal to or before end time");
            alert.showAndWait();
        } else {
            currentEvent.setName(nameField.getText());
            currentEvent.setStart(startPicker.getValue());
            currentEvent.setEnd(endPicker.getValue());
            currentEvent.setTag(tagField.getText());
            currentEvent.setColor(colorPicker.getValue());
            currentEvent.setNote(noteArea.getText());
            confirmation = true;
            dialogStage.close();
        }
    }

    @FXML
    public void changeColor() {
        Color c = colorPicker.getValue();
        colorPane.setStyle(Utility.getInstance().getBackgroundColorFX(c));
    }

    void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;

        nameField.setText(currentEvent.getName());
        noteArea.setText(currentEvent.getNote());
        tagField.setText(currentEvent.getTag());
        startPicker.setValue(currentEvent.getStart());
        endPicker.setValue(currentEvent.getEnd());
        colorPicker.setValue(currentEvent.getColor());
        changeColor();
    }

    void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    boolean isConfirm() {
        return confirmation;
    }
}