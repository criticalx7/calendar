package model;

public interface EventSource {

    /**
     * The method loads all the events from a source.
     * The initialization of the source should be left
     * for a developers to design it themselves.
     */
    void load();


    /**
     * This method handle insertion of event into the source.
     *
     * @param event - the event to be inserted
     */
    void insert(Event event);


    /**
     * This method handle deletion of event from the source.
     *
     * @param event - the event to be deleted
     */
    void delete(Event event);


    /**
     * This method handle update of event in the source.
     *
     * @param event - the event to be updated
     */
    void update(Event event);
}
