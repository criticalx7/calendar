package persistence;

import javafx.concurrent.Task;
import model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */
class UpdateTask extends Task {
    private final Event event;

    /**
     * @param event An event to be updated.
     */
    UpdateTask(Event event) {
        super();
        this.event = event;
    }

    @Override
    protected Void call() throws Exception {
        try (Connection con = DBManager.getConnection()) {
            System.out.println("Update task initialize...");
            update(con);
        }

        return null;
    }

    /**
     * Call internally to make an update statement of selected event's record.
     *
     * @param con A database connection
     * @throws SQLException Corruption occurs.
     */
    private void update(Connection con) throws SQLException {
        String sql = "UPDATE Events " +
                "SET name = ?, note = ?, tag = ?, startDate = ?, endDate = ?, color = ? " +
                "WHERE id = ?";

        PreparedStatement statement = con.prepareStatement(sql);
        statement.setString(1, event.getName());
        statement.setString(2, event.getNote());
        statement.setString(3, event.getTag());
        statement.setString(4, Event.getDefaultDatePattern().format(event.getStart()));
        statement.setString(5, Event.getDefaultDatePattern().format(event.getEnd()));
        statement.setString(6, event.getColor().toString());
        statement.setInt(7, event.getId());
        statement.executeUpdate();
    }
}