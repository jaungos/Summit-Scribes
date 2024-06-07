package main;

import summitscribes.WelcomeStage;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            // Get the server name using args[0] from main passed through launch
            String serverNumber = getParameters().getRaw().get(0);

            // Get the port number using args[1] from main passed through launch
            int portNumber = Integer.parseInt(getParameters().getRaw().get(1));

            /* Open a ClientSocket and connect to ServerSocket */
            System.out.println("Connecting to " + serverNumber + " on port " + portNumber);

            WelcomeStage game = new WelcomeStage(serverNumber, portNumber);
            game.setStage(stage);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: java GameApp <server-ip> <port-no.>");
            System.exit(0);
        }
    }
}
