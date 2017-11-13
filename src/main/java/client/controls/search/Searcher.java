package client.controls.search;

import common.model.Event;
import javafx.collections.ObservableList;

/**
 * Simple searcher interface
 */
public interface Searcher {

    /**
     * @param source Events's source
     * @param target Text target.
     * @return Filtered list resulted by search
     */
    ObservableList<Event> search(ObservableList<Event> source, String target);

}
