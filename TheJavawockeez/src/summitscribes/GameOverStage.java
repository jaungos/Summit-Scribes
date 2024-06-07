package summitscribes;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.Socket;

public class GameOverStage {
    private String serverNumber;
    private int portNumber;
    private long totalTime;

    private Scene scene;
    private Stage stage;
    private Group root;
    private Canvas canvas;
    private GraphicsContext gc;
    private StackPane stackPane;

    private WelcomeStage buttonFormatter;

    GameOverStage(Stage stage, long totalTime, String serverNumber, int portNumber) {
        this.totalTime = totalTime;

        this.buttonFormatter = new WelcomeStage(this.serverNumber, this.portNumber);

        this.stage = stage;

        this.stackPane = new StackPane();
        this.root = new Group();
        this.scene = new Scene(this.root, WelcomeStage.WINDOW_HEIGHT, WelcomeStage.WINDOW_HEIGHT);

        this.canvas = new Canvas(WelcomeStage.WINDOW_HEIGHT, WelcomeStage.WINDOW_HEIGHT);
        this.gc = this.canvas.getGraphicsContext2D();
    }

    void setScene() {
        // Import custom font and add the game summaries
        Font font = Font.loadFont("file:resources/font/prstartk.ttf", 40);
        gc.setFont(font);
        gc.fillText("Manoy", 300, 340);
        gc.fillText("" + this.totalTime, 570, 340);

        Image postGameBg = new Image("images/game_over.gif");
        ImageView postGame = new ImageView();
        postGame.setImage(postGameBg);
        postGame.setFitHeight(WelcomeStage.WINDOW_HEIGHT);
        postGame.setPreserveRatio(true);

        this.stackPane.getChildren().addAll(postGame);

        // Reference: https://jenkov.com/tutorials/javafx/button.html
        Image exitButton = new Image("images/exit_button.png");
        ImageView exButton = new ImageView(exitButton);
        exButton.setFitHeight(68);
        exButton.setPreserveRatio(true);
        Button backbtn = new Button();
        backbtn.setGraphic(exButton);

        backbtn = this.buttonFormatter.formatButton(backbtn);

        backbtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent arg0) {
                System.out.println("End of Program! Bye!");
                System.exit(0);
            }
        });

        Group btn = new Group();
        btn.setLayoutX(300);
        btn.setLayoutY(670);
        btn.getChildren().add(backbtn);

        this.root.getChildren().addAll(this.stackPane, this.canvas, btn);

        this.stage.setTitle("Game Over");
        this.stage.setMaximized(false);
        this.stage.setResizable(false);
        this.stage.centerOnScreen();
        this.stage.toFront();
    }

    Scene getScene() {
        return this.scene;
    }
}
