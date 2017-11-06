package view;

import controller.MainController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import model.Event;

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
    private Label monthLabel;
    @FXML
    private Label yearLabel;
    @FXML
    public GridPane dayOfWeekGrid;
    @FXML
    private GridPane gridView;
    @FXML
    private TextField searchBar;
    @FXML
    private Button moreButton;

    private DateCell[][] gridCells;
    private LocalDate currentMonth;
    private MainController controller;

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
        int row = gridView.getRowCount();
        int col = gridView.getColumnCount();
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
        int row = gridView.getRowCount();
        int col = gridView.getColumnCount();
        // set month and year text
        monthLabel.setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        yearLabel.setText(String.valueOf(currentMonth.getYear()));

        // get period to display
        LocalDate firstDay = currentMonth.withDayOfMonth(1);
        int firstDOW = firstDay.getDayOfWeek().getValue() % 7;
        LocalDate from = firstDay.minusDays(1 + firstDOW);
        LocalDate to = firstDay.plusDays((row * col) - firstDOW);
        ObservableList<Event> events = controller.getEvents(from, to);

        // layout detail
        int indexDay = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                DateCell cell = gridCells[i][j];
                LocalDate currentDate = firstDay.plusDays(indexDay - firstDOW);
                setCellDetail(cell, currentDate, events);
                indexDay++;
            }
        }
    }


    private void setCellDetail(DateCell cell, LocalDate currentDate, ObservableList<Event> events) {
        cell.clear();
        cell.setDate(currentDate);
        cell.setDisable(cell.getDate().getMonth().getValue() != currentMonth.getMonth().getValue());

        // main loop to lay events
        for (Event event : events.filtered(e -> cell.getDate().equals(e.getStart()))) {
            addToCell(event, cell);
        }
    }

    private DateCell createDateCell() {
        DateCell cell = new DateCell();
        cell.setOnMouseClicked(action -> {
            if (action.getClickCount() == 2 && action.getButton() == MouseButton.PRIMARY) {
                Event temp = new Event(cell.getDate());
                if (controller.handleAdd(temp)) addToCell(temp, cell);
            }
        });

        return cell;
    }


    private Node createEventBox(Event event, DateCell cell) {
        if (cell.size() < DateCell.EVENT_LIMIT) {
            EventBox box = new EventBox(event);
            box.setOnMouseClicked(action -> controller.handleEdit(event));
            box.getButton().setOnAction(action -> handleDelete(event));
            return box;
        } else if (cell.size() == DateCell.EVENT_LIMIT) {
            Hyperlink more = new Hyperlink("more...");
            more.setFocusTraversable(false);
            more.setOnMouseClicked(click -> {
                controller.handleSearch(Event.getDefaultDatePattern().format(cell.getDate()));
                more.setVisited(false);
            });
            return more;
        }
        return null;
    }


    private void addToCell(Event event, DateCell cell) {
        if (cell.size() <= DateCell.EVENT_LIMIT) {
            cell.add(createEventBox(event, cell));
        }
    }

    private void handleDelete(Event event) {
        controller.handleRemove(event);
        updateView();
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

    @FXML
    private void handleSearch() {
        controller.handleSearch(searchBar.getText());
    }

    // this is some place holder for more functions
    @FXML
    private void handleMore() {
        ContextMenu more = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Search");
        more.getItems().setAll(menuItem1);
        more.setAutoHide(true);
        more.show(moreButton, Side.BOTTOM, 0, 0);
    }

    public void setController(MainController controller) {
        this.controller = controller;
        updateView();
    }
}
