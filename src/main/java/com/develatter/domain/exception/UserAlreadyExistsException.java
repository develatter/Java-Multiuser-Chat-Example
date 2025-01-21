package com.develatter.domain.exception;

/**
 * Exception thrown when a user already exists in the system
 */
public class UserAlreadyExistsException extends Exception {

    /**
     * Creates a new UserAlreadyExistsException with the specified name
     * @param name The name of the user that already exists
     */
    public UserAlreadyExistsException(String name) {
        super(String.format("El usuario %s ya existe", name));
    }
}
