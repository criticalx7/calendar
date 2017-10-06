package utility;

import javafx.scene.paint.Color;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class UtilityTest {

    @Test
    public void getBackgroundColorFXTest() {
        Color black = Color.valueOf("#000000");
        Color white = Color.WHITE;
        assertEquals("-fx-background-color: #000000;", Utility.getBackgroundColorFX(black));
        assertEquals("-fx-background-color: #FFFFFF;", Utility.getBackgroundColorFX(white));
    }

}