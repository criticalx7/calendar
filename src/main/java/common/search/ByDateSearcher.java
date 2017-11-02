package common.search;

import javafx.collections.ObservableList;
import model.Event;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ByDateSearcher implements Searcher {

    @Override
    public ObservableList<Event> search(ObservableList<Event> list, String text) {
        ObservableList<Event> result = null;
        LocalDate target;
        if (isParsable(text)) {
            target = LocalDate.parse(text, Event.getDefaultDatePattern());
            result = list
                    .filtered(event -> !event.isCancel())
                    .filtered(event -> event.getStart().equals(target));
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
