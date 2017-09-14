package controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.time.LocalDate;

public class MonthViewController {


    @FXML private Button forwardBtn, backwardBtn;
    @FXML private Label monthLabel, yearLabel;
    @FXML private Label sunLabel, monLabel, tueLabel, wedLabel,
                        thuLabel,friLabel,satLabel;
    @FXML private GridPane gridView;

    private VBox[][] gridCells;
    private Label[][] dateLabels;
    private LocalDate date;
    private LocalDate now;
    private String currentDayStyle = "-fx-border-width: 2 2 2 2;" + "-fx-border-color:  #817153  ;";
    private String otherDayStyle = "-fx-border-width: 0 0 1 0;" + "-fx-border-color:  #101010;";
    private String disableCellStyle = "-fx-background-color: #101010;";

    @FXML
    public void initialize() {
        date = LocalDate.now();
        now = LocalDate.now();
        initGridCells(gridView.getRowConstraints().size(), gridView.getColumnConstraints().size());
        updateView(date);
    }

    private void initGridCells(int row, int column) {
        gridCells = new VBox[row][column];
        dateLabels = new Label[row][column];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < column; j++) {
                VBox nowBox = new VBox();
                Label nowLabel = new Label();
                nowBox.setStyle(otherDayStyle);
                nowLabel.setTextFill(Color.WHITE);
                gridCells[i][j] = nowBox;
                dateLabels[i][j] = nowLabel;
                gridCells[i][j].getChildren().add(dateLabels[i][j]);
                gridCells[i][j].setPadding(new Insets(10, 10, 10, 10));
                gridView.add(gridCells[i][j], j, i);
            }
    }

    public void updateView(LocalDate date) {
        monthLabel.setText(date.getMonth().toString());
        yearLabel.setText(String.valueOf(date.getYear()));
        LocalDate firstDateOfMonth = date.withDayOfMonth(1);
        int firstDayOfWeek = firstDateOfMonth.getDayOfWeek().getValue() % 7;
        int indexDay = 0;
        for (int i = 0; i < gridView.getRowConstraints().size(); i++) {
            for (int j = 0; j < gridView.getColumnConstraints().size(); j++) {
                LocalDate currentDate = firstDateOfMonth.plusDays(indexDay - firstDayOfWeek);


                if(currentDate.getMonth() != date.getMonth()) {
                    gridCells[i][j].setDisable(true);
                    gridCells[i][j].setStyle(disableCellStyle);
                } else {
                    if (currentDate.isEqual(now)) {
                        System.out.println("wow");
                        gridCells[i][j].setStyle(currentDayStyle);
                    } else {
                        gridCells[i][j].setStyle(otherDayStyle);
                    }
                    gridCells[i][j].setDisable(false);
                }
                dateLabels[i][j].setText(String.valueOf(currentDate.getDayOfMonth()));
                indexDay++;
            }
        }

    }

        @FXML
        private void onBackwardBtn() {
            updateView(date = date.minusMonths(1));
        }

        @FXML
        public void onForwardBtn() {
            updateView(date = date.plusMonths(1));
        }



}
