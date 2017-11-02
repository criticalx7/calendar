package common.search;

import javafx.collections.ObservableList;
import model.Event;

public interface Searcher {

    ObservableList<Event> search(ObservableList<Event> list, String text);

}
