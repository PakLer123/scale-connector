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

package com.ingeint.scaleconnector.gui.app;

import java.awt.BorderLayout;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import jline.ConsoleReader;
import com.fazecast.jSerialComm.*;
import com.formdev.flatlaf.FlatLightLaf;

import org.jvnet.substance.SubstanceLookAndFeel;

import com.ingeint.scaleconnector.base.ScaleConnector;
import com.ingeint.scaleconnector.gui.console.ConsoleServerMode;
import com.ingeint.scaleconnector.gui.controller.ControllerViewSelectMode;
import com.ingeint.scaleconnector.gui.feature.SCUIFeature;
import com.ingeint.scaleconnector.gui.feature.SCUILocale;

public class Main {

	private static Logger logger = Logger.getLogger(Main.class.getName());
	
	private static void commandVersion() {
		System.out.println(String.format("%s %s", SCUIFeature.get("APP_NAME"), SCUIFeature.get("VERSION")));
		System.out.println(String.format(SCUIFeature.get("VENDOR")));
		System.out.println(String.format(SCUIFeature.get("WEB")));
		System.out.println();
	}

	private static void commandHelp() {
		System.out.println(String.format("%10s\t%s", "-version", "Displays the version of the application"));
		System.out.println(String.format("%10s\t%s", "-help", "Displays the commands that can be used"));
		System.out.println(String.format("%10s\t%s", "-host", "Displays the values of the server TCP"));
		System.out.println(String.format("%10s\t%s", "-scale", "Displays the values of the scales"));
		System.out.println(String.format("%10s\t%s", "-ports", "Displays the serial ports"));
		System.out.println(String.format("%10s\t%s", "-server", "Application server console starts"));
		System.out.println();
	}

	private static void commandPorts() {
	    SerialPort[] ports = SerialPort.getCommPorts();
	    if (ports.length > 0) {
	        for (SerialPort port : ports) {
	            System.out.println("Port: " + port.getSystemPortName());
	        }
	    } else {
	        System.out.println("No available serial ports.");
	    }
	    System.out.println();
	}

