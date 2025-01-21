package com.develatter.presentation.clientgui;

import com.develatter.application.UserService;
import com.develatter.domain.User;
import com.develatter.domain.exception.UserAlreadyExistsException;
import com.develatter.infraestructure.client.ClientConnection;
import com.develatter.infraestructure.server.ChatLogger;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Controller for the client view
 */
public class ClientViewController {
    private final ClientView view;
    private User connectedUser;
    private final UserService userService;
    private ClientConnection clientConnection;
    private Thread clientThread;

    /**
     * Constructor that initializes the client view and the user service
     * @param userService the user service
     */
    public ClientViewController(UserService userService) {
        view = new ClientView();
        view.setVisible(true);
        this.userService = userService;
        connectClient();
        showLoginDialog();
        initChatService();
        initListeners();

    }

    /**
     * Connects the client to the server
     */
    private void connectClient() {
        try {
            clientConnection = new ClientConnection();
        } catch (IOException e) {
            ChatLogger.logf("Error connecting to server: %s%n", e.getMessage());
        }
    }

    /**
     * Shows a dialog to the user to input their name
     */
    public void showLoginDialog() {
        String name = JOptionPane.showInputDialog(view, "Enter your name:");
        try {
            connectedUser = userService.createUser(
                    name == null ? "" : name,
                    clientConnection
            );
        } catch (UserAlreadyExistsException e) {
            JOptionPane.showMessageDialog(view, "User already exists, setting default name");
            connectedUser = userService.createUser(clientConnection);
        }

        view.setTitle(connectedUser.getName());
    }

    /**
     * Initializes the chat service creatomg a new thread that listens for messages and updates the chat area.
     */
    private void initChatService() {
        clientThread = new Thread(() -> {
            clientConnection.sendMessage(connectedUser.getName());
            String message = clientConnection.receiveMessage();
            view.getChatArea().append("You are now connected\n");
            while (!clientConnection.isClosed() && !Thread.currentThread().isInterrupted()) {
                message = clientConnection.receiveMessage();
                String finalMessage = message;
                SwingUtilities.invokeLater(() -> view.getChatArea().append(finalMessage + "\n"));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        clientThread.start();
    }

    /**
     * Initializes the listeners for the view
     */
    private void initListeners() {
        view.getSendButton().addActionListener(_ -> {
            if (view.getMessageField().getText().isBlank()) {
                return;
            }
            clientConnection.sendMessage(
                    connectedUser.getName() + ": " + view.getMessageField().getText()
            );
            SwingUtilities.invokeLater(() -> view.getMessageField().setText(""));
        });

        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clientThread.interrupt();
                try {
                    clientConnection.close();
                } catch (IOException ex) {
                    ChatLogger.logf("Error closing client connection: %s%n", ex.getMessage());
                }
            }
        });
    }


}
