package streamboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamboard.controller.MainController;
import streamboard.service.PreferencesService;

public class Main extends Application {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private final PreferencesService prefsService = PreferencesService.INSTANCE;

    @Override
    public void start(Stage primaryStage) throws Exception{
        log.info("StreamBoard starting...");
        String fxmlFile = "/fxml/main.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream(fxmlFile));
        Scene mainScene = new Scene(root, 800, 600);
        primaryStage.setTitle("StreamBoard");
        MainController controller = (MainController)loader.getController();
        controller.setActiveStage(primaryStage);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
