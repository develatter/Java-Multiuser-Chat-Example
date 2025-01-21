package com.develatter.infraestructure.config;

import com.develatter.infraestructure.server.ChatLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration class that reads a properties file and provides the properties values to the application.
 * It uses the Singleton pattern to ensure that only one instance of the class is created.
 */
public class Configuration {
    private final Properties PROPERTIES;
    private static Configuration configuration;
    private static final String DEFAULT_CONFIGURATION_FILE = "config.properties";

    /**
     * Private constructor that reads the properties file and loads the properties into the PROPERTIES object.
     * @param configurationFile the name of the properties file to read.
     */
    private Configuration(String configurationFile) {
        PROPERTIES = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configurationFile)) {
            if (input == null) {
                ChatLogger.log("Properties file not found");
                return;
            }
            PROPERTIES.load(input);

        } catch (IOException ex) {
            ChatLogger.logf("Error reading properties file: %s%n", ex.getMessage());
        }
    }

    /**
     * Gets the value of a property from the properties file.
     * @param key the key of the property to get.
     * @return the value of the property.
     */
    public String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    /**
     * Gets the Configuration instance. If the instance is null, it creates a new one.
     * @param configurationFile the name of the properties file to read.
     * @return the Configuration instance.
     */
    synchronized public static Configuration getConfiguration(String configurationFile) {
        if (configuration == null) {
            configuration = new Configuration(configurationFile);
        }
        return configuration;
    }

    /**
     * Gets the Configuration instance using the default properties file. If the instance is null,
     * it creates a new one. The default properties file is "config.properties".
     * @return the Configuration instance.
     */
    synchronized public static Configuration getConfiguration() {
        return getConfiguration(DEFAULT_CONFIGURATION_FILE);
    }
}
