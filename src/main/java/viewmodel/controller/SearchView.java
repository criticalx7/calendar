package viewmodel.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import model.Event;
import model.EventManager;
import view.ViewManager;

public class SearchView {

    @FXML
    protected TextField searchBar;

    @FXML
    protected ListView<Event> searchList;

    private ObservableList<Event> results;
    private EventManager eventManager;
    private ViewManager viewManager;


    @FXML
    void onSearch() {
        searchList.setItems(eventManager.search(searchBar.getText()));
    }

    public void prepareList() {
        searchList.setItems(results);
        searchList.setCellFactory(c -> new ListCell<>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setGraphic(null);
                    setText("");
                } else {
                    setText(item.getName());
                    setGraphicTextGap(20);
                    setGraphic(new Circle(3, item.getColor()));
                }
            }


        });
    }


    public void setResults(ObservableList<Event> results) {
        this.results = results;
    }

    public void setEventManager(EventManager events) {
        this.eventManager = events;
    }

    public void setViewManager(ViewManager viewManager) {
        this.viewManager = viewManager;
    }

}
