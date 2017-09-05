import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DBManager;
import model.EventList;

/**
 * Name: Mr.Chatchapol Rasameluangon
 * ID:   5810404901
 */


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // loadEvent main page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MasterView.fxml"));
        Parent root = loader.load();

        //setup model and controller
        MainController mc = loader.getController();
        EventList eventList = new EventList();
        DBManager dbManager = new DBManager(eventList);
        eventList.setDatabaseManager(dbManager);
        eventList.loadEvent();
        mc.setEventList(eventList);

        // setup the primary stage
        Scene scene = new Scene(root, 600, 390);
        primaryStage.setTitle("calendar");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
