package client.controls;

import client.ClientApp;
import client.ui.editor.EditorView;
import client.ui.editor.EditorViewModel;
import client.ui.monthview.MonthView;
import client.ui.monthview.MonthViewModel;
import client.ui.searchview.SearchView;
import client.utility.ColorUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
/*
 * @author Chatchapol Rasameluangon
 * id 5810404901
 */


/**
 * A class in charge of view related construction and popup.
 */
@Component
public class ViewManager {
    private Parent root;
    private MonthView monthView;


    void setupStageControl(ActionController actionController) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MonthView.fxml"));
            root = loader.load();

            //setup viewModel
            monthView = loader.getController();
            MonthViewModel viewModel = new MonthViewModel(actionController);
            monthView.setViewModel(viewModel);

            // setup the primary stage
            Scene scene = new Scene(root);
            ClientApp.getPrimaryStage().setTitle("Calendar");
            ClientApp.getPrimaryStage().setScene(scene);
            ClientApp.getPrimaryStage().setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        ClientApp.getPrimaryStage().show();
    }

    boolean showEventEditor(EventAdapter eventModel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EventEditor.fxml"));
            Parent page = loader.load();

            // create DIALOG
            Stage stage = new Stage();
            stage.setTitle("Event");
            stage.initOwner(ClientApp.getPrimaryStage());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(page));

            // set ViewModel
            EditorView controller = loader.getController();
            EditorViewModel viewModel = new EditorViewModel(eventModel);
            controller.setViewModel(viewModel);

            ColorUtil.setShadowOverlay(root, stage);
            stage.showAndWait();

            return viewModel.isConfirm();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    void showSearchDialog(ActionController actionController, String text) {
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

            stage.setScene(new Scene(page));

            // Controller
            SearchView controller = loader.getController();
            controller.setController(actionController);
            controller.setInitText(text);
            controller.onSearch();

            ColorUtil.setShadowOverlay(root, stage);
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


    void updateMonthView() {
        monthView.updateView();
    }
}
