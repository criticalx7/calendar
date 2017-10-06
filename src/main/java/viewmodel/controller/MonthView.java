package viewmodel.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Event;
import model.EventList;
import view.ViewManager;
import viewmodel.component.ComponentFactory;
import viewmodel.component.DateCell;
import viewmodel.component.EventBox;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */

public class MonthView {

    //note: #817153 golden color

    @FXML
    protected Button forwardBtn, backwardBtn;
    @FXML
    protected Label monthLabel, yearLabel;
    @FXML
    protected GridPane gridView;
    @FXML
    protected TextField searchBar;

    private DateCell[][] gridCells;
    private LocalDate currentMonth;
    private EventList eventList;
    private ViewManager viewManager;

    @FXML
    public void initialize() {
        currentMonth = LocalDate.now();
        initGridCells();
    }


    /**
     * method to be called internally to initialize all the
     * date cells and add them to GridPane
     */
    private void initGridCells() {
        int row = gridView.getRowConstraints().size();
        int col = gridView.getColumnConstraints().size();
        gridCells = new DateCell[row][col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++) {
                DateCell cell = createDateCell();
                gridCells[i][j] = cell;
                gridView.add(gridCells[i][j], j, i);
            }
    }


    /**
     * method to be called by month change action to layout
     * new date and event.
     */
    private void updateView() {
        // set month and year text
        monthLabel.setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        yearLabel.setText(String.valueOf(currentMonth.getYear()));

        // layout detail of all cells
        LocalDate firstDateOfMonth = currentMonth.withDayOfMonth(1);
        int firstDayOfWeek = firstDateOfMonth.getDayOfWeek().getValue() % 7;
        int indexDay = 0;
        for (int i = 0; i < gridView.getRowConstraints().size(); i++) {
            for (int j = 0; j < gridView.getColumnConstraints().size(); j++) {
                DateCell cell = gridCells[i][j];
                LocalDate currentDate = firstDateOfMonth.plusDays(indexDay - firstDayOfWeek);
                cell.getChildren().clear();
                cell.setDate(currentDate);
                setCellDetail(cell);
                indexDay++;
            }
        }

    }


    private void setCellDetail(DateCell cell) {
        // if the date is outside this month range disable
        cell.setDisable(cell.getDate().getMonth() != currentMonth.getMonth());

        // style setup for date header
        cell.getDateLabel().setTextFill(cell.getDate().isEqual(LocalDate.now()) ? Color.valueOf("#817153").brighter() : Color.ALICEBLUE);
        cell.getChildren().add(cell.getDateLabel());

        // main loop to lay events
        for (Event event : eventList.getEvents().filtered(e -> !e.isCancel())) {
            if (cell.getDate().equals(event.getStart()) && cell.getChildren().size() < 4) {
                EventBox eventBox = createEventBox(event);
                cell.getChildren().add(eventBox);
            }
        }
    }

    private EventBox createEventBox(Event event) {
        EventBox box = ComponentFactory.createEventBox();
        box.textProperty().bind(event.nameProperty());
        box.colorProperty().bind(event.colorProperty());
        // make box box clickable
        box.setOnMouseClicked(action -> {
            handleEdit(event);
            action.consume();
        });
        box.getButton().setOnAction(e -> {
            boolean confirm = handleCancel(event);
            ((VBox) box.getParent()).getChildren().remove(box);
        });
        return box;
    }

    private DateCell createDateCell() {
        DateCell cell = ComponentFactory.createDateCell();
        cell.setOnMouseClicked(action -> {
            Event temp = new Event(cell.getDate());
            Boolean confirm = handleAdd(temp);
            if (confirm) cell.getChildren().add(createEventBox(temp));
        });
        return cell;
    }

    @FXML
    private void handleBackwardBtn() {
        currentMonth = currentMonth.minusMonths(1);
        updateView();
    }

    @FXML
    public void handleForwardBtn() {
        currentMonth = currentMonth.plusMonths(1);
        updateView();
    }

    private boolean handleAdd(Event event) {
        boolean confirm = viewManager.showEventProcessDialog(event);
        if (confirm) eventList.addEvent(event);
        return confirm;
    }

    private void handleEdit(Event event) {
        boolean confirm = viewManager.showEventProcessDialog(event);
        if (confirm) eventList.editEvent(event);
    }

    @FXML
    private void handleSearch() {
        String toSearch = searchBar.getText();
        for (Event event: eventList.getEvents().filtered(e -> !e.isCancel())) {
            if (Event.getDefaultDatePattern().format(event.getStart()).equals((toSearch))) {
                handleEdit(event);
                return;
            }
        }
    }
    // currently hacked
    private boolean handleCancel(Event event) {
        Boolean confirm = viewManager.showConfirmationDialog().filter(r -> r == ButtonType.OK).isPresent();
        if (confirm) eventList.cancelEvent(event);
        return confirm;
    }

    public void setEventList(EventList eventList) {
        this.eventList = eventList;
        updateView();
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }
}
