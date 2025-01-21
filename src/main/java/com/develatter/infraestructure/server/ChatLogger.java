package com.develatter.infraestructure.server;

/**
 * Singleton class that logs messages from the server. It is used to provide a centralized log for the server,
 * which will be shown to the clients in the server GUI.
 */
public class ChatLogger {

    private static StringBuffer serverLog;
    private static ChatLogger instance;

    /**
     * Private constructor to prevent instantiation.
     */
    private ChatLogger() {
        serverLog = new StringBuffer();
    }

    /**
     * Gets the ChatLogger instance. If the instance is null, it creates a new one.
     * @return the ChatLogger instance.
     */
    synchronized public static ChatLogger getChatLogger() {
        if (instance == null) {
            instance = new ChatLogger();
        }
        return instance;
    }

    /**
     * Writes a message to the server log.
     * @param message the message to write.
     */
    private void write(String message) {
        serverLog.append(message);
    }

    /**
     * Writes a formatted message to the server log.
     * @param message the message to write.
     * @param args the arguments to format the message.
     */
    private void writef(String message, Object... args) {
        serverLog.append(String.format(message, args));
    }

    /**
     * Reads the server log.
     * @return the server log.
     */
    private String read() {
        return serverLog.toString();
    }

    /**
     * Logs a message to the server log.
     * @param message the message to log.
     */
    public static void log(String message) {
        getChatLogger().write(message);
    }

    /**
     * Logs a formatted message to the server log.
     * @param message the message to log.
     * @param args the arguments to format the message.
     */
    public static void logf(String message, Object... args) {
        getChatLogger().writef(message, args);
    }

    /**
     * Gets the server log.
     * @return the server log.
     */
    public static String getLog() {
        return getChatLogger().read();
    }

}
