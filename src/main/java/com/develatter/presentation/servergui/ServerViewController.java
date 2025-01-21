package com.develatter.presentation.servergui;

import com.develatter.application.UserService;
import com.develatter.infraestructure.config.Configuration;
import com.develatter.infraestructure.persistence.UserRepositoryImpl;
import com.develatter.infraestructure.server.ChatLogger;
import com.develatter.infraestructure.server.ChatServer;
import com.develatter.presentation.clientgui.ClientViewController;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Controller for the server view
 */
public class ServerViewController {

    private final ServerView view;
    private UserService userService;
    private Thread logThread;
    private final int maxClients;

    /**
     * Constructor that initializes the server view and the user service
     */
    public ServerViewController() {
        view = new ServerView();
        view.setVisible(true);
        maxClients = Integer.parseInt(Configuration
                .getConfiguration()
                .getProperty("server.max_num_users")
        );
        initListeneres();
        initServices();
    }

    /**
     * Initializes the user service and the log service
     */
    private void initServices() {
        userService = new UserService(
                new UserRepositoryImpl()
        );
        initLogService();
    }

    /**
     * Initializes the log service creating a new thread that updates the log area in the view
     */
    private void initLogService() {
        logThread = new Thread(() -> {
            while (!logThread.isInterrupted()) {
                view.getLogArea().setText(ChatLogger.getLog());
                updateConnectedUsers();
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    view.getLogArea().setText("Error: " + e.getMessage());
                }
            }
        });
        logThread.start();
    }

    /**
     * Initializes the listeners for the view
     */
    private void initListeneres() {
        view.getAddClientButton().addActionListener(_ -> {
            new ClientViewController(userService);
            if (userService.getUserList().size() >= maxClients) {
                view.getAddClientButton().setEnabled(false);
            }
        });
        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logThread.interrupt();
                ChatServer.stopServer();
                System.exit(0);
            }
        });
    }


    /**
     * Updates the number of connected users in the view
     */
    public void updateConnectedUsers() {
        view.getConnectedUsers().setText(String.valueOf(userService.getUserList().size()));
        if (userService.getUserList().size() < maxClients) {
            view.getAddClientButton().setEnabled(true);
        }
    }

    /**
     * Gets the user service
     * @return the user service
     */
    public UserService getUserService() {
        return userService;
    }
}
