package client.controls.search;

import javafx.collections.ObservableList;

/**
 * Simple searcher interface
 */
public interface Searcher<T> {

    /**
     * @param source Events's source
     * @param target Text target.
     * @return Filtered list resulted by search
     */
    ObservableList<T> search(ObservableList<T> source, String target);

}
