package com.develatter.infraestructure.client;

import com.develatter.infraestructure.config.Configuration;
import com.develatter.infraestructure.server.ChatLogger;

import java.io.*;
import java.net.Socket;

/**
 * This class represents a client connection to the server.
 * It stores the client socket and is responsible for sending and receiving messages from the server.
 */
public class ClientConnection implements Closeable, AutoCloseable {
    private final String HOST;
    private final int PORT;
    private BufferedReader in;
    private PrintWriter out;
    private Socket sc;

    /**
     * Constructor that initializes the HOST and PORT from the configuration file and connects to the server.
     * @throws IOException if an I/O error occurs when creating the socket.
     */
    public ClientConnection() throws IOException {
        this.HOST = Configuration
                .getConfiguration()
                .getProperty("server.host");
        this.PORT = Integer
                .parseInt(Configuration
                        .getConfiguration()
                        .getProperty("server.port"));

        connectToServer();
    }

    /**
     * Connects to the server using the HOST and PORT.
     * @throws IOException if an I/O error occurs when creating the socket.
     */
    private void connectToServer() throws IOException {
        sc = new Socket(HOST, PORT);

        in = new BufferedReader(
                new InputStreamReader(
                        sc.getInputStream()
                )
        );
        out = new PrintWriter(
                sc.getOutputStream(),
                true
        );
    }

    /**
     * Sends a message to the server.
     * @param message the message to send.
     */
   public void sendMessage(String message) {
        out.println(message);
    }

    /**
     * Receives a message from the server.
     * @return the message received.
     */
   public String receiveMessage() {
        try {
            if (sc.isClosed()) {
                return null;
            }
            return in.readLine();
        } catch (IOException e) {
            try {
                close();
            } catch (IOException ioException) {
                ChatLogger.logf("Error closing socket: %s%n", ioException.getMessage());
            }
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        if (sc != null && !sc.isClosed()) {
            sc.close();
        }
    }

    /**
     * Checks if the connection is closed.
     * @return true if the connection is closed, false otherwise.
     */
    public boolean isClosed() {
        return sc.isClosed();
    }

    /**
     * Gets the output stream.
     * @return the output stream.
     */
    public PrintWriter getOut() {
        return out;
    }
}