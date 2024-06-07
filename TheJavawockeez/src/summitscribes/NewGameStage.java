package summitscribes;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import com.sun.corba.se.impl.orbutil.graph.Graph;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import sprites.MainPlayer;

public class NewGameStage {
    private MainPlayer player;

    private String difficulty;
    private int numberOfWordsToDisplay;

    private Scene scene;
    private Stage stage;
    private Group root;
    private VBox gameOptions;
    private ImageView newGamebg;
    private WelcomeStage buttonFormatter;
    private String code;

    public static ArrayList<MainPlayer> players = new ArrayList<>();
    public static ArrayList<MainGameStage> allPlayerGameTimers = new ArrayList<>();
    public static final int WINDOW_HEIGHT = 800;
    public static final int WINDOW_WIDTH = 1250;

    NewGameStage(Stage stage, MainPlayer newPlayer) {
        this.player = newPlayer;
        players.add(newPlayer);

        this.enableChatRoom();

        this.difficulty = "Easy";
        this.numberOfWordsToDisplay = 5;
        this.code = generateRandomCode();

        this.stage = stage;
        this.root = new Group();
        this.gameOptions = new VBox();
        this.buttonFormatter = new WelcomeStage(this.player.getServerNumber(), this.player.getPortNumber());

        Image background = new Image("images/new_game.gif");
        this.newGamebg = new ImageView();

        this.newGamebg.setImage(background);
        this.newGamebg.setFitHeight(WelcomeStage.WINDOW_HEIGHT);
        this.newGamebg.setFitWidth(NewGameStage.WINDOW_WIDTH);
//		this.newGamebg.setPreserveRatio(true);
    }

    NewGameStage(Stage stage, MainPlayer newPlayer, String code) {
        this.player = newPlayer;
        players.add(newPlayer);

        this.enableChatRoom();

        this.difficulty = "Easy";
        this.numberOfWordsToDisplay = 5;
        this.code = code;

        this.stage = stage;
        this.root = new Group();
        this.gameOptions = new VBox();
        this.buttonFormatter = new WelcomeStage(this.player.getServerNumber(), this.player.getPortNumber());

        Image background = new Image("images/new_game.gif");
        this.newGamebg = new ImageView();

        this.newGamebg.setImage(background);
        this.newGamebg.setFitHeight(WelcomeStage.WINDOW_HEIGHT);
        this.newGamebg.setFitWidth(NewGameStage.WINDOW_WIDTH);
//		this.newGamebg.setPreserveRatio(true);
    }

    private void enableChatRoom() {
        this.player.introducePlayer();
    }

