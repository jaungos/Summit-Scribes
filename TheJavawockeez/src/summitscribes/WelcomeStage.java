package summitscribes;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sprites.MainPlayer;

import java.net.Socket;
import java.util.Optional;

public class WelcomeStage {
    private String serverNumber;
    private int portNumber;

    private Scene scene;
    private Stage stage;
    private StackPane root;
    private VBox buttonVbox;
    private ImageView background;

    public static final int WINDOW_HEIGHT = 800; // Since the window is 800x800 pixels, the same constant is used for width

    //	Class Constructor
    public WelcomeStage(String serverNumber, int portNumber) {
        this.serverNumber = serverNumber;
        this.portNumber = portNumber;

        this.root = new StackPane();
        this.buttonVbox = new VBox();
        this.scene = new Scene(root, WINDOW_HEIGHT, WINDOW_HEIGHT);
        this.background = new ImageView();
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        // Sets the background of the welcome stage and fits the gif to the ImageView
        Image bg = new Image("images/welcome.gif");
        this.background.setImage(bg);
        this.background.setFitHeight(WINDOW_HEIGHT);
        this.background.setPreserveRatio(true);

        // Creates the different buttons for the main menu
        // Reference: https://jenkov.com/tutorials/javafx/button.html
        Image design1 = new Image("images/play_button.png");
        ImageView butDesign1 = new ImageView(design1);
        butDesign1.setFitHeight(68);
        butDesign1.setPreserveRatio(true);
        Button newGame = new Button();
        newGame.setGraphic(butDesign1);

        Image design2 = new Image("images/how_button.png");
        ImageView butDesign2 = new ImageView(design2);
        butDesign2.setFitHeight(68);
        butDesign2.setPreserveRatio(true);
        Button instructions = new Button();
        instructions.setGraphic(butDesign2);

        Image design3 = new Image("images/about_button.png");
        ImageView butDesign3 = new ImageView(design3);
        butDesign3.setFitHeight(68);
        butDesign3.setPreserveRatio(true);
        Button aboutGame = new Button();
        aboutGame.setGraphic(butDesign3);

        Image design4 = new Image("images/exit_button.png");
        ImageView butDesign4 = new ImageView(design4);
        butDesign4.setFitHeight(68);
        butDesign4.setPreserveRatio(true);
        Button exitGame = new Button();
        exitGame.setGraphic(butDesign4);

        newGame = formatButton(newGame);
        instructions = formatButton(instructions);
        aboutGame = formatButton(aboutGame);
        exitGame = formatButton(exitGame);

        // Convenience method event handlers for the corresponding button
        newGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
//                CustomDialog customDialog = new CustomDialog(stage, socket);
//                customDialog.show();
//                String playerName = customDialog.getPlayerName();
//                if (playerName != null && !playerName.isEmpty()) {
//                    MainPlayer newPlayer = new MainPlayer(playerName, socket);
//
//                    JoinCreateStage newGameStage = new JoinCreateStage(stage, socket, newPlayer);
//                    newGameStage.setScene();
//                    stage.setScene(newGameStage.getScene());
//                }
                JoinCreateStage newGameStage = new JoinCreateStage(stage, serverNumber, portNumber);
                newGameStage.setScene();
                stage.setScene(newGameStage.getScene());
            }
        });

        instructions.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                InstructionStage instructions = new InstructionStage(stage, serverNumber, portNumber);
                instructions.setScene();
                stage.setScene(instructions.getScene());
            }
        });

        aboutGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                AboutStage aboutUs = new AboutStage(stage, serverNumber, portNumber);
                aboutUs.setScene();
                stage.setScene(aboutUs.getScene());
            }
        });

        exitGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                System.out.println("End of Program! Bye!");
                System.exit(0);
            }
        });

        this.buttonVbox.setAlignment(Pos.CENTER);
        this.buttonVbox.setPadding(new Insets(200, 0, 0, 0));
        this.buttonVbox.getChildren().addAll(newGame, instructions, aboutGame, exitGame);

        this.root.getChildren().addAll(this.background, this.buttonVbox);

        this.stage.setTitle("Summit Scribes");

        // Sets the logo of the application
        // Reference: https://youtu.be/UZKKaI8OnjY
        Image icon = new Image("images/game_icon1.png");
        this.stage.getIcons().add(icon);

        this.stage.setScene(this.scene);
        this.stage.show();
        this.stage.setResizable(false);
    }

    public Button formatButton(Button btn){
        btn.setOnMouseEntered(event -> {
            btn.setStyle("-fx-cursor: hand;");
        });

        // Reference: https://docs.oracle.com/javafx/2/ui_controls/button.htm
        DropShadow shadow = new DropShadow();
        shadow.setRadius(15);

        // Adds a shadow effect when the mouse hovers on the button
        btn.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        btn.setEffect(shadow);
                    }
                });

        // Removes the shadow effect when the mouse leaves the button area
        btn.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        btn.setEffect(null);
                    }
                });

        btn.setPrefSize(160, 30);
        btn.setBackground(null);

        return btn;
    }
}
