/**
 * This file is part of Scale Connector.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Copyright (C) 2015 INGEINT <http://www.ingeint.com>.
 * Copyright (C) Contributors.
 * 
 * Contributors:
 *
 */

package com.ingeint.scaleconnector.gui.feature;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

public class SCUIFeature {

    private static Properties properties = new Properties();
    private static final String PROPERTIES_FILE = "features.properties";
    private static final String EXTERNAL_PROPERTIES_FILE = System.getProperty("user.home") + File.separator + PROPERTIES_FILE;
    public static void load() throws IOException {
        File externalFile = new File(EXTERNAL_PROPERTIES_FILE);

        if (externalFile.exists()) {
            // Load from the external properties file
            System.out.println("Loading properties from external file: " + externalFile.getAbsolutePath());
            try (FileInputStream input = new FileInputStream(externalFile)) {
                properties.load(input);
            }
            System.out.println("Loaded properties from external file: " + properties);
        } else {
            // External file does not exist, load from the classpath and create the external file
            System.out.println("External file not found. Attempting to load from classpath and create the external file.");
            InputStream input = SCUIFeature.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            if (input != null) {
                properties.load(input);
                input.close();
                System.out.println("Loaded properties from classpath: " + properties);

                // Save the loaded properties to the external file
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

        System.out.println("Attempting to save properties to file: " + externalFile.getAbsolutePath());
        System.out.println("Properties to save: " + properties);

        if (!externalFile.canWrite()) {
            throw new IOException("File is not writable: " + externalFile.getAbsolutePath());
        }

        try (FileOutputStream fileOutput = new FileOutputStream(externalFile)) {
            properties.store(fileOutput, "SCALE CONNECTOR - External Properties File");
            fileOutput.flush(); // Ensure buffer is flushed
            System.out.println("Properties successfully saved to external file: " + externalFile.getAbsolutePath());
        }

        // Reload to verify changes
        properties.clear();
        try (FileInputStream input = new FileInputStream(externalFile)) {
            properties.load(input);
            System.out.println("Reloaded properties: " + properties);
        }
    }


    private static void saveToExternalFile() throws IOException {
        File externalFile = new File(EXTERNAL_PROPERTIES_FILE);

        try (FileOutputStream fileOutput = new FileOutputStream(externalFile)) {
            properties.store(fileOutput, "SCALE CONNECTOR - Automatically Created External Properties File");
            System.out.println("External properties file created at: " + externalFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error: Failed to create external properties file. Reason: " + e.getMessage());
            throw e;
        }
    }


}
