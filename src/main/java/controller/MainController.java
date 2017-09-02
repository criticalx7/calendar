package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    protected Button removeEventButton;
    @FXML
    protected Button editEventButton;
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

    private EventList eventList;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DBManager dbManager;

    @FXML
    public void initialize() {
        eventList = new EventList();
        dbManager = new DBManager(this);
        dbManager.load();
        setupTable();
    }

    private void showEventDetail(Event event) {
        if (event != null) {
            nameLabel.setText(event.getName());
            String start = dateFormatter.format(event.getStart());
            String end = dateFormatter.format(event.getEnd());
            timeLabel.setText(String.format("%s to %s", start, end));
            tagLabel.setText(event.getTag());
            noteTextArea.setText(event.getNote());
            colorRectangle.setStyle(getBackgroundColorFX(event.getColor()));

            // Test log
            System.out.println("NOW ID: " + event.getId());
            System.out.println("KEY ID: " + Event.getPrimaryKey());
        }
    }


    private boolean showEventProcessDialog(Event event) {

        try {
            // Load the .fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ProcessEvent.fxml"));
            Parent page = loader.load();

            // create DIALOG
            Stage stage = new Stage();
            stage.setTitle("Event");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(page));

            // set Event & Stage (Very crucial)
            ProcessEventController controller = loader.getController();
            controller.setDateTimeFormatter(dateFormatter);
            controller.setCurrentEvent(event);
            controller.setDialogStage(stage);

            stage.showAndWait();

            return controller.isConfirm();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void handleAddEvent() {
        Event tempEvent = new Event();
        boolean confirm = showEventProcessDialog(tempEvent);
        if (confirm) {
            eventList.addEvent(tempEvent);
        }
        dbManager.insert(tempEvent);
    }

    @FXML
    private void handleRemoveEvent() {
        int removeIndex = eventTable.getSelectionModel().getSelectedIndex();
        int removeId = eventList.getEvents().get(removeIndex).getId();
        eventTable.getItems().remove(removeIndex);
        dbManager.delete(removeId);
        //Test log
        System.out.println("Removing: " + removeId);

        if (eventList.getEvents().size() <= 0) {
            removeEventButton.setDisable(true);
        }

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
                    setText(dateFormatter.format(item));
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

    public EventList getEventList() {
        return eventList;
    }

    public DateTimeFormatter getDateFormatter() {
        return dateFormatter;
    }
}
