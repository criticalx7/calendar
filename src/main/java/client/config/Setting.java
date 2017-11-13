package client.config;

import java.time.format.DateTimeFormatter;

// TODO - property based initialization

/*
 * @author Chatchapol Rasameluangon
 * id 5810404901
 *
 */


/**
 * Mock class for simple setting
 */
public class Setting {
    private static DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static String defaultColor = "#814567";

    private Setting() {
        // to be implement
    }

    public static DateTimeFormatter getDatePattern() {
        return datePattern;
    }

    public static void setDatePattern(DateTimeFormatter datePattern) {
        Setting.datePattern = datePattern;
    }

    public static String getDefaultColor() {
        return defaultColor;
    }

    public static void setDefaultColor(String defaultColor) {
        Setting.defaultColor = defaultColor;
    }
}