    void setScene() {
        // Reference: https://jenkov.com/tutorials/javafx/button.html
        Image play;
//        if(this.player.getHostPrivilege()){
//            play = new Image("images/play_button.png");
//        }else{
//            play = new Image("images/ready_button.png");
//        }
        play = new Image("images/ready_button.png");
        ImageView playButton = new ImageView(play);
        playButton.setFitHeight(68);
        playButton.setPreserveRatio(true);
        Button newGamebtn = new Button();
        newGamebtn.setGraphic(playButton);

        Image returnButton = new Image("images/return_button.png");
        ImageView retButton = new ImageView(returnButton);
        retButton.setFitHeight(68);
        retButton.setPreserveRatio(true);
        Button backbtn = new Button();
        backbtn.setGraphic(retButton);

//		choice box Difficulty
//		Image choice = new Image("images/play_button.png");
//		ImageView choiceButton = new ImageView(choice);
        ChoiceBox<String> choiceBoxDifficulty = new ChoiceBox<>();
        choiceBoxDifficulty.getItems().addAll("Novice Nook(Very Easy)", "Beginner Bluffs(Easy)",
                "Intermediate Peaks(Medium)", "Advanced Ascents(Difficult)", "Expert Escalation(Hardcore)",
                "Standard Slopes(Default)");
        choiceBoxDifficulty.setValue("Standard Slopes(Default)");
//		choiceBox.setGraphic(choiceButton);

//		choice box Num of Words
//		Image choice = new Image("images/play_button.png");
//		ImageView choiceButton = new ImageView(choice);
        ChoiceBox<Integer> choiceBoxNumWords = new ChoiceBox<>();
        choiceBoxNumWords.getItems().addAll(5, 10,
                15, 20, 25,
                2);
        choiceBoxNumWords.setValue(2);
//		choiceBox.setGraphic(choiceButton);

        newGamebtn = this.buttonFormatter.formatButton(newGamebtn);
        backbtn = this.buttonFormatter.formatButton(backbtn);

        // Start game
        newGamebtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent arg0) {
                difficulty = setDifficulty(choiceBoxDifficulty);
                numberOfWordsToDisplay = choiceBoxNumWords.getValue();

                // TODO: check if all players are ready
                for (MainPlayer player : players) {
                    player.setDifficulty(difficulty);
                }

                MainGameStage game = new MainGameStage(player, stage, numberOfWordsToDisplay);
                game.setScene();
                stage.setScene(game.getScene());
                // Reference: https://stackoverflow.com/questions/31426912/how-to-make-window-fullscreen-maximized-in-scene-builder
                stage.setFullScreen(true);
                stage.centerOnScreen();
                stage.toFront();
                game.setGame();
            }
        });

        TextField chatField = new TextField();

        Label codeValue = new Label(this.code);
        codeValue.setAlignment(Pos.CENTER);
        VBox codeContainer = new VBox(codeValue);

//        StringAlignment leftAlignment = new StringAlignment(width, StringAlignment.Alignment.LEFT);
//        String leftAlignedString = leftAlignment.format(input);


        backbtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent arg0) {
                WelcomeStage game = new WelcomeStage(player.getServerNumber(), player.getPortNumber());
                game.setStage(stage);
            }
        });

        this.gameOptions.setAlignment(Pos.CENTER);
        this.gameOptions.getChildren().addAll(newGamebtn);

        Group btn = new Group();
        btn.setLayoutX(520);
        btn.setLayoutY(670);
        btn.getChildren().add(backbtn);

        Group btnNewGame = new Group();
        btnNewGame.setLayoutX(520);
        btnNewGame.setLayoutY(580);
        btnNewGame.getChildren().add(newGamebtn);

        chatField.setMinWidth(230);
        chatField.setLayoutX(845);
        chatField.setLayoutY(490);
        chatField.setPromptText("Send chat");
        chatField.setStyle(
//                "-fx-background-color: transparent; " +
//                "-fx-text-fill: white; " +
                        "-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%); " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 5px;"
        );

//        codeLabel.setLayoutX(580);
//        codeLabel.setLayoutY(90);
//        codeLabel.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 16));

        codeValue.setLayoutX(590);
        codeValue.setLayoutY(100);
        codeContainer.setLayoutX(568);
        codeContainer.setLayoutY(95);
        codeValue.setStyle("-fx-font-weight: 1500;");
        codeValue.setFont(Font.font("Rockwell", FontWeight.EXTRA_BOLD, 25));
        codeValue.setTextFill(Color.WHITESMOKE);


        choiceBoxDifficulty.setPrefWidth(190);
        choiceBoxDifficulty.setLayoutX(205);
        choiceBoxDifficulty.setLayoutY(320);
        choiceBoxDifficulty.setStyle(
//                "-fx-background-color: transparent; " +
                "-fx-text-fill: white; " +
                        "-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%); " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 5px;"
        );

        choiceBoxNumWords.setPrefWidth(190);
        choiceBoxNumWords.setLayoutX(205);
        choiceBoxNumWords.setLayoutY(420);
        choiceBoxNumWords.setStyle(
//                "-fx-background-color: transparent; " +
                        "-fx-text-fill: white; " +
                        "-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%); " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 5px;"
        );

        Image backgroundChoice = new Image("images/bg.png");
        ImageView choiceBg = new ImageView();
        choiceBg.setImage(backgroundChoice);
        choiceBg.setFitHeight(400);
        choiceBg.setPreserveRatio(true);
        choiceBg.setLayoutX(450);
        choiceBg.setLayoutY(170);

        Image backgroundChoice2 = new Image("images/bg.png");
        ImageView players = new ImageView();
        players.setImage(backgroundChoice2);
        players.setFitHeight(400);
        players.setPreserveRatio(true);
        players.setLayoutX(120);
        players.setLayoutY(170);

        Image backgroundChoice3 = new Image("images/bg.png");
        ImageView chatBoxBg = new ImageView();
        chatBoxBg.setImage(backgroundChoice3);
        chatBoxBg.setFitHeight(400);
        chatBoxBg.setPreserveRatio(true);
        chatBoxBg.setLayoutX(780);
        chatBoxBg.setLayoutY(170);

        Image backgroundCode = new Image("images/bg.png");
        ImageView codeBg = new ImageView();
        codeBg.setImage(backgroundCode);
        codeBg.setFitHeight(70);
        codeBg.setFitWidth(200);
