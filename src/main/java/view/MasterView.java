package view;

import controller.EventManager;
import controller.ViewManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import model.Event;
import utility.Utility;

import java.time.LocalDate;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class MasterView {

    @FXML
    protected Button addEventButton;
    @FXML
    private Button removeEventButton;
    @FXML
    private Button editEventButton;
    @FXML
    private TableView<Event> eventTable;
    @FXML
    private TableColumn<Event, String> nameCol;
    @FXML
    private TableColumn<Event, LocalDate> dateCol;
    @FXML
    private TableColumn<Event, Color> colorCol;
    @FXML
    private Label nameLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label tagLabel;
    @FXML
    private TextArea noteTextArea;
    @FXML
    private AnchorPane colorRectangle;

    private EventManager eventManager;
    private ViewManager viewManager;

    private void showEventDetail(Event event) {
        if (event != null) {
            nameLabel.setText(event.getName());
            String start = Event.getDefaultDatePattern().format(event.getStart());
            String end = Event.getDefaultDatePattern().format(event.getEnd());
            timeLabel.setText(String.format("%s to %s", start, end));
            tagLabel.setText(event.getTag());
            noteTextArea.setText(event.getNote());
            colorRectangle.setStyle(Utility.getBackgroundColorFX(event.getColor()));
        }
    }

    /**
     * Handle add event button by   calling showEventEditor method
     * to process new event and add it to events model by model's addEvent method
     */
    @FXML
    private void handleAddEvent() {
        Event tempEvent = new Event();
        boolean confirm = viewManager.showEventEditor(tempEvent);
        if (confirm) {
            eventManager.addEvent(tempEvent);
            if (eventManager.getEvents().size() >= 1) {
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
        eventManager.removeEvent(removeIndex);

        if (eventManager.getEvents().size() <= 0) {
            removeEventButton.setDisable(true);
            editEventButton.setDisable(true);
        }

    }

    /**
     * Handle edit event button by calling showEventEditor method
     * to edit currently selected event and updating database in the process
     */
    @FXML
    private void handleEditEvent() {
        Event selectedEvent = eventManager.getCurrentEvent();
        if (selectedEvent != null) {
            boolean confirm = viewManager.showEventEditor(selectedEvent);
            if (confirm) {
                showEventDetail(selectedEvent);
                eventManager.editEvent(selectedEvent);
            }
        }
    }


    /**
     * This private method call after setEventManager() to properly setup
     * the TableView's appearance and functionality
     */
    private void setupTable() {
        // setup cells value of TableView's column
        eventTable.setItems(eventManager.getEvents());
        colorCol.setCellValueFactory(cellData -> cellData.getValue().colorProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().startProperty());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        // Setup Color column format so that it show color cell
        colorCol.setSortable(false);
        colorCol.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Color item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    setStyle(Utility.getBackgroundColorFX(item));
                }
            }
        });

        // Setup Date column format so that it show formatted date
        dateCol.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty)
                    setText(null);
                else
                    setText(Event.getDefaultDatePattern().format(item));
            }
        });

        // Setup TableView SelectionModel property so that it is sync with a currentEventProperty in eventManager
        eventTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            eventManager.setCurrentEvent(newValue);
            showEventDetail(eventManager.getCurrentEvent());
        });

        eventManager.currentEventProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                eventTable.getSelectionModel().clearSelection();
            } else {
                eventTable.getSelectionModel().select(newValue);
                eventTable.getSelectionModel().getSelectedIndex();
            }
        });

        colorCol.setReorderable(false);
        nameCol.setReorderable(false);
        dateCol.setReorderable(false);
    }


    /**
     * This setter method set the events model and setup the TableView
     *
     * @param eventManager - to be used events model
     */
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
        setupTable();
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }
}
