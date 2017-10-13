import javafx.application.Application;
import javafx.stage.Stage;
import model.EventList;
import persitence.DBManager;
import persitence.EventSource;
import view.ViewManager;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class Main extends Application {
    private EventSource defaultSource;
    private EventList model;

    @Override
    public void init() throws Exception {
        model = new EventList();
        defaultSource = new DBManager(model);
        model.setEventSource(defaultSource);
        defaultSource.setup();
        // current view model doesn't support auto invalidation so we need to block the thread.
        ((DBManager) defaultSource).getTaskFuture().get();
        model.loadEvent();
        // current view model doesn't support auto invalidation so we need to block the thread.
        ((DBManager) defaultSource).getTaskFuture().get();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewManager view = new ViewManager(primaryStage);
        view.setupSceneGraph(model);
        view.start();
    }

    @Override
    public void stop() {
        defaultSource.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
