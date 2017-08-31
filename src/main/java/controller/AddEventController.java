package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class AddEventController {

    @FXML
    protected TextField nameField;
    @FXML
    protected TextField tagField;
    @FXML
    protected TextArea noteArea;
    @FXML
    protected Button addButton;
    @FXML
    protected DatePicker startPicker;
    @FXML
    protected DatePicker endPicker;
    @FXML
    protected ColorPicker colorPicker;
    @FXML
    protected AnchorPane colorPane;


    @FXML
    public void initialize() {
        setupDateFormatter();
        startPicker.setValue(LocalDate.now());
        endPicker.setValue(LocalDate.now());
        colorPicker.setValue(Color.valueOf("#7290c1"));
    }

    @FXML
    public void addEvent(ActionEvent e) {
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
            Event event = new Event();
            event.setName(nameField.getText());
            event.setStart(startPicker.getValue());
            event.setEnd(endPicker.getValue());
            event.setTag(tagField.getText());
            event.setColor(colorPicker.getValue());
            event.setNote(noteArea.getText());
            MainController.getEventList().addEvent(event);
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void changeColor(ActionEvent e) {
        Color c = colorPicker.getValue();
        colorPane.setStyle(MainController.getBackgroundColorFX(c));
    }


    private void setupDateFormatter() {
        StringConverter<LocalDate> dateStringConverter = new StringConverter<LocalDate>() {
            String pattern = "dd-MM-yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        startPicker.setConverter(dateStringConverter);
        endPicker.setConverter(dateStringConverter);
    }

}
