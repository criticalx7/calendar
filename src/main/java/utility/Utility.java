package utility;

import javafx.scene.paint.Color;

public class Utility {
    private static Utility ourInstance = new Utility();

    public static Utility getInstance() {
        return ourInstance;
    }

    private Utility() {
    }

    public String getBackgroundColorFX(Color c) {
        int r = (int) (c.getRed() * 255);
        int g = (int) (c.getGreen() * 255);
        int b = (int) (c.getBlue() * 255);
        return String.format("-fx-background-color: #%02X%02X%02X", r, g, b);
    }
}
