package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import model.Event;
import utility.Utility;

class EventBox extends HBox {
    private final Button button = new Button("x");
    private static final Insets INSETS = new Insets(0, 0, 0, 5);

    EventBox(Event event) {
        super();
        //setPadding(INSETS);
        setAlignment(Pos.CENTER_LEFT);

        // add event's color dot
        Circle circle = new Circle(3);
        circle.fillProperty().bind(event.colorProperty());

        // add event's name
        Label nameLabel = new Label();
        nameLabel.textProperty().bind(event.nameProperty());

        // set the rigidArea between label and button
        Pane rigidArea = new Pane();
        setHgrow(rigidArea, Priority.ALWAYS);

        // set hover color effect
        setOnMouseEntered(action -> setStyle(Utility.getBackgroundColorFX(event.getColor())));
        setOnMouseExited(action -> setStyle(null));
        button.visibleProperty().bind(hoverProperty());


        nameLabel.getStyleClass().setAll("event-label");
        button.getStyleClass().setAll("event-closeBtn");

        getChildren().addAll(circle, nameLabel, rigidArea, button);
    }


    Button getButton() {
        return button;
    }


}
