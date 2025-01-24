package com.ingeint.scaleconnector.gui.feature;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

public class SCUIFeature {

    private static final Logger logger = Logger.getLogger(SCUIFeature.class.getName());
    private static final Properties properties = new Properties();
    private static final String PROPERTIES_FILE = "features.properties";
    private static final String EXTERNAL_PROPERTIES_FILE = System.getProperty("user.home") + File.separator + PROPERTIES_FILE;

    public static void load() throws IOException {
        File externalFile = new File(EXTERNAL_PROPERTIES_FILE);

        if (externalFile.exists()) {
            loadFromExternalFile(externalFile);
        } else {
            loadFromClasspath();
        }
    }

    private static void loadFromExternalFile(File externalFile) throws IOException {
        logger.info("Loading properties from external file: " + externalFile.getAbsolutePath());
        try (FileInputStream input = new FileInputStream(externalFile)) {
            properties.load(input);
        }
        logger.info("Loaded properties from external file: " + properties);
    }

    private static void loadFromClasspath() throws IOException {
        logger.info("External file not found. Attempting to load from classpath and create the external file.");
        try (InputStream input = SCUIFeature.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input != null) {
                properties.load(input);
                logger.info("Loaded properties from classpath: " + properties);
                saveToExternalFile();
            } else {
                throw new FileNotFoundException("Default properties file not found in the classpath: " + PROPERTIES_FILE);
            }
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static Enumeration<Object> getKeys() {
        return properties.keys();
    }

    public static void set(String key, String value) {
        properties.put(key, value);
    }

    public static void save() throws IOException {
        File externalFile = new File(EXTERNAL_PROPERTIES_FILE);
        logger.info("Attempting to save properties to file: " + externalFile.getAbsolutePath());
        logger.info("Properties to save: " + properties);

        if (!externalFile.canWrite()) {
            throw new IOException("File is not writable: " + externalFile.getAbsolutePath());
        }

        try (FileOutputStream fileOutput = new FileOutputStream(externalFile)) {
            properties.store(fileOutput, "SCALE CONNECTOR - External Properties File");
            fileOutput.flush();
            logger.info("Properties successfully saved to external file: " + externalFile.getAbsolutePath());
        }

        reloadProperties(externalFile);
    }

    private static void reloadProperties(File externalFile) throws IOException {
        properties.clear();
        try (FileInputStream input = new FileInputStream(externalFile)) {
            properties.load(input);
            logger.info("Reloaded properties: " + properties);
        }
    }

    private static void saveToExternalFile() throws IOException {
        File externalFile = new File(EXTERNAL_PROPERTIES_FILE);

        try (FileOutputStream fileOutput = new FileOutputStream(externalFile)) {
            properties.store(fileOutput, "SCALE CONNECTOR - Automatically Created External Properties File");
            logger.info("External properties file created at: " + externalFile.getAbsolutePath());
        } catch (IOException e) {
            logger.severe("Error: Failed to create external properties file. Reason: " + e.getMessage());
            throw e;
        }
    }
}