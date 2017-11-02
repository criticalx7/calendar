package common.search;

import javafx.collections.ObservableList;
import model.Event;

public class ByNameSearcher implements Searcher {

    @Override
    public ObservableList<Event> search(ObservableList<Event> list, String text) {
        ObservableList<Event> result;
        result = list
                .filtered(e -> !e.isCancel())
                .filtered(e -> e.getName().toLowerCase().equals(text.toLowerCase()));
        return result;
    }
}
