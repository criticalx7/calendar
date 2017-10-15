package persistence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import model.Event;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;


/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */
class LoadTask extends Task<ObservableList<Event>> {

    @Override
    protected ObservableList<Event> call() throws Exception {
        try (Connection con = DBManager.getConnection()) {
            System.out.println("Load task initialize...");
            return load(con);
        }
    }

    /**
     * Issue a statement for obtaining a result set and then process the set.
     * This also set the auto increment key from sqlite_sequence table.
     *
     * @param con A database connection
     * @return List of events, ready to be set.
     * @throws SQLException Corruption occurs.
     */
    private ObservableList<Event> load(Connection con) throws SQLException {
        ObservableList<Event> events;
        String sql = "SELECT * FROM Events";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        events = processEvents(resultSet);
        statement.close();

        sql = "SELECT * FROM sqlite_sequence";
        statement = con.createStatement();
        resultSet = statement.executeQuery(sql);
        resultSet.next();
        DBManager.getPrimaryKey().set(resultSet.getInt("seq"));
        statement.close();
        return events;
    }


    /**
     * Call internally to process the result set, turn it into a list of events.
     *
     * @param set A set of records from a query.
     * @return List of events, ready to be set.
     * @throws SQLException Corruption occurs.
     */
    private ObservableList<Event> processEvents(ResultSet set) throws SQLException {
        ObservableList<Event> events = FXCollections.observableArrayList();

        while (set.next()) {
            Event event = new Event();
            event.setId(set.getInt("id"));
            event.setName(set.getString("name"));
            event.setNote(set.getString("note"));
            event.setTag(set.getString("tag"));
            event.setStart(LocalDate.parse(set.getString("startDate"), Event.getDefaultDatePattern()));
            event.setEnd(LocalDate.parse(set.getString("endDate"), Event.getDefaultDatePattern()));
            if (set.getString("color") == null) {
                event.setColor(Color.valueOf(Event.getDefaultColor()));
            } else {
                event.setColor(Color.valueOf(set.getString("color")));
            }
            events.add(event);
        }
        return events;
    }
}