//        codeBg.setPreserveRatio(true);
        codeBg.setLayoutX(520);
        codeBg.setLayoutY(75);

        Image textDifficulty = new Image("images/difficulty.png");
        ImageView difficulty = new ImageView();
        difficulty.setImage(textDifficulty);
        difficulty.setFitWidth(200);
        difficulty.setPreserveRatio(true);
        difficulty.setLayoutX(200);
        difficulty.setLayoutY(285);

        Image textNoWords = new Image("images/no._words.png");
        ImageView noWords = new ImageView();
        noWords.setImage(textNoWords);
        noWords.setFitWidth(200);
        noWords.setPreserveRatio(true);
        noWords.setLayoutX(200);
        noWords.setLayoutY(385);

        Image textSettings = new Image("images/settings.png");
        ImageView settings = new ImageView();
        settings.setImage(textSettings);
        settings.setFitWidth(200);
        settings.setPreserveRatio(true);
        settings.setLayoutX(200);
        settings.setLayoutY(155);

        Image textPlayers2 = new Image("images/players.png");
        ImageView players2 = new ImageView();
        players2.setImage(textPlayers2);
        players2.setFitWidth(200);
        players2.setPreserveRatio(true);
        players2.setLayoutX(530);
        players2.setLayoutY(155);

        Image textChat = new Image("images/chat.png");
        ImageView chat = new ImageView();
        chat.setImage(textChat);
        chat.setFitWidth(200);
        chat.setPreserveRatio(true);
        chat.setLayoutX(860);
        chat.setLayoutY(155);

        Image textCode = new Image("images/game_code.png");
        ImageView codeHeader = new ImageView();
        codeHeader.setImage(textCode);
        codeHeader.setFitWidth(200);
        codeHeader.setPreserveRatio(true);
        codeHeader.setLayoutX(522);
        codeHeader.setLayoutY(50);

        String[] playerNames = { "Player 1111111111111111111111111111111111111111", "Player 2", "Player 3", "Player 4"};

        // Create the main VBox
        VBox mainVBox = new VBox(10); // 20 is the spacing between inner VBoxes
        mainVBox.setLayoutX(522);
        mainVBox.setLayoutY(230);

        // Iterate over the player groups
        for (String playerName : playerNames) {
            // Create an inner VBox for each group
            VBox playerVBox = new VBox(); // 10 is the spacing between Text elements

            // Add Text elements to the inner VBox
            Text playerText = new Text(playerName);
            playerVBox.getChildren().add(playerText);
            playerText.setStyle("-fx-font-weight: 19;");
            playerText.setFont(Font.font("SansSerif", FontWeight.EXTRA_BOLD, 25));
            playerText.setWrappingWidth(200);
//            playerVBox.set;

            // Add the inner VBox to the main VBox
            mainVBox.getChildren().add(playerVBox);
        }

        VBox playersBox = new VBox();
        playersBox.setPrefSize(230, 250);
        playersBox.setLayoutX(545);
        playersBox.setLayoutY(230);
        playersBox.setPadding(new Insets(10, 20, 10, 2));

        // Add a VBox within a ScrollPane for the chat room
        VBox chatRoom = new VBox();
        chatRoom.setPrefSize(230, 250);
        chatRoom.setLayoutX(845);
        chatRoom.setLayoutY(230);
        chatRoom.setPadding(new Insets(10, 20, 10, 2));

        ScrollPane chatScrollPane = new ScrollPane();
        chatRoom.setStyle("-fx-background-color: rgb(0, 0, 0, 0.3)");
        chatScrollPane.setContent(chatRoom);
        chatScrollPane.setPrefSize(230, 250);
        chatScrollPane.setLayoutX(845);
        chatScrollPane.setLayoutY(230);
        chatScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        chatScrollPane.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-text-fill: white; " +
                        "-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%); " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 5px;"
        );
