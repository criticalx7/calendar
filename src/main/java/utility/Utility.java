package utility;

import javafx.scene.paint.Color;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public final class Utility {
    private Utility() {
    }

    public static String getBackgroundColorFX(Color c) {
        int r = (int) (c.getRed() * 255);
        int g = (int) (c.getGreen() * 255);
        int b = (int) (c.getBlue() * 255);
        return String.format("-fx-background-color: #%02X%02X%02X;", r, g, b);
    }

    public static String getBorderColorFX(Color c) {
        int r = (int) (c.getRed() * 255);
        int g = (int) (c.getGreen() * 255);
        int b = (int) (c.getBlue() * 255);
        return String.format("-fx-border-color: #%02X%02X%02X;", r, g, b);
    }
}
