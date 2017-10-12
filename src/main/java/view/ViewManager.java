package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DBManager;
import model.Event;
import model.EventList;
import viewmodel.controller.EventProcessor;
import viewmodel.controller.MonthView;

import java.io.IOException;
import java.util.Optional;

public class ViewManager {
    private final Stage primaryStage;

    public ViewManager(Stage stage) {
        primaryStage = stage;
    }

    public void start() throws IOException {
        // loadEvent main page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MonthView.fxml"));
        Parent root = loader.load();

        //setup model and controller
        MonthView mc = loader.getController();
        EventList eventList = new EventList();
        DBManager dbManager = new DBManager(eventList);
        eventList.setDatabaseManager(dbManager);
        eventList.loadEvent();
        mc.setEventList(eventList);
        mc.setViewManager(this);

        // setup the primary stage
        Scene scene = new Scene(root, 700, 600);
        primaryStage.setTitle("Calendar");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public boolean showEventProcessDialog(Event event) {
        try {
            // Load the .fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/EventProcessor.fxml"));
            Parent page = loader.load();

            // create DIALOG
            Stage stage = new Stage();
            stage.setTitle("Event");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(page));

            // set Event & Stage (Very crucial)
            EventProcessor controller = loader.getController();
            controller.setDateTimeFormatter(Event.getDefaultDatePattern());
            controller.setCurrentEvent(event);
            controller.setViewManager(this);
            stage.showAndWait();

            return controller.isConfirm();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<ButtonType> showConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to 'delete' this event");
        return alert.showAndWait();
    }

    public void showErrorDialog(String title, String header, String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
