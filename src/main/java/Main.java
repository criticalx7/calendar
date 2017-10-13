import javafx.application.Application;
import javafx.stage.Stage;
import model.DBManager;
import model.EventList;
import model.EventSource;
import view.ViewManager;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        EventList eventList = new EventList();
        EventSource defaultSource = new DBManager(eventList, null);
        eventList.setEventSource(defaultSource);
        eventList.loadEvent();

        ViewManager view = new ViewManager(primaryStage);
        view.setupSceneGraph(eventList);
        view.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
