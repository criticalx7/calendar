package client.ui.monthview;

import client.config.Setting;
import client.controls.EventAdapter;
import common.model.Event;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

public class MonthView {
    //note: #817153 golden color

    @FXML
    protected Button backwardBtn, forwardBtn, refreshBtn;
    @FXML
    private Label monthLabel, yearLabel;
    @FXML
    public GridPane dayOfWeekGrid;
    @FXML
    private GridPane gridView;
    @FXML
    private TextField searchBar;
    @FXML
    private Button moreButton;

    private DateCell[][] gridCells;
    private MonthViewModel viewModel;
    private ContextMenu menu;

    public void initialize() {
        createMore();
        initGridCells();
    }

    private void createMore() {
        ContextMenu more = new ContextMenu();
        MenuItem search = new MenuItem("Search");
        MenuItem setting = new MenuItem("Setting");
        MenuItem about = new MenuItem("About");
        more.getItems().setAll(search, setting, about);
        more.setAutoHide(true);
        menu = more;
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
     * Refresh the whole table of events.
     */
    public void updateView() {
        int row = gridView.getRowCount();
        int col = gridView.getColumnCount();
        // set month and year text
        monthLabel.setText(viewModel.getCurrentDate().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        yearLabel.setText(String.valueOf(viewModel.getCurrentDate().getYear()));

        // get period to display
        LocalDate firstDay = viewModel.getCurrentDate().withDayOfMonth(1);
        int firstDOW = firstDay.getDayOfWeek().getValue() % 7;
        LocalDate from = firstDay.minusDays(1 + firstDOW);
        LocalDate to = firstDay.plusDays((row * col) - firstDOW);
        ObservableList<EventAdapter> events = viewModel.query(from, to);

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


    private void setCellDetail(DateCell cell, LocalDate currentDate, ObservableList<EventAdapter> events) {
        cell.clear();
        cell.setDate(currentDate);
        cell.setDisable(cell.getDate().getMonth().getValue() != viewModel.getCurrentDate().getMonth().getValue());

        // main loop to lay events
        for (EventAdapter event : events.filtered(e -> cell.getDate().equals(e.startProperty().get()))) {
            if (cell.size() <= DateCell.EVENT_LIMIT) {
                cell.add(createEventBox(event, cell));
            }
        }
    }

    private DateCell createDateCell() {
        DateCell cell = new DateCell();
        cell.setOnMouseClicked(action -> {
            if (action.getClickCount() == 2 && action.getButton() == MouseButton.PRIMARY) {
                Event temp = new Event(cell.getDate());
                viewModel.add(cell.getDate());
            }
        });

        return cell;
    }


    private Node createEventBox(EventAdapter event, DateCell cell) {
        if (cell.size() < DateCell.EVENT_LIMIT) {
            EventBox box = new EventBox(event);
            box.setOnMouseClicked(action -> viewModel.edit(event));
            box.getButton().setOnAction(action -> viewModel.delete(event));
            return box;
        } else if (cell.size() == DateCell.EVENT_LIMIT) {
            Hyperlink more = new Hyperlink("more...");
            more.setFocusTraversable(false);
            more.setOnMouseClicked(click -> {
                viewModel.search(Setting.getDatePattern().format(cell.getDate()));
                more.setVisited(false);
            });
            return more;
        }
        return null;
    }

    @FXML
    private void onBackward() {
        viewModel.prevMonth();
    }

    @FXML
    public void onForward() {
        viewModel.nextMonth();
    }

    @FXML
    private void onSearch() {
        viewModel.search(searchBar.getText());
    }

    @FXML
    private void onMore() {
        menu.show(moreButton, Side.BOTTOM, 0, 0);
    }

    @FXML
    private void onRefresh() {
        viewModel.refresh();
    }

    public void setViewModel(MonthViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.currentDateProperty().addListener((obs, oldDate, newDate) -> updateView());
    }
}
