package persistence;

import javafx.collections.ObservableList;
import model.Event;

public interface EventSource {


    /**
     * The method handles setting up the source.
     * This can be leave empty if the source
     * don't require any setup
     */
    void setup();


    /**
     * The method loads all the events from a source.
     * The initialization of the source should be left
     * for a developers to design it themselves.
     */
    ObservableList<Event> load();


    /**
     * This method handle insertion of event into the source.
     *
     * @param event - the event to be inserted
     */
    void insert(final Event event);


    /**
     * This method handle deletion of event from the source.
     *
     * @param event - the event to be deleted
     */
    void delete(final Event event);


    /**
     * This method handle update of event in the source.
     *
     * @param event - the event to be updated
     */
    void update(final Event event);


    /**
     * This method handle closing all resource connection.
     * The method can be left empty in order to let the
     * GC do its job.
     */
    void close();
}
