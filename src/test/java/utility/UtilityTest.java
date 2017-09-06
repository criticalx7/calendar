package utility;

import javafx.scene.paint.Color;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class UtilityTest {


    @Test
    public void getInstanceTest() {
        assertNotNull(Utility.getInstance());
    }

    @Test
    public void getBackgroundColorFXTest() {
        Color black = Color.valueOf("#000000");
        Color white = Color.WHITE;
        assertEquals("-fx-background-color: #000000", Utility.getInstance().getBackgroundColorFX(black));
        assertEquals("-fx-background-color: #FFFFFF", Utility.getInstance().getBackgroundColorFX(white));
    }

}