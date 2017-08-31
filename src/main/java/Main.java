import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MasterView.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("calendar");
        Scene scene = new Scene(root, 600, 390);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
