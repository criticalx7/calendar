package server;

import common.model.Event;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import server.persistence.SimpleDAO;
import server.persistence.sql.EventsDAO;

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

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("server-context.xml");
        SimpleDAO<Event> database = context.getBean("eventsDAO", EventsDAO.class);
        database.setup();
        Runtime.getRuntime().addShutdownHook(new Thread(database::close));

        logger.info("Waiting for requests\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
