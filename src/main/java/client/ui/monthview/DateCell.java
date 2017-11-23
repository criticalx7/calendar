package client.ui.monthview;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.time.LocalDate;

class DateCell extends VBox {
    static final int EVENT_LIMIT = 3;
    private static final String DEFAULT_STYLE = "default-cell";
    private static final String DEFAULT_LABEL_STYLE = "dateLabel";

    private LocalDate date = LocalDate.now();
    private final Label dateLabel = new Label();
    private final VBox eventCell = new VBox();


    DateCell() {
        super();
        getChildren().add(dateLabel);
        getChildren().add(eventCell);
        getStyleClass().setAll(DEFAULT_STYLE);
        dateLabel.getStyleClass().setAll(DEFAULT_LABEL_STYLE);
    }

    void add(Node node) {
        eventCell.getChildren().add(node);
    }

    void clear() {
        eventCell.getChildren().clear();
    }

    void setDate(LocalDate date) {
        this.date = date;
        dateLabel.setText(String.valueOf(date.getDayOfMonth()));
        dateLabel.setTextFill(date.isEqual(LocalDate.now()) ? Color.valueOf("#817153").brighter() : Color.ALICEBLUE);
    }

    int size() {
        return eventCell.getChildren().size();
    }

    LocalDate getDate() {
        return date;
    }

    Label getDateLabel() {
        return dateLabel;
    }


}
