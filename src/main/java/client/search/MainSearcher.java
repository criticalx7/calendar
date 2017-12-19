package client.search;

/*
 * @author Chatchapol Rasameluangon
 * id 5810404901
 */

import client.config.Setting;
import client.controls.EventAdapter;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;


/**
 *  searcher for searching by date and name.
 */
@Service
public class MainSearcher implements Searcher<EventAdapter> {
    @Override
    public ObservableList<EventAdapter> search(ObservableList<EventAdapter> source, String text) {
        ObservableList<EventAdapter> result;
        if (isParsable(text)) {
            LocalDate target = LocalDate.parse(text, Setting.getDatePattern());
            result = source
                    .filtered(event -> event.startProperty().get().equals(target));
        } else {
            result = source
                    .filtered(event -> event.nameProperty().get().compareToIgnoreCase(text) == 0);
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
