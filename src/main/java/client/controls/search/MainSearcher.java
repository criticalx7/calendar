package client.controls.search;

import client.config.Setting;
import common.model.Event;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
/*
 * @author Chatchapol Rasameluangon
 * id 5810404901
 */

/**
 * Main searcher for searching by date and name.
 */
@Service
public class MainSearcher implements Searcher {
    private List<Searcher> searchers = new ArrayList<>();

    @Override
    public ObservableList<Event> search(ObservableList<Event> source, String text) {
        ObservableList<Event> result;
        if (isParsable(text)) {
            LocalDate target = LocalDate.parse(text, Setting.getDatePattern());
            result = source
                    .filtered(event -> event.getStart().equals(target));
        } else {
            result = source
                    .filtered(event -> event.getName().compareToIgnoreCase(text) == 0);
        }
        return result;
    }


    private boolean isParsable(String text) {
        boolean parsable = true;
        try {
            LocalDate.parse(text, Setting.getDatePattern());
        } catch (DateTimeParseException e) {
            parsable = false;
        }
        return parsable;
    }
}
