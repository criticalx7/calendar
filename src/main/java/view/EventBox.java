package view;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import utility.Utility;

public class EventBox extends HBox {
    private final SimpleObjectProperty<Color> color = new SimpleObjectProperty<>(this, "color");
    private final Label nameLabel = new Label();
    private final Button button = new Button("x");

    EventBox() {
        super();
        // set the rigidArea between label and button
        button.setVisible(false);
        setAlignment(Pos.CENTER_LEFT);
        Pane rigidArea = new Pane();
        setHgrow(rigidArea, Priority.ALWAYS);

        // add style and color
        setOnMouseEntered(action -> {
            button.setVisible(true);
            setStyle(Utility.getBackgroundColorFX(getColor().saturate()));
        });
        setOnMouseExited(action -> {
            button.setVisible(false);
            setStyle(Utility.getBackgroundColorFX(getColor()));
        });
        color.addListener((obv, oldColor, newColor) -> setStyle(Utility.getBackgroundColorFX(newColor)));
        nameLabel.getStyleClass().setAll("event-label");
        button.getStyleClass().setAll("event-closeBtn");
        getChildren().addAll(nameLabel, rigidArea, button);
    }


    public Button getButton() {
        return button;
    }

    private Color getColor() {
        return color.get();
    }

  
    public void setText(String text) {
        nameLabel.setText(text);
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    StringProperty textProperty() {
        return nameLabel.textProperty();
    }

    SimpleObjectProperty<Color> colorProperty() {
        return color;
    }

}
