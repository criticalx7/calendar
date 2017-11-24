package server.persistence;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

import common.model.Event;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Abstract all commons SQL or NoSQL DAO operations.
 */
public abstract class BaseEventsDAO implements SimpleDAO<Event> {
    private static final Logger logger = LogManager.getLogger(BaseEventsDAO.class);
    private static final DateTimeFormatter defaultDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String defaultColor = Color.AQUA.toString();

    public static DateTimeFormatter getDefaultDateFormat() {
        return defaultDateFormat;
    }

    public static String getDefaultColor() {
        return defaultColor;
    }

    protected void createDirectory() {
        File eventFolder = new File(System.getProperty("user.home"), "CalendarDB");
        if (!eventFolder.exists()) {
            logger.info("Events directory not found, creating...");
            boolean success = eventFolder.mkdirs();
            // if not success, just close the program.
            if (!success) {
                logger.error("Create directory failed, terminate...");
                System.exit(0);
            }
        }
    }

    protected void insertDummy() {
        Event checkme = new Event();
        checkme.setName("checkMe");
        checkme.setNote("This is example events");
        checkme.setStart(LocalDate.now());
        checkme.setEnd(LocalDate.now());
        checkme.setColor(defaultColor);
        checkme.setRecurred(false);
        checkme.setYearly(false);
        checkme.setMonthly(false);
        insert(checkme);
    }

    @Override
    public void setup() throws Exception {
        // Subclass that is requiring setup operation should override this.
    }


    @Override
    public void close() {
        // Subclass that is requiring pre exit operation should override this.
    }
}
