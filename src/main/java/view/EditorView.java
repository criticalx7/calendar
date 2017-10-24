package view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import model.Event;
import utility.Utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class EditorView {

    @FXML
    private AnchorPane headerPane;
    @FXML
    private Label dayLabel;
    @FXML
    private Label monthYearLabel;
    @FXML
    private TextField nameField;
    @FXML
    private Label nameErrorText;
    @FXML
    private DatePicker startPicker;
    @FXML
    protected CheckBox repeatBox;
    @FXML
    protected CheckBox allDayBox;
    @FXML
    private TextField tagField;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private TextArea noteArea;
    @FXML
    private Button saveButton;

    private Event currentEvent;
    private boolean confirmation = false;

    public void initialize() {
        setupDatePicker();
        startPicker.setValue(LocalDate.now());
    }


    private void setupDatePicker() {
        DateTimeFormatter dt = Event.getDefaultDatePattern();
        StringConverter<LocalDate> dateStringConverter = new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? dt.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string, dt) : null;
            }
        };
        startPicker.setConverter(dateStringConverter);
    }


    @FXML
    public void handleAdd() {
        nameErrorText.setVisible(false);
        if (nameField.getText().isEmpty()) {
            nameErrorText.setVisible(true);
            nameField.requestFocus();
        } else {
            currentEvent.setName(nameField.getText());
            currentEvent.setStart(startPicker.getValue());
            currentEvent.setEnd(startPicker.getValue());
            currentEvent.setTag(tagField.getText());
            currentEvent.setColor(colorPicker.getValue());
            currentEvent.setNote(noteArea.getText());
            confirmation = true;
            saveButton.getScene().getWindow().hide();
        }
    }

    @FXML
    private void changeColor() {
        Color c = colorPicker.getValue();
        headerPane.setStyle(Utility.getBorderColorFX(c));
    }


    @FXML
    public void prepareHeader() {
        String day = String.valueOf(startPicker.getValue().getDayOfMonth());
        String month = startPicker.getValue().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        String year = String.valueOf(startPicker.getValue().getYear());
        dayLabel.setText(day);
        monthYearLabel.setText(String.format("%s  %s", month, year));
    }


    public boolean isConfirm() {
        return confirmation;
    }


    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
        nameField.setText(currentEvent.getName());
        noteArea.setText(currentEvent.getNote());
        tagField.setText(currentEvent.getTag());
        startPicker.setValue(currentEvent.getStart());
        colorPicker.setValue(currentEvent.getColor());
        changeColor();
    }

}
