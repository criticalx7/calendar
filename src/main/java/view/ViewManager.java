package view;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Event;
import model.EventManager;
import viewmodel.controller.EventEditor;
import viewmodel.controller.MonthView;
import viewmodel.controller.SearchView;

import java.io.IOException;
import java.util.Optional;

public class ViewManager {
    private final Stage primaryStage;

    public ViewManager(Stage stage) {
        this.primaryStage = stage;
    }

    public void setupSceneGraph(EventManager eventManager) throws IOException {
        // loadEvent main page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MonthView.fxml"));
        Parent root = loader.load();

        //setup controller
        MonthView mc = loader.getController();
        mc.setEventManager(eventManager);
        mc.setViewManager(this);

        // setup the primary stage
        Scene scene = new Scene(root, 700, 600);
        primaryStage.setTitle("Calendar");
        primaryStage.setScene(scene);
    }

    public void start() {
        primaryStage.show();
    }

    public boolean showEventEditor(Event event) {
        try {
            // Load the .fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/EventEditor.fxml"));
            Parent page = loader.load();

            // create DIALOG
            Stage stage = new Stage();
            stage.setTitle("Event");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(page));

            // set Event & Stage (Very crucial)
            EventEditor controller = loader.getController();
            controller.setCurrentEvent(event);
            controller.prepareHeader();
            stage.showAndWait();

            return controller.isConfirm();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showSearchDialog(EventManager manager, ObservableList<Event> results) {
        try {
            // Load the .fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/SearchView.fxml"));
            GridPane page = loader.load();
            page.setEffect(new DropShadow(5, Color.BLACK));

            // create DIALOG
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.NONE);
            stage.setResizable(false);

            // set Close action
            stage.focusedProperty().addListener((obs, wasFocus, nowFocus) -> {
                if (!nowFocus) stage.hide();
            });
            page.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ESCAPE) stage.hide();
            });
            stage.setScene(new Scene(page));

            // Controller
            SearchView controller = loader.getController();
            controller.setResults(results);
            controller.setEventManager(manager);
            controller.setViewManager(this);
            controller.prepareList();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
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
