package main;

import packets.MessagePacket;
import packets.Packet;
import sprites.MainPlayer;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class ClientHandler {
    private String playerName;
    private String serverNumber;
    private int portNumber;
    private MainPlayer player;

    private DatagramSocket socket;
    private InetAddress serverAddress;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    public static ArrayList<ClientHandler> playerHandlers = new ArrayList<>();

    public ClientHandler(String serverNumber, int portNumber, String playerName, MainPlayer player) {
        this.serverNumber = serverNumber;
        this.portNumber = portNumber;
        this.playerName = playerName;
        this.player = player;

        // Add the new player to the list of player handlers
        playerHandlers.add(this);
    }

    public void connectToServer() {
        try {
            this.socket = new DatagramSocket();
            this.serverAddress = InetAddress.getByName(this.serverNumber);

            Thread listenToMessages = new Thread(() -> {
                System.out.println("Player " + this.playerName + " on server " + this.serverNumber + " at port " + this.portNumber);

                broadcastToServer(this.playerName, " has joined the lobby!");
                listenToServer();
            });

            listenToMessages.start();
        } catch (IOException e) {
            this.closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

//    @Override
//    public void run() {
//        // Listen to a message from this player
//        String messageFromPlayer;
//
//        while (this.socket.isConnected()) {
//            try {
//                messageFromPlayer = this.bufferedReader.readLine();
//                this.broadcastMessage(messageFromPlayer);
//            } catch (IOException e) {
//                closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
//                break;
//            }
//        }
//    }

    public void broadcastToServer(String sender, String messageToSendToServer) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(new MessagePacket(sender, messageToSendToServer));
            byte[] message = outputStream.toByteArray();
            DatagramPacket packet = new DatagramPacket(message, message.length, this.serverAddress, this.portNumber);
            this.socket.send(packet);
        } catch (IOException e) {
            this.closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

    public void listenToServer() {
        try {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                this.socket.receive(packet);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(packet.getData());
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                Packet message = (Packet) objectInputStream.readObject();

                if (message instanceof MessagePacket) {
                    MessagePacket messagePacket = (MessagePacket) message;
                    this.broadcastMessage(messagePacket.getSender() + ": " + messagePacket.getMessage());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            this.closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

    public void broadcastMessage(String messageToSendToEveryone) {
        for (ClientHandler playerHandler : playerHandlers) {
            playerHandler.player.addLabel(messageToSendToEveryone);
        }
    }

    public void removeClientHandler() {
        this.broadcastMessage("SERVER:  " + this.playerName + " has left the lobby!");
        playerHandlers.remove(this);
    }

    public void closeEverything(DatagramSocket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        this.removeClientHandler();
        try {
            if (this.bufferedReader != null) {
                this.bufferedReader.close();
            }

            if (this.bufferedWriter != null) {
                this.bufferedWriter.close();
            }

            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing the socket, buffered reader, or buffered writer!");
        }
    }
}
