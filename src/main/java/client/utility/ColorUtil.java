package client.utility;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

import javafx.scene.Parent;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public final class ColorUtil {
    private ColorUtil() {
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

    public static void setShadowOverlay(Parent background, Stage foreground) {
        ColorAdjust effect = new ColorAdjust();
        effect.setBrightness(-0.5);
        background.setEffect(effect);
        foreground.setOnHidden(action -> background.setEffect(null));
    }
}
