package com.develatter.presentation.servergui;

import javax.swing.*;
import java.awt.*;

/**
 * View for the server chat
 */
public class ServerView extends JFrame {
    private final String WINDOW_NAME = "Chat App";
    private final int WIDTH = 350;
    private final int HEIGHT = 550;

    private JButton addClient;
    private JTextArea serverLog;
    private JLabel connectedUsers;

    /**
     * Creates a new server view
     */
    public ServerView() {
        initProperties();
        addComponents();
    }

    /**
     * Initializes the properties of the frame
     */
    private void initProperties() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(WINDOW_NAME);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null); //En tol centro
        setResizable(false);
    }

    /**
     * Adds the components to the frame
     */
    private void addComponents() {
        addClient = new JButton("Añadir Chat");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(new JScrollPane(serverLog = new JTextArea()), gbc);
        serverLog.setEditable(false);
        serverLog.setFocusable(false);
        serverLog.setLineWrap(true);
        serverLog.setWrapStyleWord(true);
        gbc.weighty = 0.0;

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(addClient, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel connectedUsersLabel = new JLabel("Usuarios conectados: ");
        connectedUsers = new JLabel("0");
        bottomPanel.add(connectedUsersLabel);
        bottomPanel.add(connectedUsers);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(bottomPanel, gbc);

        add(panel);
    }

    /**
     * Gets the button to add a client
     * @return the button to add a client
     */
    public JButton getAddClientButton() {
        return addClient;
    }

    /**
     * Gets the log area
     * @return the log area
     */
    public JTextArea getLogArea() {
        return serverLog;
    }

    /**
     * Gets the label that shows the number of connected users
     * @return the label that shows the number of connected users
     */
    public JLabel getConnectedUsers() {
        return connectedUsers;
    }
}
