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
    private Thread clientThread;

    /**
     * Constructor that initializes the client view and the user service
     * @param userService the user service
     */
    public ClientViewController(UserService userService) {
        view = new ClientView();
        view.setVisible(true);
        this.userService = userService;
        try {
            showLoginDialog();
        } catch (IOException e) {
            ChatLogger.logf("Error: %s%n", e.getMessage());
        }
        initChatService();
        initListeners();
    }

    /**
     * Shows a dialog to the user to input their name
     */
    public void showLoginDialog() throws IOException{
        String name = JOptionPane.showInputDialog(view, "Enter your name:");
        try {
            connectedUser = userService.createUser(
                    name == null ? "" : name,
                    new ClientConnection()
            );
        } catch (UserAlreadyExistsException e) {
            JOptionPane.showMessageDialog(view, "User already exists, setting default name");
            connectedUser = userService.createUser(new ClientConnection());
        }
        view.setTitle(connectedUser.getName());
    }

    /**
     * Initializes the chat service creatomg a new thread that listens for messages and updates the chat area.
     */
    private void initChatService() {
        clientThread = new Thread(() -> {
            ClientConnection clientConnection = connectedUser.getClientConnection();
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
        view.getSendButton().addActionListener(e -> {
            if (view.getMessageField().getText().isBlank()) {
                return;
            }
            connectedUser.getClientConnection().sendMessage(
                    connectedUser.getName() + ": " + view.getMessageField().getText()
            );
            SwingUtilities.invokeLater(() -> view.getMessageField().setText(""));
        });

        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clientThread.interrupt();
                try {
                    connectedUser.getClientConnection().close();
                } catch (IOException ex) {
                    ChatLogger.logf("Error closing client connection: %s%n", ex.getMessage());
                }
            }
        });
    }


}
