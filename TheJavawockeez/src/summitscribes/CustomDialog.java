package summitscribes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sprites.MainPlayer;

import java.net.Socket;
import java.util.function.UnaryOperator;

public class CustomDialog {
    private Stage dialogStage;
    private String playerName;
    private boolean isName;

    public CustomDialog(Stage owner, Boolean isName) {
        dialogStage = new Stage();
        dialogStage.initOwner(owner);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initStyle(StageStyle.UNDECORATED);
        dialogStage.setResizable(false);
        this.isName = isName;

        // Background image
        ImageView background = new ImageView(new Image("images/dialogbox.png"));
        background.setFitWidth(400);
        background.setFitHeight(300);

        // Custom title image
        ImageView titleImage = new ImageView(new Image("images/welcome_text.png"));
        titleImage.setFitWidth(310);
        titleImage.setFitHeight(63);

        TextField nameInput = new TextField();
        if (this.isName){
            // Input field and buttons
            nameInput.setPromptText("Enter your name");
        }else{
            // Input field and buttons
            nameInput.setPromptText("Enter lobby code");
        }
            nameInput.setMaxWidth(200);

        // Limit text input to 10 characters
        UnaryOperator<TextFormatter.Change> filter = change -> {
            int maxCharacters = 10;
            if (change.isContentChange()) {
                int newLength = change.getControlNewText().length();
                if (newLength > maxCharacters) {
                    return null; // Null change means no change will occur
                }
            }
            return change;
        };
        nameInput.setTextFormatter(new TextFormatter<>(filter));

        nameInput.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: white; " +
                        "-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%); " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 5px;"
        );

        Button okButton = new Button("OK");
        okButton.setOnAction(event -> {
            playerName = nameInput.getText();
            if (!playerName.isEmpty()) {
//                MainPlayer newPlayer = new MainPlayer(playerName, socket);
//                JoinCreateStage newGameStage = new JoinCreateStage(owner, socket);
//                newGameStage.setScene();
//                owner.setScene(newGameStage.getScene());
                nameInput.clear();
                dialogStage.close();
            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(event -> dialogStage.close());

        // Layout
        VBox content = new VBox(20, titleImage, nameInput, okButton, cancelButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: transparent;"); // Transparent background for the VBox

        StackPane root = new StackPane(background, content);
        Scene scene = new Scene(root, 400, 300);

        dialogStage.setScene(scene);
    }

    public void show() {
        dialogStage.showAndWait();
    }

    public String getPlayerName() {
        return playerName;
    }
}
