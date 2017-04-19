package streamboard.custom;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import streamboard.core.Playable;

import java.io.File;

/**
 * Created by mspellecacy on 4/18/2017.
 */
public class PlayableInputDialog extends Dialog<Playable> {

    private final VBox vbox = new VBox();
    private final HBox pickerBox = new HBox();
    private final HBox labelBox = new HBox();
    private final FileChooser fileChooser = new FileChooser();
    private final Label buttonLabelLabel = new Label("Button Text");
    private final Label fileChooserLabel = new Label("Audio File");
    private final TextField fileNameTextField = new TextField();
    private final TextField buttonLabelTextField = new TextField();
    private final Button chooseFileButton = new Button("Choose");

    //TODO: Add basic audio file validation.

    public PlayableInputDialog(){
        setTitle("Playable File Chooser");
        setHeaderText(null);

        final DialogPane dialogPane = getDialogPane();
        dialogPane.getButtonTypes().addAll(
                ButtonType.CANCEL, ButtonType.OK
        );

        buttonLabelTextField.setPromptText("Ta-Da!");
        fileNameTextField.setPromptText("ta_da.mp3");

        chooseFileButton.setOnAction(event -> {

            File file = fileChooser.showOpenDialog(new Stage());
            if(file.canRead() && file.isFile()) {
                fileNameTextField.setText(file.getAbsolutePath());
            }

        });

        pickerBox.getChildren().addAll(chooseFileButton, fileNameTextField);
        vbox.getChildren().addAll(buttonLabelLabel,buttonLabelTextField, fileChooserLabel, pickerBox);
        dialogPane.setContent(vbox);

        setResultConverter((ButtonType button) -> new Playable(buttonLabelTextField.getText(),fileNameTextField.getText()));

    }

}