	private static void commandScale() {
		try {
			ConsoleReader console = new ConsoleReader();

			String temp = console.readLine(String.format("Serial Port (%s): ", SCUIFeature.get("DEFAULT_SERIALPORT")));
			if (!temp.trim().isEmpty()) {
				SCUIFeature.set("DEFAULT_SERIALPORT", temp.trim());
			}
			boolean readNext;
			do {
				temp = console.readLine(String.format("Baud (%s): ", SCUIFeature.get("DEFAULT_BAUD")));
				if (temp.trim().isEmpty()) {
					readNext = true;
				} else if (!temp.matches("[0-9]+")) {
					System.out.println("Input must be numbers");
					readNext = false;
				} else {
					readNext = true;
					SCUIFeature.set("DEFAULT_BAUD", temp.trim());
				}
			} while (!readNext);

			do {
				temp = console.readLine(String.format("Data Bits (%s): ", SCUIFeature.get("DEFAULT_DATABITS")));
				if (temp.trim().isEmpty()) {
					readNext = true;
				} else if (!temp.matches("[0-9]+")) {
					System.out.println("Input must be numbers");
					readNext = false;
				} else {
					readNext = true;
					SCUIFeature.set("DEFAULT_DATABITS", temp.trim());
				}
			} while (!readNext);

			do {
				temp = console.readLine(String.format("Stop Bits (%s): ", SCUIFeature.get("DEFAULT_STOPBITS")));
				if (temp.trim().isEmpty()) {
					readNext = true;
				} else if (!temp.matches("[0-9]+")) {
					System.out.println("Input must be numbers");
					readNext = false;
				} else {
					readNext = true;
					SCUIFeature.set("DEFAULT_STOPBITS", temp.trim());
				}
			} while (!readNext);

			do {
				temp = console.readLine(String.format("Parity (%s): ", SCUIFeature.get("DEFAULT_PARITY")));
				if (temp.trim().isEmpty()) {
					readNext = true;
				} else if (!temp.matches("[0-9]+")) {
					System.out.println("Input must be numbers");
					readNext = false;
				} else {
					readNext = true;
					SCUIFeature.set("DEFAULT_PARITY", temp.trim());
				}
			} while (!readNext);

			do {
				temp = console.readLine(String.format("Start Character (%s): ", SCUIFeature.get("DEFAULT_STARTCHARACTER")));
				if (temp.trim().isEmpty()) {
					readNext = true;
				} else if (!temp.matches("[0-9]+")) {
					System.out.println("Input must be numbers");
					readNext = false;
				} else {
					readNext = true;
					SCUIFeature.set("DEFAULT_STARTCHARACTER", temp.trim());
				}
			} while (!readNext);

			do {
				temp = console.readLine(String.format("End Character (%s): ", SCUIFeature.get("DEFAULT_ENDCHARACTER")));
				if (temp.trim().isEmpty()) {
					readNext = true;
				} else if (!temp.matches("[0-9]+")) {
					System.out.println("Input must be numbers");
					readNext = false;
				} else {
					readNext = true;
					SCUIFeature.set("DEFAULT_ENDCHARACTER", temp.trim());
				}
			} while (!readNext);

			do {
				temp = console.readLine(String.format("Byte Count (%s): ", SCUIFeature.get("DEFAULT_BYTECOUNT")));
				if (temp.trim().isEmpty()) {
					readNext = true;
				} else if (!temp.matches("[0-9]+")) {
					System.out.println("Input must be numbers");
					readNext = false;
				} else {
					readNext = true;
					SCUIFeature.set("DEFAULT_BYTECOUNT", temp.trim());
				}
			} while (!readNext);

			do {
				temp = console.readLine(String.format("Readings (%s): ", SCUIFeature.get("DEFAULT_READINGS")));
				if (temp.trim().isEmpty()) {
					readNext = true;
				} else if (!temp.matches("[0-9]+")) {
					System.out.println("Input must be numbers");
					readNext = false;
				} else {
					readNext = true;
					SCUIFeature.set("DEFAULT_READINGS", temp.trim());
				}
			} while (!readNext);

			do {
				temp = console.readLine(String.format("Start Cut Position (%s): ", SCUIFeature.get("DEFAULT_STARTCUT")));
				if (temp.trim().isEmpty()) {
					readNext = true;
				} else if (!temp.matches("[0-9]+")) {
					System.out.println("Input must be numbers");
					readNext = false;
				} else {
					readNext = true;
					SCUIFeature.set("DEFAULT_STARTCUT", temp.trim());
				}
			} while (!readNext);

			do {
				temp = console.readLine(String.format("End Cut Position (%s): ", SCUIFeature.get("DEFAULT_ENDCUT")));
				if (temp.trim().isEmpty()) {
					readNext = true;
				} else if (!temp.matches("[0-9]+")) {
					System.out.println("Input must be numbers");
					readNext = false;
				} else {
					readNext = true;
					SCUIFeature.set("DEFAULT_ENDCUT", temp.trim());
				}
			} while (!readNext);

			do {
				temp = console.readLine(String.format("Stability Position (%s): ", SCUIFeature.get("DEFAULT_SINDICATORPOS")));
				if (temp.trim().isEmpty()) {
					readNext = true;
				} else if (!temp.matches("[0-9]+")) {
					System.out.println("Input must be numbers");
					readNext = false;
				} else {
					readNext = true;
					SCUIFeature.set("DEFAULT_SINDICATORPOS", temp.trim());
				}
			} while (!readNext);

			do {
				temp = console.readLine(String.format("Stability (%s): ", SCUIFeature.get("DEFAULT_SINDICATOR")));
				if (temp.trim().isEmpty()) {
					readNext = true;
				} else if (!temp.matches("[0-9]+")) {
					System.out.println("Input must be numbers");
					readNext = false;
				} else {
					readNext = true;
					SCUIFeature.set("DEFAULT_SINDICATOR", temp.trim());
				}
			} while (!readNext);

			do {
				temp = console.readLine(String.format("Floating Point (%s): ", SCUIFeature.get("DEFAULT_FPOINT")));
				if (temp.trim().isEmpty()) {
					readNext = true;
				} else if (!temp.matches("[0-9]+")) {
					System.out.println("Input must be numbers");
					readNext = false;
				} else {
					readNext = true;
					SCUIFeature.set("DEFAULT_FPOINT", temp.trim());
				}
			} while (!readNext);

			try {
				SCUIFeature.save();
			} catch (Exception e) {
				logger.info("Unsaved settings");
			}

		} catch (IOException e) {
			logger.severe("Error reading data");
			e.printStackTrace();
			System.exit(0);
		}

		int baud = Integer.parseInt(SCUIFeature.get("DEFAULT_BAUD").trim());
		int dataBits = Integer.parseInt(SCUIFeature.get("DEFAULT_DATABITS").trim());
		int stopBits = Integer.parseInt(SCUIFeature.get("DEFAULT_STOPBITS").trim());
		int parity = Integer.parseInt(SCUIFeature.get("DEFAULT_PARITY").trim());
		int byteCount = Integer.parseInt(SCUIFeature.get("DEFAULT_BYTECOUNT").trim());
		int readings = Integer.parseInt(SCUIFeature.get("DEFAULT_READINGS").trim());
		String serialPort = SCUIFeature.get("DEFAULT_SERIALPORT");
		char startCharacter = SCUIFeature.get("DEFAULT_STARTCHARACTER").trim().charAt(0);
		char endCharacter = SCUIFeature.get("DEFAULT_ENDCHARACTER").trim().charAt(0);
		int startCut = Integer.parseInt(SCUIFeature.get("DEFAULT_STARTCUT"));
		int endCut = Integer.parseInt(SCUIFeature.get("DEFAULT_ENDCUT"));
		int sIndicatorPos = Integer.parseInt(SCUIFeature.get("DEFAULT_SINDICATORPOS"));
		int sIndicator = Integer.parseInt(SCUIFeature.get("DEFAULT_SINDICATOR"));
		int fPoint = Integer.parseInt(SCUIFeature.get("DEFAULT_FPOINT"));

		ScaleConnector sc = new ScaleConnector(serialPort, baud, dataBits, stopBits, parity, startCharacter, endCharacter);
		sc.setByteCount(byteCount);
		sc.setStartCharacter(startCharacter);
		sc.setEndCharacter(endCharacter);
		sc.setReadings(readings);
		sc.setStartCutPosition(startCut);
		sc.setEndCutPosition(endCut);
		sc.setStabilityValuePosition(sIndicatorPos);
		sc.setStabilityValue(sIndicator);
		sc.setFloatingPoint(fPoint);
		try {
			System.out.println("Value: " + sc.readValue());
		} catch (Exception e) {
			logger.severe("Error reading serial port");
			e.printStackTrace();
		}
		System.out.println();
	}

