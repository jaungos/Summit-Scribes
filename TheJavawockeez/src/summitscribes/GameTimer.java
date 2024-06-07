package summitscribes;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sprites.MainPlayer;
import javafx.scene.effect.DropShadow;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.paint.Color;

public class GameTimer extends AnimationTimer {
    private final static double BACKGROUND_SPEED = 10.0; // Adjust as needed
    private long lastTime = 0;
    private JSONReader parseJSON;
    private int numberOfWordsToDisplay;
    private ArrayList<String> wordList;
    private Random randomWordSelector;
    private long startTimer;
    private MainPlayer player;
    private GraphicsContext gc;
    private Scene scene;
    private Stage stage;
    private Group root;
    private HBox wordBox;
    private Group wordGroup;
    private double backgroundY = -6010;

    private Image winImage;
    private ImageView winImageView = new ImageView(winImage);
    private boolean gameWon = false;
    private long winTime = 0;

    public final static String VERY_EASY_WORDS = "TheJavawockeez/words/english_1k.json";
    public final static String EASY_WORDS = "TheJavawockeez/words/english_5k.json";
    public final static String MEDIUM_WORDS = "TheJavawockeez/words/english_medical.json";
    public final static String DIFFICULT_WORDS = "TheJavawockeez/words/english_commonly_misspelled.json";
    public final static String HARDCORE_WORDS = "TheJavawockeez/words/english_contractions.json";
    public final static String DEFAULT_WORDS = "TheJavawockeez/words/english_doubleletter.json";

    private Image background = new Image ("images/main_game1.png", GameTimer.WORLD_WIDTH, GameTimer.WORLD_HEIGHT-10000, false, false);

    public final static int WORLD_WIDTH = 1920;
    public final static int WORLD_HEIGHT = 7096;

    GameTimer(MainPlayer player, int numberOfWordsToDisplay, GraphicsContext gc, Scene scene, Stage stage, Group root) {
        this.parseJSON = new JSONReader();
        this.numberOfWordsToDisplay = numberOfWordsToDisplay;
        this.randomWordSelector = new Random();
        this.startTimer = System.nanoTime();
        this.player = player;
        this.gc = gc;
        this.scene = scene;
        this.stage = stage;
        this.root = root;
        this.wordBox = new HBox();
        this.wordGroup = new Group();
        this.setWords();
        this.wordList = this.parseJSON.getWordList();
        this.player.setCurrentWordToDisplaySize(this.wordList);
        this.handleKeyPressEvent();
    }

    @Override
    public void handle(long currentNanoTime) {
        this.refreshBG();
        double elapsedTime = (currentNanoTime - lastTime) / 1e9;
        lastTime = currentNanoTime;

        this.renderSprites();

        if (!gameWon && this.player.isPlayerFinished(this.numberOfWordsToDisplay)) {
            gameWon = true;
            winTime = currentNanoTime;
            winImage = new Image("images/win.gif");
            winImageView = new ImageView(winImage);
            winImageView.setFitWidth(GameTimer.WORLD_WIDTH);
            winImageView.setFitHeight(1080);

            root.getChildren().add(winImageView);
        }

        if (gameWon && currentNanoTime - winTime >= 12_000_000_000L) {
            this.stop();
            long currentTime = currentNanoTime / 1_000_000_000;
            long startTime = this.startTimer / 1_000_000_000;

            long totalTime = currentTime - startTime;

            GameOverStage gameOver = new GameOverStage(this.stage, totalTime, this.player.getServerNumber(), this.player.getPortNumber());
            gameOver.setScene();
            this.stage.setScene(gameOver.getScene());
        }
    }


    void refreshBG () {
        // clears the graphic content of the window
        this.gc.clearRect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        // redraws the background image
        this.gc.drawImage(background, 0, this.backgroundY);
    }


    void handleKeyPressEvent() {
        this.scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                KeyCode code = e.getCode();
                checkLetterPressed(code.toString().toLowerCase());

                if (player.isWordFinished() && !player.isPlayerFinished(numberOfWordsToDisplay)) {
                    displayNextWord();
                }

            }
        });

        // Reset MainPlayer's dy when key is released
        this.scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                player.setDY(0); // Stop vertical movement
            }
        });
    }

    private void checkLetterPressed(String letterPressed) {
        char currentLetter = this.player.getCurrentWordToDisplay()[this.player.getCorrectLettersCount()];

        if (letterPressed.charAt(0) == currentLetter || (currentLetter == '\'' && letterPressed.equals("quote"))) {
            this.wordBox.getChildren().get(this.player.getCorrectLettersCount()).setVisible(false);
            this.player.incrementCorrectLettersCount();

            // Call climb() when the correct key is pressed
            this.player.climb();

            // Move the background
            moveBackground();
        } else {
            // TODO: Handle incorrect key press
        }

        if (this.player.isWordFinished()) {
            this.player.incrementCorrectWordsCount();
            return;
        }
    }

    private void moveBackground() {
        this.backgroundY += GameTimer.BACKGROUND_SPEED;
    }


    void displayNextWord() {
        // Perform the necessary re-initializations of certain variables and layouts
        this.player.resetCorrectLetters();
        this.root.getChildren().remove(this.wordGroup);
        this.wordBox = new HBox();

        this.player.setCurrentWordToDisplay(this.getNextWord().toLowerCase().toCharArray());

        for (char wordCharacter : this.player.getCurrentWordToDisplay()) {
            Text letter = new Text(wordCharacter + "");
            letter.setFont(Font.font("Arial", FontWeight.BOLD, 80)); // Set font and size
            letter.setFill(Color.WHITE); // Set text color to white

            // Create and set the drop shadow effect
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(5);
            dropShadow.setOffsetX(3);
            dropShadow.setOffsetY(3);
            dropShadow.setColor(Color.BLACK);
            letter.setEffect(dropShadow);

            this.wordBox.getChildren().add(letter);
        }

        this.wordBox.setAlignment(Pos.CENTER);

        this.wordGroup.getChildren().setAll(this.wordBox);
        this.wordGroup.setLayoutX(820);
        this.wordGroup.setLayoutY(795); //lower = higher

        this.root.getChildren().add(this.wordGroup);
    }


    String getNextWord() {
        ArrayList<String> availableWords = new ArrayList<>(this.wordList);
        availableWords.removeAll(this.player.getUsedWords()); // Remove used words

        int randomIndex = this.randomWordSelector.nextInt(availableWords.size());
        String nextWord = availableWords.get(randomIndex);
        this.player.addUsedWords(nextWord); // Mark word as used

        return nextWord;
    }

    private void setWords() {
        System.out.println("Set words: " + this.player.getDifficulty());
        switch (this.player.getDifficulty()) {
            case "Very Easy":
                this.parseJSON.readFile(VERY_EASY_WORDS);
                break;
            case "Easy":
                this.parseJSON.readFile(EASY_WORDS);
                break;
            case "Medium":
                this.parseJSON.readFile(MEDIUM_WORDS);
                break;
            case "Difficult":
                this.parseJSON.readFile(DIFFICULT_WORDS);
                break;
            case "Hardcore":
                this.parseJSON.readFile(HARDCORE_WORDS);
                break;
            default:
                this.parseJSON.readFile(DEFAULT_WORDS);
        }
    }

    void renderSprites() {
        this.player.render(this.gc); //player
    }

    ArrayList<String> getWords() {
        return this.wordList;
    }

}
