package server.persistence.sql;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

import common.model.Event;
import javafx.concurrent.Task;
import server.persistence.BaseEventsDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * A task handles insert event's record.
 */
public class InsertTask extends Task {
    private final Event event;
    private final Connection connection;

    InsertTask(Event event, Connection con) {
        this.event = event;
        this.connection = con;
    }

    @Override
    protected Void call() throws Exception {
        try (Connection con = connection) {
            insert(con);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    void insert(Connection con) throws SQLException {
        String sql = "INSERT INTO Events (name, note, startDate, endDate, color, recurred, yearly, monthly) " +
                "VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setString(1, event.getName());
        statement.setString(2, event.getNote());
        statement.setString(3, event.getStart().format(BaseEventsDAO.getDefaultDateFormat()));
        statement.setString(4, event.getEnd().format(BaseEventsDAO.getDefaultDateFormat()));
        statement.setString(5, event.getColor());
        statement.setInt(6, event.isRecurred() ? 1 : 0);
        statement.setInt(7, event.isYearly() ? 1 : 0);
        statement.setInt(8, event.isMonthly() ? 1 : 0);
        statement.executeUpdate();
        statement.close();
    }
}
