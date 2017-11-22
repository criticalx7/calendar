package server.persistence.sql;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

import common.model.Event;
import javafx.concurrent.Task;
import server.persistence.BaseEventsDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A task handles query all events record and turn it into object.
 */
public class LoadTask extends Task<List<Event>> {
    private final Connection connection;

    LoadTask(Connection con) {
        this.connection = con;
    }

    @Override
    protected List<Event> call() throws Exception {
        try (Connection con = connection) {
            return load(con);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    private List<Event> load(Connection con) throws SQLException {
        String sql = "SELECT * FROM Events";
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        return processEvents(resultSet);
    }

    private ArrayList<Event> processEvents(ResultSet set) throws SQLException {
        ArrayList<Event> events = new ArrayList<>();
        while (set.next()) {
            Event event = new Event();
            event.setId(set.getInt("id"));
            event.setName(set.getString("name"));
            event.setNote(set.getString("note"));
            event.setStart(LocalDate.parse(set.getString("startDate"), BaseEventsDAO.getDefaultDateFormat()));
            event.setEnd(LocalDate.parse(set.getString("endDate"), BaseEventsDAO.getDefaultDateFormat()));

            String color = set.getString("color");
            event.setColor(color == null ? BaseEventsDAO.getDefaultColor() : color);

            event.setRecurred(set.getInt("recurred") == 1);
            event.setYearly(set.getInt("yearly") == 1);
            event.setMonthly(set.getInt("monthly") == 1);
            events.add(event);
        }
        return events;
    }
}
