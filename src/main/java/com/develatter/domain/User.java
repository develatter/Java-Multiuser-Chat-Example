package com.develatter.domain;

import com.develatter.infraestructure.client.ClientConnection;

/**
 * Represents a chat user.
 */
public class User {
    private final String NAME;
    private static int count = 0;
    private final ClientConnection clientConnection;

    private User(String name, ClientConnection cc) {
        this.NAME = name;
        clientConnection = cc;
        count++;
    }

    /**
     * Creates a user with the specified name or with a default name in the format "Anon###".
     * Each time a user is created, an internal counter is incremented.
     * @param name The name of the user.
     * @param cc The client connection of the user.
     * @return the user.
     */
    public static User createUser(String name, ClientConnection cc) {
        return new User(name.isBlank() ?
                String.format("%s%d", "Anon", count) :
                name,
                cc
        );
    }

    /**
     * Gets the name of the user.
     * @return the name of the user.
     */
    public String getName() {
        return NAME;
    }

    /**
     * Gets the client connection of the user.
     * @return the client connection of the user.
     */
    public ClientConnection getClientConnection() {
        return clientConnection;
    }
}