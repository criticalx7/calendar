package view;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.shape.Circle;
import model.Event;

public class SearchView {

    @FXML
    private TextField searchBar;

    @FXML
    private ListView<Event> searchList;

    private MainController controller;

    public void initialize() {
        prepareList();
    }

    @FXML
    public void onSearch() {
        searchList.setItems(controller.getEventManager().search(searchBar.getText()));
    }

    private void prepareList() {
        // set simple factory for cell including color, date, and name
        searchList.setCellFactory(c -> new ListCell<>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setGraphic(null);
                    setText("");
                } else {
                    setText(String.format("%s       %s",
                            Event.getDefaultDatePattern().format(item.getStart()),
                            item.getName()));
                    setGraphicTextGap(10);
                    setGraphic(new Circle(3, item.getColor()));
                }
            }
        });

        // set place holder text when empty
        searchList.setPlaceholder(new Label("Nothing to show"));

        // set on mouse double clicked action
        searchList.setOnMouseClicked(clicked -> {
            if (clicked.getClickCount() == 2 && clicked.getButton() == MouseButton.PRIMARY) {
                Event event = searchList.getSelectionModel().getSelectedItem();
                controller.handleEdit(event);
            }
        });
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    public void setInitText(String text) {
        searchBar.setText(text);
    }



}
