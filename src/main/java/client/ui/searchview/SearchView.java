package client.ui.searchview;

import client.config.Setting;
import client.controls.ActionController;
import client.controls.EventAdapter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;

//TODO - delegate all not so view related operation to ViewModel

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */
public class SearchView {

    @FXML
    private TextField searchBar;
    @FXML
    private ListView<EventAdapter> searchList;

    private ActionController controller;
    private static final Label placeHolder = new Label("No items found");
    private final ObservableList<EventAdapter> items = FXCollections.observableArrayList();

    public void initialize() {
        prepareList();
    }

    @FXML
    public void onSearch() {
        items.setAll(controller.getEventManager().search(searchBar.getText()));
        searchList.setItems(items);
    }

    private void prepareList() {
        // set simple factory for cell including color, date, and name
        searchList.setCellFactory(c -> new ListCell<>() {
            @Override
            protected void updateItem(EventAdapter item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setGraphic(null);
                    setText("");
                } else {
                    setText(String.format("%s       %s",
                            Setting.getDatePattern().format(item.startProperty().get()),
                            item.nameProperty().get()));
                    setGraphicTextGap(10);
                    setGraphic(new Circle(3, item.colorProperty().get()));
                }
            }
        });

        // set place holder text when empty
        searchList.setPlaceholder(placeHolder);

        // set on mouse double clicked action
        searchList.setOnMouseClicked(clicked -> {
            if (clicked.getClickCount() == 2 && clicked.getButton() == MouseButton.PRIMARY) {
                controller.handleEdit(searchList.getSelectionModel().getSelectedItem());
            }
        });
    }

    public void setController(ActionController controller) {
        this.controller = controller;
    }

    public void setInitText(String text) {
        searchBar.setText(text);
    }


}
