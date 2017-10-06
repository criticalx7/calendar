package viewmodel.component;

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
        setAlignment(Pos.CENTER_LEFT);
        Pane rigidArea = new Pane();
        setHgrow(rigidArea, Priority.ALWAYS);

        // add style and color
        setOnMouseEntered(action -> setStyle(Utility.getBackgroundColorFX(getColor().saturate())));
        setOnMouseExited(action -> setStyle(Utility.getBackgroundColorFX(getColor())));
        color.addListener((obv, oldColor, newColor) -> setStyle(Utility.getBackgroundColorFX(newColor)));
        nameLabel.getStyleClass().setAll("event-label");
        button.getStyleClass().setAll("event-closeBtn");
        getChildren().addAll(nameLabel, rigidArea, button);
    }

    public StringProperty textProperty() {
        return nameLabel.textProperty();
    }

    public Button getButton() {
        return button;
    }

    public void setText(String text) {
        nameLabel.setText(text);
    }

    public Color getColor() {
        return color.get();
    }

    public SimpleObjectProperty<Color> colorProperty() {
        return color;
    }

    public void setColor(Color color) {
        this.color.set(color);
    }
}
