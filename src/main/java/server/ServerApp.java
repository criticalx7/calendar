package server;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

import common.model.Event;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import server.persistence.SimpleDAO;
import server.persistence.sql.EventsDAO;


public class ServerApp extends Application {

    private static Logger logger = LogManager.getLogger(ServerApp.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("server-context.xml");
        SimpleDAO<Event> database = context.getBean("eventsDAO", EventsDAO.class);
        database.setup();
        logger.info("Initialize success, waiting for requests\n");
    }
}
