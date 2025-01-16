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
 *    - 2015 Saúl Piña <spina@ingeint.com>.
 */

package com.ingeint.scaleconnector.gui.feature;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;

import com.ingeint.scaleconnector.gui.util.HelperFile;

public class SCUILocale {
	private static final String LOCALE_FILE = "locale/en_US.properties";
	private static Properties properties = new Properties();

	public static void load() throws IOException{
		System.out.println("Attempting to load en_US.properties from: " + SCUILocale.class.getClassLoader().getResource("locale/en_US.properties"));
		InputStream input = SCUILocale.class.getClassLoader().getResourceAsStream(LOCALE_FILE);
		if (input != null) {
			properties.load(input);
			input.close();
			System.out.println("Loaded en_US.properties: " + properties);
		} else {
			throw new FileNotFoundException("Locale properties file not found in the classpath: " + LOCALE_FILE);
		}
	}
	

	public static String get(String key) {
		return properties.getProperty(key);
	}

	public static String[] list() {
	    try {
	        System.out.println("Listing locale files...");
	        // Use the class loader to load resources
	        InputStream enLocaleStream = SCUILocale.class.getClassLoader().getResourceAsStream("locale/en_US.properties");
	        if (enLocaleStream != null) {
	            System.out.println("Locale files found in JAR.");
	            return new String[]{"en_US"}; // Hard-code available locales
	        } else {
	            System.out.println("No locale files found.");
	            return new String[0];
	        }
	    } catch (Exception e) {
	        System.err.println("Error listing locale files: " + e.getMessage());
	        e.printStackTrace();
	        return new String[0];
	    }
	}




}
