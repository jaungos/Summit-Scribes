package sprites;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import main.ClientHandler;

import java.io.*;
import java.util.*;

public class MainPlayer extends Sprite {
    private String serverNumber;
    private int portNumber;
    private ClientHandler clientHandler;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private int currentFrame = 1;
    private static final int NUM_FRAMES = 6;
    private static final Image[] CLIMB_FRAMES = new Image[NUM_FRAMES];

    private String name;
    private boolean isHost;
    private boolean isReady;
    private String difficulty;
    private HashSet<String> usedWords;
    private VBox chatRoom;

    private int correctLetters;
    private int correctWords;
    private char[] currentWordToDisplay;

    public double dy; // Added dy for vertical movement

    // constant attributes for initial position
    private final static double INITIAL_X = 750; // Adjust as needed for centering
    private final static double INITIAL_Y = 300; // Adjust as needed for centering
    private final static double INITIAL_SIZE = 400; // Adjust as needed for size

    static {
        // Load the climbing frames
        for (int i = 0; i < NUM_FRAMES; i++) {
            CLIMB_FRAMES[i] = new Image("images/" + (i + 1) + ".png", INITIAL_SIZE, INITIAL_SIZE, false, false);
        }
    }

    public MainPlayer(String name, String serverNumber, int portNumber) {
        super(INITIAL_X, INITIAL_Y, CLIMB_FRAMES[0]); // Set initial frame
        this.name = name;
        this.isReady = false;

        this.serverNumber = serverNumber;
        this.portNumber = portNumber;
        this.clientHandler = new ClientHandler(this.serverNumber, this.portNumber, this.name, this);

        this.height = INITIAL_SIZE;
        this.width = INITIAL_SIZE;

        this.usedWords = new HashSet<String>();

        this.correctLetters = 0;
        this.correctWords = 0;
    }

    public String getServerNumber() {
        return this.serverNumber;
    }

    public int getPortNumber() {
        return this.portNumber;
    }

    public void climb() {
        // Update the frame when climbing
        currentFrame = (currentFrame % NUM_FRAMES) + 1; // Cycle through frames 1 to NUM_FRAMES
        setImage(CLIMB_FRAMES[currentFrame - 1]);
    }

    public double getPositionY() {
        return this.yPos;
    }

    public String getName() {
        return this.name;
    }

    public void setHostPrivilege(boolean isHost) {
        this.isHost = isHost;
    }

    public boolean getHostPrivilege() {
        return this.isHost;
    }

    public void setReadyState() {
        this.isReady = true;
    }

    public boolean getReadyState() {
        return this.isReady;
    }

    public void setDifficulty(String selectedDifficulty) {
        this.difficulty = selectedDifficulty;
    }

    public void setCurrentWordToDisplaySize (ArrayList<String> wordList) {
        // Get the longest string for that specific wordList
        // Reference: https://stackoverflow.com/questions/32593820/finding-largest-string-in-arraylist
        String max = Collections.max(wordList, Comparator.comparing(String::length)); // or s -> s.length()

        // Use the length of the longest word as the basis for creating the array
        this.currentWordToDisplay = new char[max.length() + 1];
    }

    public void introducePlayer() {
        this.clientHandler.connectToServer();
    }

    public void sendMessage(String messageToSend) {
        this.clientHandler.broadcastToServer(this.name, messageToSend);
    }

    public void setChatRoom(VBox chatBox) {
        this.chatRoom = chatBox;
    }

    public void addLabel(String messageFromOtherPlayer) {
        HBox chatMessage = new HBox();
        chatMessage.setAlignment(Pos.CENTER_LEFT);
        chatMessage.setPadding(new Insets(5, 5, 5, 5));

        Text message = new Text(messageFromOtherPlayer);
        TextFlow textFlow = new TextFlow(message);

        // Set the style of the chat message
        // Reference: https://www.youtube.com/watch?v=_1nqY-DKP9A&t=1754s
        textFlow.setStyle("-fx-background-color: rgb(233, 233, 235);" + "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));

        chatMessage.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatRoom.getChildren().add(chatMessage);
            }
        });
    }

    public void resetCorrectLetters() {
        this.correctLetters = 0;
    }

    public void incrementCorrectLettersCount() {
        this.correctLetters++;
    }

    public int getCorrectLettersCount() {
        return this.correctLetters;
    }

    public void incrementCorrectWordsCount() {
        this.correctWords++;
    }

    public void setCurrentWordToDisplay(char[] currentWord) {
        this.currentWordToDisplay = currentWord;
    }

    public char[] getCurrentWordToDisplay() {
        return this.currentWordToDisplay;
    }

    public void addUsedWords(String alreadyUsedWord) {
        this.usedWords.add(alreadyUsedWord);
    }

    public HashSet<String> getUsedWords() {
        return this.usedWords;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public boolean isWordFinished() {
        return this.correctLetters == this.currentWordToDisplay.length;
    }

    public boolean isPlayerFinished(int numberOfWordsToDisplay) {
        return this.correctWords == numberOfWordsToDisplay;
    }
}
