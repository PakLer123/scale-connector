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

package com.ingeint.scaleconnector.base;

import java.util.Enumeration;
import java.util.Properties;

/**
 * This class contains information about <b>Scale Connector</b>
 */
public class ScaleConectorInfo {

	public static final String COMPONENT_NAME = "Scale Connector";
	public static final String VENDOR = "INGEINT http://ingeint.com";
	public static final String VERSION = "3.0.0";

	private static Properties properties = new Properties();

	static {
		properties.put("VENDOR", VENDOR);
		properties.put("COMPONENT_NAME", COMPONENT_NAME);
		properties.put("VERSION", VERSION);
		properties.put("OS", System.getProperty("os.name"));
	}

	/**
	 * @param key
	 *            The key whose associated value is to be returned
	 * @return Setting value
	 * @see ScaleConectorInfo#set(String, String)
	 */
	public static String get(String key) {
		return properties.getProperty(key);
	}

	/**
	 * @return An enumeration of the keys in the settings
	 */
	public static Enumeration<Object> getKeys() {
		return properties.keys();
	}

	/**
	 * @param key
	 *            The key whose associated setting value
	 * @param value
	 *            Setting
	 * @see ScaleConectorInfo#get(String)
	 */
	public static void set(String key, String value) {
		properties.put(key, value);
	}

}
