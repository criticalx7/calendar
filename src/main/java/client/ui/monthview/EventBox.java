package client.ui.monthview;

import client.ui.EventAdapter;
import client.utility.ColorUtil;
import common.model.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

class EventBox extends HBox {
    private static final String DEFAULT_LABEL_STYLE = "event-label";
    private static final String CLOSE_BUTTON_STYLE = "event-closeBtn";
    private static final Insets INSETS = new Insets(0, 0, 0, 5);

    private final EventAdapter eventModel;
    private final Button button = new Button("x");
    private final Label nameLabel = new Label();
    private final Circle circle = new Circle(3);

    EventBox(Event event) {
        super();
        eventModel = new EventAdapter(event);
        setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);
        Pane rigidArea = new Pane();
        setHgrow(rigidArea, Priority.ALWAYS);

        circle.fillProperty().bind(eventModel.colorProperty());
        nameLabel.textProperty().bind(eventModel.nameProperty());

        // set hover color effect
        setOnMouseEntered(action -> setStyle(ColorUtil.getBackgroundColorFX(eventModel.colorProperty().get())));
        setOnMouseExited(action -> setStyle(null));
        button.visibleProperty().bind(hoverProperty());


        nameLabel.getStyleClass().setAll(DEFAULT_LABEL_STYLE);
        button.getStyleClass().setAll(CLOSE_BUTTON_STYLE);

        getChildren().addAll(circle, nameLabel, rigidArea, button);
    }


    Button getButton() {
        return button;
    }


}
