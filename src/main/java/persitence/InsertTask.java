package persitence;

import javafx.concurrent.Task;
import model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */
class InsertTask extends Task {
    private final Event event;

    /**
     * @param event An event to be inserted.
     */
    InsertTask(Event event) {
        super();
        this.event = event;
    }

    @Override
    protected Void call() throws Exception {
        try (Connection con = DBManager.getConnection()) {
            System.out.println("Insert task initialize...");
            insert(con);
        }

        return null;
    }

    /**
     * Call internally to make a insert statement of new event's record.
     *
     * @param con A database connection
     * @throws SQLException Corruption occurs.
     */
    private void insert(Connection con) throws SQLException {
        String sql = "INSERT INTO Events (name, note, tag, startDate, endDate, color) VALUES(?,?,?,?,?,?)";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setString(1, event.getName());
        statement.setString(2, event.getNote());
        statement.setString(3, event.getTag());
        statement.setString(4, model.Event.getDefaultDatePattern().format(event.getStart()));
        statement.setString(5, model.Event.getDefaultDatePattern().format(event.getEnd()));
        statement.setString(6, event.getColor().toString());
        statement.executeUpdate();
        statement.close();
    }


}
