package client.utility;

import javafx.scene.paint.Color;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class ColorUtilTest {

    @Test
    public void getBackgroundColorFX() throws Exception {
        Color black = Color.valueOf("#000000");
        Color white = Color.WHITE;
        assertEquals("-fx-background-color: #000000;", ColorUtil.getBackgroundColorFX(black));
        assertEquals("-fx-background-color: #FFFFFF;", ColorUtil.getBackgroundColorFX(white));
    }

    @Test
    public void getBorderColorFX() throws Exception {
        Color black = Color.valueOf("#000000");
        Color white = Color.WHITE;
        assertEquals("-fx-border-color: #000000;", ColorUtil.getBorderColorFX(black));
        assertEquals("-fx-border-color: #FFFFFF;", ColorUtil.getBorderColorFX(white));
    }

}