package summitscribes;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sprites.MainPlayer;

import java.net.Socket;

public class JoinCreateStage {
    private String serverNumber;
    private int portNumber;
    private MainPlayer player;

    private Scene scene;
    private Stage stage;
    private Group root;
    private VBox gameOptions;
    private ImageView newGamebg;
    private WelcomeStage buttonFormatter;

    JoinCreateStage(Stage stage, String serverNumber, int portNumber) {
        this.serverNumber = serverNumber;
        this.portNumber = portNumber;

        this.stage = stage;
        this.root = new Group();
        this.gameOptions = new VBox();
        this.buttonFormatter = new WelcomeStage(this.serverNumber, this.portNumber);

        Image background = new Image("images/new_game.gif");
        this.newGamebg = new ImageView();

        this.newGamebg.setImage(background);
        this.newGamebg.setFitHeight(WelcomeStage.WINDOW_HEIGHT);
        this.newGamebg.setPreserveRatio(true);
    }

    void setScene() {
        // Reference: https://jenkov.com/tutorials/javafx/button.html
        Image create = new Image("images/host_button.png");
        ImageView createButton = new ImageView(create);
        createButton.setFitHeight(68);
        createButton.setPreserveRatio(true);
        Button createBtn = new Button();
        createBtn.setGraphic(createButton);

        Image join = new Image("images/join_button.png");
        ImageView joinButton = new ImageView(join);
        joinButton.setFitHeight(68);
        joinButton.setPreserveRatio(true);
        Button joinBtn = new Button();
        joinBtn.setGraphic(joinButton);

        Image returnButton = new Image("images/return_button.png");
        ImageView retButton = new ImageView(returnButton);
        retButton.setFitHeight(68);
        retButton.setPreserveRatio(true);
        Button backbtn = new Button();
        backbtn.setGraphic(retButton);

        createBtn = this.buttonFormatter.formatButton(createBtn);
        joinBtn = this.buttonFormatter.formatButton(joinBtn);
        backbtn = this.buttonFormatter.formatButton(backbtn);

        // Host Game
        createBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                CustomDialog customDialog = new CustomDialog(stage, true);
                customDialog.show();
                String playerName = customDialog.getPlayerName();
                if (playerName != null && !playerName.isEmpty()) {
                    player = new MainPlayer(playerName, serverNumber, portNumber);
                    player.setHostPrivilege(true);

                    NewGameStage newGame = new NewGameStage(stage, player);
                    newGame.setScene();
                    stage.setScene(newGame.getScene());
                }
            }
        });

        // Join Game
        joinBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                CustomDialog customDialog = new CustomDialog(stage, false);
                customDialog.show();
                String lobbyCode = customDialog.getPlayerName();

                if (lobbyCode != null && !lobbyCode.isEmpty()) {
                    CustomDialog customDialogName = new CustomDialog(stage, true);
                    customDialogName.show();
                    String playerName = customDialogName.getPlayerName();
                    if (playerName != null && !playerName.isEmpty()) {
                        player = new MainPlayer(playerName, serverNumber, portNumber);
                        player.setHostPrivilege(false);

                        NewGameStage newGame = new NewGameStage(stage, player, lobbyCode);
                        newGame.setScene();
                        stage.setScene(newGame.getScene());
                    }
                }

            }
        });

        backbtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent arg0) {
                WelcomeStage game = new WelcomeStage(serverNumber, portNumber);
                game.setStage(stage);
            }
        });

        this.gameOptions.setAlignment(Pos.CENTER);
        this.gameOptions.getChildren().addAll(createBtn, joinBtn, backbtn);
        this.gameOptions.setLayoutX(280);
        this.gameOptions.setLayoutY(340);

        this.root.getChildren().addAll(this.newGamebg, this.gameOptions);
        this.scene = new Scene(this.root, WelcomeStage.WINDOW_HEIGHT, NewGameStage.WINDOW_HEIGHT);

        // Sets the logo of the application
        // Reference: https://youtu.be/UZKKaI8OnjY
        Image icon = new Image("images/game_icon1.png");
        this.stage.getIcons().add(icon);

        this.stage.setTitle("Game");
    }

    Scene getScene() {
        return this.scene;
    }


}
