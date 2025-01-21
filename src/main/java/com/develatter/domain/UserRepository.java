package com.develatter.domain;

import java.util.Map;

/**
 * Repository that manages the users of the chat
 */
public interface UserRepository {
    /**
     * Checks if a user with the specified name exists
     * @param name The name of the user
     * @return true if the user exists, false otherwise
     */
    boolean userExists(String name);

    /**
     * Adds a user to the repository
     * @param user The user to add
     */
    void addUser(User user);

    /**
     * Gets a map of Users using username as a key value
     * @return the map of users
     */
    Map<String, User> getUsers();


    void removeUser(String name);
}