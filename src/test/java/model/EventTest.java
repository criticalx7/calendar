package model;

import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */

public class EventTest {

    private Event event;
    private LocalDate dummyDate;
    private Color dummyColor;

    @Before
    public void before() {
        event = new Event();
        event.setName("Holiday");
        event.setNote("I love holiday");
        event.setTag("special");

        dummyDate = LocalDate.now();
        event.setStart(dummyDate);
        event.setEnd(dummyDate);

        dummyColor = Color.BLUE;
        event.setColor(dummyColor);
    }

    @Test
    public void testEventGetName() {
        assertEquals(event.getName(), "Holiday");
    }

    @Test
    public void testEventGetNote() {
        assertEquals(event.getNote(), "I love holiday");
    }

    @Test
    public void testEventGetTag() {
        assertEquals(event.getTag(), "special");
    }

    @Test
    public void testEventGetStart() {
        assertEquals(event.getStart(), dummyDate);
    }

    @Test
    public void testEventGetEnd() {
        assertEquals(event.getEnd(), dummyDate);
    }

    @Test
    public void testEventGetColor() {
        assertEquals(event.getColor(), dummyColor);
    }

    @After
    public void after() {
        event = null;
        dummyDate = null;
        dummyColor = null;
    }


}