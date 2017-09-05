package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Event;
import model.EventList;
import utility.Utility;

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
    protected TableColumn<Event, String> nameCol;
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
    private Stage processDialog;


    private void showEventDetail(Event event) {
        if (event != null) {
            nameLabel.setText(event.getName());
            String start = dateFormatter.format(event.getStart());
            String end = dateFormatter.format(event.getEnd());
            timeLabel.setText(String.format("%s to %s", start, end));
            tagLabel.setText(event.getTag());
            noteTextArea.setText(event.getNote());
            colorRectangle.setStyle(Utility.getInstance().getBackgroundColorFX(event.getColor()));
        }
    }


    private boolean showEventProcessDialog(Event event, String buttonText) {

        try {
            // Load the .fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ProcessEvent.fxml"));
            Parent page = loader.load();

            // create DIALOG
            Stage stage = new Stage();
            stage.setTitle("Event");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(page));

            // set Event & Stage (Very crucial)
            ProcessEventController controller = loader.getController();
            controller.setDateTimeFormatter(dateFormatter);
            controller.setCurrentEvent(event);
            controller.setDialogStage(stage);
            controller.okButton.setText(buttonText);

            stage.showAndWait();

            return controller.isConfirm();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Handle add event button by calling showEventProcessDialog method
     * to process new event and add it to events model by model's addEvent method
     */
    @FXML
    private void handleAddEvent() {
        Event tempEvent = new Event();
        boolean confirm = showEventProcessDialog(tempEvent, "Add");
        if (confirm) {
            eventList.addEvent(tempEvent);
            if (eventList.getEvents().size() >= 1) {
                removeEventButton.setDisable(false);
                editEventButton.setDisable(false);
            }
        }

    }


    /**
     * Handle remove event button by calling removeEvent method from events model and
     * passing removed event index from SelectionModel to it
     */
    @FXML
    private void handleRemoveEvent() {
        int removeIndex = eventTable.getSelectionModel().getSelectedIndex();
        eventList.removeEvent(removeIndex);

        if (eventList.getEvents().size() <= 0) {
            removeEventButton.setDisable(true);
            editEventButton.setDisable(true);
        }

    }

    /**
     * Handle edit event button by calling showEventProcessDialog method
     * to edit currently selected event and updating database in the process
     */
    @FXML
    private void handleEditEvent() {
        Event selectedEvent = eventList.getCurrentEvent();
        if (selectedEvent != null) {
            boolean confirm = showEventProcessDialog(selectedEvent, "Edit");
            if (confirm) {
                showEventDetail(selectedEvent);
                eventList.editEvent(selectedEvent);
            }
        }
    }


    /**
     * This private method call after setEventList() to properly setup
     * the TableView's appearance and functionality
     */
    private void setupTable() {
        // setup cells value of TableView's column
        eventTable.setItems(eventList.getEvents());
        colorCol.setCellValueFactory(cellData -> cellData.getValue().colorProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().startProperty());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        // Setup Color column format so that it show color cell
        colorCol.setSortable(false);
        colorCol.setCellFactory(c -> new TableCell<Event, Color>() {
            @Override
            protected void updateItem(Color item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    setStyle(Utility.getInstance().getBackgroundColorFX(item));
                }
            }
        });

        // Setup Date column format so that it show formatted date
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

        // Setup TableView SelectionModel property so that it is sync with a currentEventProperty in eventList
        eventTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            eventList.setCurrentEvent(newValue);
            showEventDetail(eventList.getCurrentEvent());
        });

        eventList.currentEventProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                eventTable.getSelectionModel().clearSelection();
            } else {
                eventTable.getSelectionModel().select(newValue);
            }
        });

    }


    /**
     * This setter method set the events model and setup the TableView
     *
     * @param eventList - to be used events model
     */
    public void setEventList(EventList eventList) {
        this.eventList = eventList;
        setupTable();
    }

}
