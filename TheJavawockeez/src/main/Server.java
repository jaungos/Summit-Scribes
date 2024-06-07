package main;

import packets.GameUpdatePacket;
import packets.MessagePacket;
import packets.Packet;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    private static DatagramSocket serverSocket;

    public static ArrayList<SocketAddress> allPlayers = new ArrayList<>();
    public static ArrayList<String> allPlayerNames = new ArrayList<>();
    public static HashMap<String, Integer> allPlayerScores = new HashMap<>();

    public Server(int port) throws IOException {
        serverSocket = new DatagramSocket(port);
        // this.serverSocket.setSoTimeout(10000);
    }

    private void runServer() throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (!serverSocket.isClosed()) {
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");

            serverSocket.receive(packet);

            // InetAddress address = packet.getAddress();
            // int port = packet.getPort();
            // SocketAddress playerSocketAddress = new InetSocketAddress(address, port);

            SocketAddress playerSocketAddress = packet.getSocketAddress();
            System.out.println("Received packet from " + playerSocketAddress);

            if (!allPlayers.contains(playerSocketAddress)) {
                allPlayers.add(playerSocketAddress);
                this.playerConnect(playerSocketAddress, packet.getData());
            } else {
                this.listenPacket(playerSocketAddress, packet.getData());
            }

            System.out.println("Number of players: " + allPlayers.size());
        }
    }

    private void playerConnect(SocketAddress playerSocketAddress, byte[] message) throws IOException, ClassNotFoundException {
        // Read the MessagePacket from the player
        // Add the player to the list of players
        // Broadcast to all players that a new player has joined
        ByteArrayInputStream inputStream = new ByteArrayInputStream(message);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        MessagePacket messagePacket = (MessagePacket) objectInputStream.readObject();

        allPlayerNames.add(messagePacket.getSender());
        allPlayerScores.put(messagePacket.getSender(), 0);

        broadcastMessage(playerSocketAddress, new MessagePacket("SERVER", messagePacket.getSender() + messagePacket.getMessage()));
    }

    private void listenPacket(SocketAddress playerSocketAddress, byte[] message) throws IOException, ClassNotFoundException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(message);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Packet packet = (Packet) objectInputStream.readObject();

        if (packet instanceof MessagePacket) {
            MessagePacket messagePacket = (MessagePacket) packet;
            broadcastMessage(playerSocketAddress, messagePacket);
        }

    }

//    private byte[] serializePacket(Packet packet) throws IOException {
//        if (packet instanceof MessagePacket) {
//            MessagePacket messagePacket = (MessagePacket) packet;
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//            objectOutputStream.writeObject(messagePacket);
//            return outputStream.toByteArray();
//        } else if (packet instanceof GameUpdatePacket) {
//            GameUpdatePacket gameUpdatePacket = (GameUpdatePacket) packet;
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
//            objectOutputStream.writeObject(gameUpdatePacket);
//            return outputStream.toByteArray();
//        } else {
//            throw new IOException("Invalid packet type!");
//        }
//    }

    private void broadcastMessage(SocketAddress playerSocketAddress, Packet message) throws IOException {
        // Send a message to all players except the player who sent the message
        byte[] buffer = null;

        if (message instanceof MessagePacket) {
            MessagePacket messagePacket = (MessagePacket) message;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(messagePacket);
            buffer = outputStream.toByteArray();
        } else if (message instanceof GameUpdatePacket) {
            GameUpdatePacket gameUpdatePacket = (GameUpdatePacket) message;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(gameUpdatePacket);
            buffer = outputStream.toByteArray();
        }

        for (SocketAddress player : allPlayers) {
            if (!player.equals(playerSocketAddress)) {
                InetSocketAddress playerAddress = (InetSocketAddress) player;
                InetAddress address = playerAddress.getAddress();
                int port = playerAddress.getPort();

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
                serverSocket.send(packet);
            }
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        try {
            int portToUse = Integer.parseInt(args[0]);
            Server server = new Server(portToUse);
            server.runServer();
        } catch (IOException e) {
            System.out.println("Usage: java Server <port-no.>\n"+
                    "Make sure to use valid ports (greater than 1023)");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Usage: java Server <port-no.>\n"+
                    "Insufficient arguments given.");
        }
    }
}
