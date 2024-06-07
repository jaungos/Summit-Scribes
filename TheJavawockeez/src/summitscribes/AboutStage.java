package summitscribes;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.Socket;

public class AboutStage {
    private String serverNumber;
    private int portNumber;

    private Scene scene;
    private Stage stage;
    private Group root;
    private ImageView newGamebg;
    private WelcomeStage buttonFormatter;

    AboutStage(Stage stage, String serverNumber, int portNumber) {
        this.serverNumber = serverNumber;
        this.portNumber = portNumber;

        this.stage = stage;
        this.root = new Group();
        this.buttonFormatter = new WelcomeStage(this.serverNumber, this.portNumber);

        Image background = new Image("images/about_us.gif");
        this.newGamebg = new ImageView();

        this.newGamebg.setImage(background);
        this.newGamebg.setFitHeight(WelcomeStage.WINDOW_HEIGHT);
        this.newGamebg.setPreserveRatio(true);
    }

    void setScene() {
        // Reference: https://jenkov.com/tutorials/javafx/button.html
        Image returnButton = new Image("images/return_button.png");
        ImageView retButton = new ImageView(returnButton);
        retButton.setFitHeight(68);
        retButton.setPreserveRatio(true);
        Button backbtn = new Button();
        backbtn.setGraphic(retButton);

        backbtn = this.buttonFormatter.formatButton(backbtn);

        backbtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent arg0) {
                WelcomeStage game = new WelcomeStage(serverNumber, portNumber);
                game.setStage(stage);
            }
        });

        Group btn = new Group();
        btn.setLayoutX(300);
        btn.setLayoutY(650);
        btn.getChildren().add(backbtn);

        this.root.getChildren().addAll(this.newGamebg, btn);
        this.scene = new Scene(this.root, WelcomeStage.WINDOW_HEIGHT, WelcomeStage.WINDOW_HEIGHT);

        // Sets the logo of the application
        // Reference: https://youtu.be/UZKKaI8OnjY
        Image icon = new Image("images/game_icon1.png");
        this.stage.getIcons().add(icon);

        this.stage.setTitle("About Us");
    }

    Scene getScene() {
        return this.scene;
    }
}