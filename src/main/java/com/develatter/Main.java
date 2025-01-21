package com.develatter;
import com.develatter.infraestructure.server.ChatServer;

/**
 * An application that simulates a chat server with multiple concurrent clients,
 * designed for educational purposes to practice architectures, design patterns,
 * and the implementation of sockets and threads.
 *
 * @author Alejandro López Martínez
 */
public class Main {
    public static void main(String[] args) {
        new ChatServer();
    }
}