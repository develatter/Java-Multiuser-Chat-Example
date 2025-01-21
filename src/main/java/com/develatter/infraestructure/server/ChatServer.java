package com.develatter.infraestructure.server;

import com.develatter.infraestructure.config.Configuration;
import com.develatter.presentation.servergui.ServerViewController;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class represents the chat server.
 * It is responsible for starting the server and handling client connections.
 */
public class ChatServer {
    private static int port;
    private final ServerViewController serverViewController;
    private static ServerSocket serverSocket;
    private final Queue<PrintWriter> clientWriters;

    /**
     * Constructor that initializes the server withe the server port provided in the configuration file.
     */
    public ChatServer() {
        port = Integer.parseInt(
                Configuration
                        .getConfiguration()
                        .getProperty("server.port")
        );
        ChatLogger.logf("Server starting...%n");
        serverViewController = new ServerViewController();
        clientWriters = new ConcurrentLinkedQueue<>();
        initServer();
    }

    /**
     * Initializes the server by creating a new ServerSocket a thread that
     * waits for client connections.
     */
    private void initServer() {
        try {
            serverSocket = new ServerSocket(port);
            ChatLogger.logf("Server started on port %d%n", port);
            ChatLogger.logf("Waiting for clients...%n");
            new Thread(() -> {
                while (!serverSocket.isClosed()) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        handleClient(clientSocket);
                    } catch (IOException e) {
                        if (!serverSocket.isClosed()) {
                            ChatLogger.logf("Error accepting client: %s%n", e.getMessage());
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            ChatLogger.logf("Error starting server: %s%n", e.getMessage());
        }
    }

    /**
     * Handles a client connection by creating a new ClientHandler thread.
     * @param clientSocket the client socket.
     */
    private void handleClient(Socket clientSocket) {
        new Thread(
                ClientHandler
                        .builder()
                        .clientSocket(clientSocket)
                        .userService(serverViewController.getUserService())
                        .clientWriters(clientWriters)
                        .build()
        ).start();
    }


    /**
     * Stops the server by closing the ServerSocket.
     */
    public static void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                ChatLogger.logf("Server stopped%n");
            }
        } catch (IOException e) {
            ChatLogger.logf("Error stopping server: %s%n", e.getMessage());
        }
    }

}