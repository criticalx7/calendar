package server.persistence;

/*
  @author Chatchapol Rasameluangon
 * id 5810404901
 */

import java.util.List;


/**
 * Simple data source interface
 *
 * @param <T> - A data's class
 */
public interface SimpleDAO<T> {

    /**
     * Setting up the source.
     */
    void setup();


    /**
     * Loads all the object from the source.
     */
    List<T> load();


    /**
     * Delete event from the source.
     *
     * @param object - the object to be inserted
     */
    void insert(final T object);


    /**
     * Delete object from the source.
     *
     * @param object - the object to be deleted
     */
    void delete(final T object);


    /**
     * Update and save object to the source.
     *
     * @param object - the object to be updated
     */
    void update(final T object);


    /**
     * Closing all resource connections.
     */
    void close();
}
