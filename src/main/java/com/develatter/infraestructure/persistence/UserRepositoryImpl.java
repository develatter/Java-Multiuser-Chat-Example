package com.develatter.infraestructure.persistence;

import com.develatter.domain.User;
import com.develatter.domain.UserRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the UserRepository interface that uses a ConcurrentHashMap to store the users in memory
 */
public class UserRepositoryImpl implements UserRepository {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public boolean userExists(String name) {
        return users.containsKey(name);
    }

    @Override
    public void addUser(User user) {
        users.put(user.getName(), user);
    }

    @Override
    public Map<String, User> getUsers() {
        return new ConcurrentHashMap<>(users);
    }

    @Override
    public void removeUser(String name) {
        users.remove(name);
    }

}