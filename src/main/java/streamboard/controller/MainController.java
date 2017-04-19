package streamboard.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import streamboard.core.Playable;
import streamboard.custom.PlayableInputDialog;
import streamboard.service.PreferencesService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    private final PreferencesService prefService = PreferencesService.INSTANCE;

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private FlowPane mainFlowPane;
    @FXML
    private ScrollPane mainScrollPane;

    private Stage activeStage;

    private ObservableList<Playable> playables = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        log.info("Loaded main controller class.");

        //Load our saved preferences
        playables.addAll(prefService.getUserPrefs().getPlayables());
        log.info("Playables Size: {}", playables.size());

        //Do some basic UI/UX
        mainScrollPane.setFitToWidth(true);
        mainFlowPane.setPadding(new Insets(10, 10, 10, 10));
        mainFlowPane.setHgap(10);
        mainFlowPane.setVgap(10);

        //Construct our buttons from saved preferences
        for (Playable playable : playables) {
            mainFlowPane.getChildren().add(generatePlayableButton(playable));
        }
    }

    private Button generatePlayableButton(Playable playable) {
        Button btn = new Button(playable.getLabel());
        btn.setStyle("-fx-font-size: 3em;");
        ContextMenu menu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Remove");
        menu.getItems().add(deleteMenuItem);

        //Play the file associated w/ this button...
        btn.setOnAction(event -> {
            MediaPlayer mp = new MediaPlayer(new Media(new File(playable.getFilePath()).toURI().toString()));
            mp.play();
        });

        //Remove the playable from the registry and the button from the list
        menu.setOnAction(event -> {
            playables.remove(playable);
            mainFlowPane.getChildren().remove(btn);
        });

        //Attach our context menu to our button (conveniently binds to right click)
        btn.setContextMenu(menu);
        return btn;
    }

    public void eventAddPlayableHandler(ActionEvent actionEvent) throws IOException {
        actionEvent.consume();
        PlayableInputDialog dialog = new PlayableInputDialog();
        dialog.showAndWait().ifPresent(r -> {
            if (r.getFilePath() != null && r.getLabel() != null && !r.getLabel().equals("") && !r.getFilePath().equals("")) {
                log.info("Adding Playable: [label: {}, path: {}]", new Object[]{r.getLabel(), r.getFilePath()});
                playables.add(r);
                mainFlowPane.getChildren().add(generatePlayableButton(r));
            } else {
                log.info("Empty dialog response.");
            }
        });

        log.info("Playables Size: {}", playables.size());

    }

    public void eventSavePreferencesHandler(ActionEvent actionEvent) {
        actionEvent.consume();
        prefService.getUserPrefs().setPlayables(new ArrayList<>(playables));
        prefService.savePreferences();
    }

    public void eventQuitApplicationHandler(ActionEvent actionEvent) {
        actionEvent.consume();
        Platform.exit();
    }

    public Stage getActiveStage() {
        return activeStage;
    }

    public void setActiveStage(Stage activeStage) {
        this.activeStage = activeStage;
    }


}
