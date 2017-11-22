package client;

import client.controls.ActionController;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/*
 * @author Chatchapol Rasameluangon
 * id: 5810404901
 */

public class ClientApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        ClientApp.primaryStage = primaryStage;
        ApplicationContext context = new ClassPathXmlApplicationContext("client-context.xml");
        ActionController mc = context.getBean("actionController", ActionController.class);
        mc.handleLoad();
        mc.getViewManager().show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
