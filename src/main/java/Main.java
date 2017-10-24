import controller.EventManager;
import controller.MainController;
import controller.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.sqlite.SQLiteDataSource;
import persistence.DatabaseManager;

import javax.sql.DataSource;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class Main extends Application {
    private DatabaseManager dao;


    @Override
    public void init() throws Exception {
        // Main source initialized, attempt to create event.
        DataSource dataSource = new SQLiteDataSource();
        String url = String.format("jdbc:sqlite:%s/CalendarDB/Events.db", System.getProperty("user.home"));
        ((SQLiteDataSource) dataSource).setUrl(url);
        dao = new DatabaseManager(dataSource);
        dao.setup();
        dao.getTaskFuture().get();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // model
        EventManager em = new EventManager();
        em.setEventSource(dao);
        em.loadEvent();
        // view
        ViewManager vm = new ViewManager(primaryStage);
        // main control
        MainController mc = new MainController(em, vm);
        mc.start();
    }

    @Override
    public void stop() {
        dao.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
