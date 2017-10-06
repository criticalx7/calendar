import javafx.application.Application;
import javafx.stage.Stage;
import view.ViewManager;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ViewManager view = new ViewManager(primaryStage);
        view.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
