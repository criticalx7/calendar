package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Event;
import model.EventList;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class MainController {

    @FXML
    protected Button addEventButton;
    @FXML
    protected TableView<Event> eventTable;
    @FXML
    protected TableColumn<Event, LocalDate> dateCol;
    @FXML
    protected TableColumn<Event, Color> colorCol;
    @FXML
    protected Label nameLabel;
    @FXML
    protected Label timeLabel;
    @FXML
    protected Label tagLabel;
    @FXML
    protected TextArea noteTextArea;
    @FXML
    protected AnchorPane colorRectangle;

    private static EventList eventList;

    @FXML
    public void initialize() {
        eventList = new EventList();
        setupTable();
    }

    @FXML
    public void popUpEventAdder(ActionEvent e) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/AddEvent.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Event");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Node) e.getSource()).getScene().getWindow());
        stage.show();
    }


    private void setupTable() {
        eventTable.setItems(eventList.getEvents());
        colorCol.setCellValueFactory(cellData -> cellData.getValue().colorProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().startProperty());
        eventTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("name"));
        colorCol.setSortable(false);
        setupColorCell();
        setupDateColumnFormat();
        setupSelectionModelListener();

    }

    private void setupColorCell() {
        colorCol.setCellFactory(c -> new TableCell<Event, Color>() {
            @Override
            protected void updateItem(Color item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    setStyle(getBackgroundColorFX(item));
                }
            }
        });
    }

    private void setupDateColumnFormat() {
        dateCol.setCellFactory(c -> new TableCell<Event, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty)
                    setText(null);
                else
                    setText(DateTimeFormatter.ofPattern("dd-MM-yyyy").format(item));
            }
        });
    }

    private void setupSelectionModelListener() {
        eventTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                showEventDetail(newValue));
    }

    public static String getBackgroundColorFX(Color c) {
        int r = (int) (c.getRed() * 255);
        int g = (int) (c.getGreen() * 255);
        int b = (int) (c.getBlue() * 255);
        return String.format("-fx-background-color: #%02X%02X%02X", r, g, b);
    }

    private void showEventDetail(Event event) {
        if (event != null) {
            nameLabel.setText(event.getName());
            String start = DateTimeFormatter.ofPattern("dd-MM-yy").format(event.getStart());
            String end = DateTimeFormatter.ofPattern("dd-MM-yy").format(event.getEnd());
            timeLabel.setText(String.format("%s to %s", start, end));
            tagLabel.setText(event.getTag());
            noteTextArea.setText(event.getNote());
            colorRectangle.setStyle(getBackgroundColorFX(event.getColor()));
        }
    }

    public static EventList getEventList() {
        return eventList;
    }

}
