package com.develatter.application;

import com.develatter.domain.exception.UserAlreadyExistsException;
import com.develatter.domain.User;
import com.develatter.domain.UserRepository;
import com.develatter.infraestructure.client.ClientConnection;

import java.util.List;
import java.util.Map;

/**
 * Service that manages the creation and removal of users of the chat
 */
public class UserService {

    private final UserRepository userRepository;

    /**
     * Creates a new UserService with the specified repository
     *
     * @param repository The repository to use
     */
    public UserService(UserRepository repository) {
        userRepository = repository;
    }


    /**
     * Creates a new user with the specified name
     *
     * @param name The name of the user
     * @return the user
     * @throws UserAlreadyExistsException if the user already exists
     */
    public User createUser(String name, ClientConnection cc) throws UserAlreadyExistsException {
        if (userRepository.userExists(name)) throw new UserAlreadyExistsException(name);
        User user = User.createUser(name, cc);
        userRepository.addUser(user);
        return user;
    }

    /**
     * Creates a new user with a default name
     *
     * @param cc The client connection of the user
     * @return the user
     */
    public User createUser(ClientConnection cc) {
        return User.createUser("", cc);
    }

    /**
     * Gets the list of users
     *
     * @return the list of users
     */
    public List<User> getUserList() {
        return userRepository
                .getUsers()
                .values()
                .stream()
                .toList();
    }

    /**
     * Removes the user with the specified name
     *
     * @param name The name of the user to remove
     */
    public void removeUser(String name) {
        userRepository.removeUser(name);
    }

    /**
     * Gets a map of Users using username as a key value
     * @return the map of users
     */
    public Map<String, User> getUsers() {
        return userRepository.getUsers();
    }

}