//        chatScrollPane.setBorder();

        chatRoom.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                chatScrollPane.setVvalue((Double) newValue);
            }
        });

        // this.player.listenForMessages(chatRoom);
        this.player.setChatRoom(chatRoom);

        // When the user presses enter in the chat field, send the message
        chatField.setOnAction(e -> {
            String messageToSend = chatField.getText();

            if (!messageToSend.isEmpty()) {
                HBox chatMessage = new HBox();
                chatMessage.setAlignment(Pos.CENTER_RIGHT);
                chatMessage.setPadding(new Insets(5, 5, 5, 10));

                Text message = new Text(messageToSend);
                TextFlow textFlow = new TextFlow(message);

                // Set the style of the chat message
                // Reference: https://www.youtube.com/watch?v=_1nqY-DKP9A&t=1754s
                textFlow.setStyle("-fx-color: rgb(239, 242, 255);" + "-fx-background-color: rgb(15, 125, 242);" + "-fx-background-radius: 20px;");
                textFlow.setPadding(new Insets(5, 10, 5, 10));
                message.setFill(Color.color(0.934, 0.945, 0.996));

                chatMessage.getChildren().add(textFlow);
                chatRoom.getChildren().add(chatMessage);

                player.sendMessage(messageToSend);
            }

            chatField.clear();
        });

        this.root.getChildren().addAll(this.newGamebg, this.gameOptions, btn, btnNewGame, choiceBg, players, chatBoxBg, playersBox, codeBg, chatScrollPane, codeHeader, difficulty, noWords, settings, players2, chat, choiceBoxDifficulty, choiceBoxNumWords, chatField, codeContainer, mainVBox);
        this.scene = new Scene(this.root, NewGameStage.WINDOW_WIDTH, NewGameStage.WINDOW_HEIGHT);

        // Sets the logo of the application
        // Reference: https://youtu.be/UZKKaI8OnjY
        Image icon = new Image("images/game_icon1.png");
        this.stage.getIcons().add(icon);

        this.stage.setTitle("Game Lobby");
    }

    private String setDifficulty(ChoiceBox<String> choiceBox) {
        switch (choiceBox.getValue()) {
            case "Novice Nook(Very Easy)":
                return "Very Easy";
            case "Beginner Bluffs(Easy)":
                return "Easy";
            case "Intermediate Peaks(Medium)":
                return "Medium";
            case "Advanced Ascents(Difficult)":
                return "Difficult";
            case "Expert Escalation(Hardcore)":
                return "Hardcore";
            default:
                return "Default";
        }
    }

    private void handleJoin(String code) {
        // Add your code here to handle joining the game with the provided code
        System.out.println("Chat " + code);
    }

    private String generateRandomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        // Generate a code of length 6
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }

    Scene getScene() {
        return this.scene;
    }


}
