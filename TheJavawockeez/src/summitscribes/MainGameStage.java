package summitscribes;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sprites.MainPlayer;

public class MainGameStage {
    private MainPlayer player;
    private double backgroundOffsetY = 0.0;
    private Stage stage;
    private Scene scene;
    private Group root;
    private StackPane stack;
    private Canvas canvas;
    private GraphicsContext gc;
    private GameTimer gameTimer;
    private Image backgroundImage;
    private Image background;
    private Image stoneBgImage;

    private final static double BACKGROUND_SPEED = 50.0; // Adjust as needed
    public final static int MAIN_GAME_WINDOW_HEIGHT = 1080;
    public final static int MAIN_GAME_WINDOW_WIDTH = 1920;
    public final static int MAIN_GAME_MAP_HEIGHT = 7096;

    MainGameStage(MainPlayer player, Stage stage, int numberOfWordsToDisplay) {
        this.player = player;
        this.stage = stage;
        this.root = new Group();
        this.stack = new StackPane();

        // Load the background image
        this.backgroundImage = new Image("images/main_game1.png", MainGameStage. MAIN_GAME_WINDOW_WIDTH, MainGameStage. MAIN_GAME_MAP_HEIGHT, false, false);

        // Set up the canvas and graphics context
        this.canvas = new Canvas(MainGameStage.MAIN_GAME_WINDOW_WIDTH, MainGameStage.MAIN_GAME_WINDOW_HEIGHT);
        this.gc = this.canvas.getGraphicsContext2D();

        // Initialize the background image with correct size and position
        BackgroundImage bgImage = new BackgroundImage(this.backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        this.stack.setBackground(new Background(bgImage));

        this.stoneBgImage = new Image("images/stoneWordBg.png");
        ImageView stoneBgView = new ImageView(stoneBgImage);
        stoneBgView.setFitWidth(754); // Adjust as needed
        stoneBgView.setFitHeight(174); // Adjust as needed

        stoneBgView.setTranslateX(0); // Move to the right or left
        stoneBgView.setTranslateY(-150); // Move down or up

        this.stack.getChildren().addAll(stoneBgView);
        StackPane.setAlignment(stoneBgView, Pos.BOTTOM_CENTER);

        this.scene = new Scene(this.root, MainGameStage.MAIN_GAME_WINDOW_WIDTH, MainGameStage.MAIN_GAME_WINDOW_HEIGHT);

        // Instantiate an Animation Timer
        this.gameTimer = new GameTimer(this.player, numberOfWordsToDisplay, this.gc, this.scene, this.stage, this.root);

        this.stage.setTitle("Summit Scribes");
        this.stage.centerOnScreen();
        this.stage.toFront();

    }

    void setScene() {
        // Set background image
        //Image mainGameBg = new Image("images/main_game1.png", MainGameStage.MAIN_GAME_WINDOW_WIDTH, MainGameStage.MAIN_GAME_MAP_HEIGHT, false, false);

        // Draw the background image at its initial position
        gc.drawImage(backgroundImage, 0, 0); // Draw background

        this.stack.getChildren().addAll(this.canvas);
        this.root.getChildren().addAll(this.stack);
    }

    Scene getScene() {
        return this.scene;
    }

    void setGame() {
        this.gameTimer.displayNextWord();
        this.gameTimer.start();
    }
}
