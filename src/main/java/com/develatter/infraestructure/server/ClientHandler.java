package com.develatter.infraestructure.server;

import com.develatter.application.UserService;
import com.develatter.domain.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;

/**
 * This class represents a client handler.
 * It is responsible for handling client connections and broadcasting messages to all clients.
 */
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final UserService userService;
    private final Queue<PrintWriter> clientWriters;

    /**
     * Constructor that initializes the client handler with the client socket, client writers and user service.
     *
     * @param b the builder that contains the client socket, client writers and user service.
     */
    private ClientHandler(Builder b) {
        this.clientSocket = b.clientSocket;
        this.userService = b.userService;
        this.clientWriters = b.clientWriters;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            synchronized (userService.getUsers()) {
                clientWriters.add(out);
            }
            String userName = in.readLine();
            String message;
            broadcastMessage(String.format("%s connected.", userName));
            while ((message = in.readLine()) != null) {
                broadcastMessage(message);
            }

            clientSocket.close();

            synchronized (userService.getUsers()) {
                userService.removeUser(userName);
                clientWriters.remove(out);
                broadcastMessage(String.format("%s disconnected.", userName));
            }
        } catch (IOException e) {
            ChatLogger.logf("Error handling client: %s%n", e.getMessage());
        }


    }

    /**
     * Broadcasts a message to all clients and logs the message into the server.
     *
     * @param message the message to broadcast.
     */
    private void broadcastMessage(String message) {
        ChatLogger.logf("%s%n", message);
        synchronized (userService.getUsers()) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }
    }

    /**
     * Creates a new builder for the client handler.
     *
     * @return the builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * This class represents a builder for the client handler.
     */
    public static class Builder {
        private Socket clientSocket;
        private UserService userService;
        private Queue<PrintWriter> clientWriters;

        /**
         * Sets the client socket.
         *
         * @param clientSocket the client socket.
         * @return the builder.
         */
        public Builder clientSocket(Socket clientSocket) {
            this.clientSocket = clientSocket;
            return this;
        }

        /**
         * Sets the user service.
         *
         * @param userService the user service.
         * @return the builder.
         */
        public Builder userService(UserService userService) {
            this.userService = userService;
            return this;
        }

        /**
         * Sets the client writers.
         *
         * @param clientWriters the client writers.
         * @return the builder.
         */
        public Builder clientWriters(Queue<PrintWriter> clientWriters) {
            this.clientWriters = clientWriters;
            return this;
        }

        /**
         * Builds the client handler.
         *
         * @return the client handler.
         */
        public ClientHandler build() {
            if (clientSocket == null) {
                throw new IllegalStateException("clientSocket is required");
            }
            if (userService == null) {
                throw new IllegalStateException("userService is required");
            }

            if (clientWriters == null) {
                throw new IllegalStateException("clientWriters is required");
            }
            return new ClientHandler(this);
        }
    }
}