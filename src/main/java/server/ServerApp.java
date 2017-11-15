package server;

import common.model.Event;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import server.persistence.CalendarDAO;
import server.persistence.CalendarDAOImpl;

import java.util.logging.Logger;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

/**
 * A simple spring server
 */
public class ServerApp extends Application {

    private static final Logger logger = Logger.getLogger(ServerApp.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("server-context.xml");
        CalendarDAO<Event> database = context.getBean("calendarDAOImpl", CalendarDAOImpl.class);
        database.setup();
        Runtime.getRuntime().addShutdownHook(new Thread(database::close));

        logger.info("Waiting for requests\n");

        // add shutdown hook for destroy DAO
    }
}
