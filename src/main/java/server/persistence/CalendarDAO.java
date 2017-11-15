package server.persistence;

import java.util.List;

/*
  @author Chatchapol Rasameluangon
 * id 5810404901
 */

/**
 * Simple data source interface
 *
 * @param <T> - A data's class
 */
public interface CalendarDAO<T> {

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
    List<T> load();


    /**
     * This method handle insertion of event into the source.
     *
     * @param object - the event to be inserted
     */
    void insert(final T object);


    /**
     * This method handle deletion of event from the source.
     *
     * @param object - the event to be deleted
     */
    void delete(final T object);


    /**
     * This method handle update of event in the source.
     *
     * @param object - the event to be updated
     */
    void update(final T object);


    /**
     * This method handle closing all resource connection.
     * The method can be left empty in order to let the
     * GC do its job.
     */
    void close();
}
