package view;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

class DateCell extends VBox {
    private LocalDate date;
    private final Label dateLabel = new Label();

    DateCell() {
        super();
        getStyleClass().setAll("default-cell");
        dateLabel.getStyleClass().setAll("dateLabel");
    }

    void setDate(LocalDate date) {
        this.date = date;
        dateLabel.setText(String.valueOf(date.getDayOfMonth()));
    }

    LocalDate getDate() {
        return date;
    }

    Label getDateLabel() {
        return dateLabel;
    }


}
