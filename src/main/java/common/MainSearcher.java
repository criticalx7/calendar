package common;

import javafx.collections.ObservableList;
import model.Event;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class MainSearcher implements Searcher {

    public ObservableList<Event> search(ObservableList<Event> list, String text) {
        ObservableList<Event> result;
        if (isParsable(text)) {
            LocalDate target = LocalDate.parse(text, Event.getDefaultDatePattern());
            result = list
                    .filtered(e -> !e.isCancel())
                    .filtered(e -> e.getStart().equals(target));
        } else {
            result = list
                    .filtered(e -> !e.isCancel())
                    .filtered(e -> e.getName().toLowerCase().equals(text.toLowerCase()));
        }
        return result;
    }

    private boolean isParsable(String text) {
        boolean parsable = true;
        try {
            LocalDate.parse(text, Event.getDefaultDatePattern());
        } catch (DateTimeParseException e) {
            parsable = false;
        }
        return parsable;
    }
}
