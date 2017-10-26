package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Event;
import view.EditorView;
import view.MonthView;
import view.SearchView;

import java.io.IOException;
import java.util.Optional;

public class ViewManager {
    private final Stage primaryStage;
    private Parent root;

    public ViewManager(Stage stage) {
        this.primaryStage = stage;
    }

    void setupStageControl(MainController controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MonthView.fxml"));
            root = loader.load();

            //setup controller
            MonthView mc = loader.getController();
            mc.setController(controller);

            // setup the primary stage
            Scene scene = new Scene(root);
            primaryStage.setTitle("Calendar");
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void show() {
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
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(page));

            // Color Adjust
            ColorAdjust effect = new ColorAdjust();
            effect.setBrightness(-0.5);
            root.setEffect(effect);
            stage.setOnHidden(action -> root.setEffect(null));

            // set Event & Stage (Very crucial)
            EditorView controller = loader.getController();
            controller.setCurrentEvent(event);
            controller.prepareHeader();

            stage.showAndWait();

            return controller.isConfirm();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    void showSearchDialog(MainController mc, String text) {
        try {
            // Load the .fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/SearchView.fxml"));
            GridPane page = loader.load();

            // create DIALOG
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.NONE);

            // set how to close the dialog action
            stage.focusedProperty().addListener((obs, wasFocus, nowFocus) -> {
                if (!nowFocus) stage.hide();
            });

            page.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ESCAPE) stage.hide();
            });

            Scene scene = new Scene(page);
            stage.setScene(scene);

            // Controller
            SearchView controller = loader.getController();
            controller.setController(mc);
            controller.setInitText(text);
            controller.onSearch();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Optional<ButtonType> showConfirmationDialog() {
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