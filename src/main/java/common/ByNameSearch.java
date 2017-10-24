package common;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Event;

public class ByNameSearch implements Searcher{
    @Override
    public ObservableList<Event> search(ObservableList<Event> list, String text) {
        ObservableList<Event> result = FXCollections.observableArrayList();
        result.setAll(list
                .filtered(e -> !e.isCancel())
                .filtered(e -> e.getName().toLowerCase().equals(text.toLowerCase())));
        return result;
    }
}
