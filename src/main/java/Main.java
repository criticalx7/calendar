import controller.EventManager;
import controller.MainController;
import controller.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import persistence.CalendarDAO;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */

public class Main extends Application {
    private CalendarDAO dao;
    private ApplicationContext context;


    @Override
    public void init() throws Exception {
        // Main source initialized, attempt to create event.
        context = new ClassPathXmlApplicationContext("main-context.xml");
        dao = context.getBean("calendarDAOImpl", CalendarDAO.class);
        dao.setup();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        EventManager em = context.getBean("eventManager", EventManager.class);
        ViewManager vm = new ViewManager(primaryStage);
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
