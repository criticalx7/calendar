import controller.EventManager;
import controller.MainController;
import controller.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import persistence.DatabaseManager;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class Main extends Application {
    private DatabaseManager dao;
    ApplicationContext context;


    @Override
    public void init() throws Exception {
        // Main source initialized, attempt to create event.
        context = new ClassPathXmlApplicationContext("main-context.xml");
        dao = context.getBean("databaseManager", DatabaseManager.class);
        dao.setup();
        dao.getTaskFuture().get();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // model
        EventManager em = context.getBean("eventManager", EventManager.class);
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
        System.out.println("program exits");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