	private static void commandHost() {
		String os = SCUIFeature.get("OS");
		System.out.println("Operative system: " + os);
		System.out.println("Port TCP Server: " + SCUIFeature.get("DEFAULT_PORT"));

		try {
			if (os.toLowerCase().contains("windows")) {
				Process p = Runtime.getRuntime().exec("ipconfig /all");
				InputStream out = new BufferedInputStream(p.getInputStream());
				StringBuffer buffer = new StringBuffer();
				int c;
				while ((c = out.read()) != -1) {
					buffer.append((char) c);
				}
				out.close();
				System.out.println(buffer.toString());
			} else if (os.toLowerCase().contains("linux")) {
				System.out.println();
				Process p = Runtime.getRuntime().exec("ifconfig");
				InputStream out = new BufferedInputStream(p.getInputStream());
				StringBuffer buffer = new StringBuffer();
				int c;
				while ((c = out.read()) != -1) {
					buffer.append((char) c);
				}
				out.close();
				System.out.println(buffer.toString());
			} else {
				logger.warning("Unknown operative system to display network configuration");
			}
		} catch (Exception e) {
			logger.severe("Error, command not complete");
			e.printStackTrace();
		}
		System.out.println();
	}

	private static void initServerConsole() {
		new ConsoleServerMode();
	}

	private static void initUI() {
		loadFeaturesUI();
		System.out.println("Initializing UI components...");
		JFrame mainFrame = new JFrame("Scale Connector");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(800, 600);
		mainFrame.setLocationRelativeTo(null);
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		try {
			//SubstanceLookAndFeel.setSkin("org.jvnet.substance.skin.CremeSkin");
			UIManager.setLookAndFeel(new FlatLightLaf());
	        System.out.println("Substance Look-and-Feel applied.");
		} catch(Exception e) {
			System.err.println("Error applying substance Look-and-feel: " + e.getMessage());
			e.printStackTrace();
		}
		new ControllerViewSelectMode();
	}

	private static void loadFeaturesUI() {
		try {
			SCUIFeature.load();
			SCUILocale.load();
		} catch (Exception e) {
			logger.severe("Error loading features");
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error loading features", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	private static void loadFeatures() {
		try {
			SCUIFeature.load();
		} catch (Exception e) {
			logger.severe("Error loading features");
			e.printStackTrace();
			System.exit(0);
		}
	}
	private static void configureLogging() {
	    Logger rootLogger = Logger.getLogger(""); // Root logger
	    rootLogger.setLevel(Level.INFO); // Set global logging level to INFO

	    // Set all handlers to INFO
	    for (java.util.logging.Handler handler : rootLogger.getHandlers()) {
	        handler.setLevel(Level.INFO);
	    }

	    // Suppress specific noisy loggers
	    Logger componentLogger = Logger.getLogger("java.awt.Component");
	    componentLogger.setLevel(Level.WARNING); // Suppress detailed component logs

	    Logger focusManagerLogger = Logger.getLogger("java.awt.DefaultKeyboardFocusManager");
	    focusManagerLogger.setLevel(Level.WARNING); // Suppress focus manager logs

	    Logger substanceLogger = Logger.getLogger("org.jvnet.substance");
	    substanceLogger.setLevel(Level.WARNING); // Suppress Substance Look-and-Feel logs

	    Logger eventDispatchLogger = Logger.getLogger("java.awt.EventDispatchThread");
	    eventDispatchLogger.setLevel(Level.WARNING); // Suppress Event Dispatch logs

	    Logger substanceUtilsLogger = Logger.getLogger("org.jvnet.substance.utils.SubstanceCoreUtilities");
	    substanceUtilsLogger.setLevel(Level.WARNING); // Suppress Substance utilities logs

	    Logger.getLogger(Main.class.getName()).info("Logging configured successfully.");
	}


	public static void main(String[] args) {
		configureLogging();
		loadFeatures();
		if (args.length > 0) {
			loadFeatures();
			for (String arg : args) {
				switch (arg) {
				case "-version":
					commandVersion();
					break;
				case "-server":
					initServerConsole();
					break;
				case "-host":
					commandHost();
					break;
				case "-scale":
					commandScale();
					break;
				case "-ports":
					commandPorts();
					break;
				case "-help":
				default:
					commandHelp();
					break;
				}
			}
		} else {
			initUI();
		}
	}

}
