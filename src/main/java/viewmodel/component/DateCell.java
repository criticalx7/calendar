package viewmodel.component;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class DateCell extends VBox {
    private LocalDate date;
    private final Label dateLabel = new Label();

    DateCell() {
        super();
        getStyleClass().setAll("default-cell");
        dateLabel.getStyleClass().setAll("dateLabel");
    }

    public void setDate(LocalDate date) {
        this.date = date;
        dateLabel.setText(String.valueOf(date.getDayOfMonth()));
    }

    public LocalDate getDate() {
        return date;
    }

    public Label getDateLabel() {
        return dateLabel;
    }


}
