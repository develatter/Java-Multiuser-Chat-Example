package com.develatter.presentation.clientgui;

import javax.swing.*;
import java.awt.*;

/**
 * View for the client chat
 */
public class ClientView extends JFrame {
    public final String TITLE = "Chat";
    public final int WIDTH = 300;
    public final int HEIGHT = 400;

    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;

    /**
     * Creates a new client view
     */
    public ClientView() {
        initProperties();
        addComponents();
    }

    /**
     * Initializes the properties of the frame
     */
    private void initProperties() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(TITLE);
        setBounds((int) (Math.random() * 15) * 100, (int) (Math.random() * 6) * 100, WIDTH, HEIGHT);
        setLayout(new GridBagLayout());
    }

    /**
     * Adds the components to the frame
     */
    private void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFocusable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);

        messageField = new JTextField();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(messageField, gbc);

        sendButton = new JButton("Enviar");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        add(sendButton, gbc);
    }

    /**
     * Gets the chat area
     * @return the chat area
     */
    public JTextArea getChatArea() {
        return chatArea;
    }

    /**
     * Gets the message field
     * @return the message field
     */
    public JTextField getMessageField() {
        return messageField;
    }

    /**
     * Gets the send button
     * @return the send button
     */
    public JButton getSendButton() {
        return sendButton;
    }


}