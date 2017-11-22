package server.persistence.sql;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

import common.model.Event;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A task handles delete event's record.
 */
public class DeleteTask extends Task {
    private final Event event;
    private final Connection connection;

    DeleteTask(Event event, Connection con) {
        this.event = event;
        this.connection = con;
    }

    @Override
    protected Void call() throws Exception {
        try (Connection con = connection) {
            delete(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    void delete(Connection con) throws SQLException {
        Statement statement = con.createStatement();
        String sql = String.format("DELETE FROM Events WHERE id = %d", event.getId());
        statement.executeUpdate(sql);
    }
}
