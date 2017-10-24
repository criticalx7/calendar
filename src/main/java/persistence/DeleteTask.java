package persistence;

import javafx.concurrent.Task;
import model.Event;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */
class DeleteTask extends Task {
    private final Event event;

    /**
     * @param event An event to be deleted.
     */
    DeleteTask(Event event) {
        super();
        this.event = event;
    }

    @Override
    protected Void call() throws Exception {
        try (Connection con = DatabaseManager.getConnection()) {
            System.out.println("Delete task initialize...");
            delete(con);
        }

        return null;
    }

    /**
     * Call internally to make a delete statement of selected event's record.
     *
     * @param con A database connection
     * @throws SQLException Corruption occurs.
     */
    private void delete(Connection con) throws SQLException {
        int id = event.getId();
        String sql = String.format("DELETE FROM Events WHERE id = %d", id);
        Statement statement = con.createStatement();
        statement.executeUpdate(sql);
        statement.close();
    }
}