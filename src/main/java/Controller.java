import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import model.Event;

import java.time.LocalDate;

public class Controller {

    @FXML protected TextField nameField;
    @FXML protected TextField tagField;
    @FXML protected TextArea noteArea;
    @FXML protected Button cancelButton;
    @FXML protected Button addButton;
    @FXML protected DatePicker startPicker;
    @FXML protected DatePicker endPicker;
    @FXML protected ColorPicker colorPicker;

    @FXML
    public void initialize() {
        startPicker.setValue(LocalDate.now());
        endPicker.setValue(LocalDate.now());
    }

    @FXML
    public void addEvent(ActionEvent e) {
        if (nameField.getText().isEmpty()) {
            System.out.println("No name");
        } else if (startPicker.getValue().compareTo(endPicker.getValue()) == 1) {
            System.out.println("End before Start");
        } else {
            Event event = new Event();
            event.setTitle(nameField.getText());
            event.setStart(startPicker.getValue());
            event.setEnd(endPicker.getValue());
            event.setTag(tagField.getText());
            event.setColor(colorPicker.getValue());
            event.setNote(noteArea.getText());

            System.out.println(event);
        }
    }

    @FXML
    public void closeWindow(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


}
