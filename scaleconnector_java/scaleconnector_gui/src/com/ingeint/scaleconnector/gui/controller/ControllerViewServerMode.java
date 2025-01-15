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

package com.ingeint.scaleconnector.gui.controller;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URI;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.logging.LogManager;
import java.util.logging.Level;
import java.util.logging.*;

import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import com.ingeint.scaleconnector.base.ScaleConnector;
import com.ingeint.scaleconnector.base.Server;
import com.ingeint.scaleconnector.gui.feature.SCUIFeature;
import com.ingeint.scaleconnector.gui.feature.SCUILocale;
import com.ingeint.scaleconnector.gui.view.ViewAbout;
import com.ingeint.scaleconnector.gui.view.ViewServerMode;
import com.ingeint.scaleconnector.gui.view.ViewWait;


import com.fazecast.jSerialComm.*;

public class ControllerViewServerMode implements ActionListener, WindowListener {

	private ViewServerMode viewServerMode;
	private Server server;
	private ViewWait viewWait;
	private volatile boolean isStreaming = false;

	private static Logger logger = Logger.getLogger(ControllerViewServerMode.class.getName());

	public ControllerViewServerMode() {
		LogManager.getLogManager().getLogger("").setLevel(Level.INFO);
	    logger.info("Initializing ControllerViewServerMode...");
	    try {
	        viewServerMode = new ViewServerMode();
	        logger.info("ViewServerMode initialized successfully");
	        viewServerMode.addListener(this);
	        initView();
	        logger.info("ViewServerMode components initialized successfully");
	        viewServerMode.setVisible(true);
	        viewServerMode.setResizable(true);
	        logger.info("ControllerViewServerMode fully initialized and visible");
	    } catch (Exception e) {
	        logger.severe("Error initializing ControllerViewServerMode: ");
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Failed to initialize the server mode interface.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

	public void initView() {
	    logger.info("Starting initView()...");
	    try {
	        logger.info("Detecting serial ports using jSerialComm...");
	        logger.info("Calling SerialPort.getCommPorts()...");
	        SerialPort[] ports = SerialPort.getCommPorts();
	        
	        if (ports == null) {
	            logger.severe("SerialPort.getCommPorts() returned null.");
	            return; // Exit if no ports are found
	        }

	        if (ports.length == 0) {
	            logger.warning("No serial ports detected.");
	        } else {
	            logger.info("SerialPort.getCommPorts() returned: " + ports.length + " ports");
	            for (SerialPort port : ports) {
	                logger.info("Adding serial port: " + port.getSystemPortName());
	                viewServerMode.getCmbModelSerialPort().addElement(port.getSystemPortName());
	            }
	        }

	        logger.info("Serial ports added to combo box.");

	        // Load default serial port
	        String defaultPort = SCUIFeature.get("DEFAULT_SERIALPORT");
	        if (defaultPort != null && viewServerMode.getCmbModelSerialPort().getIndexOf(defaultPort) >= 0) {
	            logger.info("Setting default serial port: " + defaultPort);
	            viewServerMode.getCmbModelSerialPort().setSelectedItem(defaultPort);
	        } else {
	            logger.info("No default serial port found or set.");
	        }

	        // Load UI defaults
	        logger.info("Loading other UI defaults...");
	        viewServerMode.getTxtBaud().setText(SCUIFeature.get("DEFAULT_BAUD"));
	        viewServerMode.getTxtDataBits().setText(SCUIFeature.get("DEFAULT_DATABITS"));
	        viewServerMode.getTxtParity().setText(SCUIFeature.get("DEFAULT_PARITY"));
	        viewServerMode.getTxtStopBits().setText(SCUIFeature.get("DEFAULT_STOPBITS"));
	        viewServerMode.getTxtPort().setText(SCUIFeature.get("DEFAULT_PORT"));
	        viewServerMode.getTxtByteCount().setText(SCUIFeature.get("DEFAULT_BYTECOUNT"));
	        viewServerMode.getTxtStartCharacter().setText(SCUIFeature.get("DEFAULT_STARTCHARACTER"));
	        viewServerMode.getTxtEndCharacter().setText(SCUIFeature.get("DEFAULT_ENDCHARACTER"));
	        viewServerMode.getTxtReadings().setText(SCUIFeature.get("DEFAULT_READINGS"));
	        viewServerMode.getTxtStartCutPosition().setText(SCUIFeature.get("DEFAULT_STARTCUT"));
	        viewServerMode.getTxtEndCutPosition().setText(SCUIFeature.get("DEFAULT_ENDCUT"));
	        viewServerMode.getTxtStabilityIndicatorPosition().setText(SCUIFeature.get("DEFAULT_SINDICATORPOS"));
	        viewServerMode.getTxtStabilityIndicator().setText(SCUIFeature.get("DEFAULT_SINDICATOR"));
	        viewServerMode.getTxtFloatingPoint().setText(SCUIFeature.get("DEFAULT_FPOINT"));
	        viewServerMode.getBtnStop().setEnabled(false);

	        viewServerMode.getTblModelBytesRead().setColumnIdentifiers(new Object[] { "POS", "CHAR", "INT", "HEX" });

	        logger.info("UI defaults loaded successfully.");
	    } catch (Exception e) {
	        logger.severe("Error in initView(): ");
	        e.printStackTrace();
	    }
	}



	public void close() {
		if (server != null && server.isAlive()) {
			int option = JOptionPane.showConfirmDialog(viewServerMode, SCUILocale.get("ViewServerMode.changeMode"), SCUILocale.get("ViewClientMode.menuItemChangeMode"), JOptionPane.OK_CANCEL_OPTION);
			if (option != JOptionPane.OK_OPTION) {
				return;
			}
		}
		viewServerMode.dispose();
		System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(viewServerMode.getMenuItemClose())) {
			close();
		} else if (e.getSource().equals(viewServerMode.getMenuItemAbout())) {
			new ViewAbout().setVisible(true);
		} else if (e.getSource().equals(viewServerMode.getMenuItemChangeMode())) {
			changeMode();
		} else if (e.getSource().equals(viewServerMode.getMenuItemDocumentation())) {
			goDocumentation();
		} else if (e.getSource().equals(viewServerMode.getBtnStart())) {
			startServer();
		} else if (e.getSource().equals(viewServerMode.getBtnStop())) {
			stopServer();
		} else if (e.getSource().equals(viewServerMode.getBtnReadPort())) {
			readPort();
		} else if (e.getSource().equals(viewServerMode.getBtnToggleStream())) {
            handleLiveStreamToggle();
        }
	}

	public void goDocumentation() {
		try {
			Desktop.getDesktop().browse(new URI(SCUIFeature.get("DOCUMENTATION")));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(viewServerMode, SCUILocale.get("ViewClientMode.errorGoDocumentation"), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void changeMode() {
		if (server != null && server.isAlive()) {
			int option = JOptionPane.showConfirmDialog(viewServerMode, SCUILocale.get("ViewServerMode.changeMode"), SCUILocale.get("ViewClientMode.menuItemChangeMode"), JOptionPane.OK_CANCEL_OPTION);
			if (option != JOptionPane.OK_OPTION) {
				return;
			}
			stopServer();
		}
		viewServerMode.dispose();
		new ControllerViewSelectMode();
	}


	public synchronized void readPort() {
	    new Thread(new Runnable() {
	        @Override
	        public void run() {
	            ScaleConnector sc = null; // Declare sc outside try
	            try {
	                // Clear previous data in the table
	                viewServerMode.getTblModelBytesRead().getDataVector().removeAllElements();

	                // Gather configuration settings
	                int baud = viewServerMode.getTxtBaud().getInteger();
	                int dataBits = viewServerMode.getTxtDataBits().getInteger();
	                int stopBits = viewServerMode.getTxtStopBits().getInteger();
	                int parity = viewServerMode.getTxtParity().getInteger();
	                int byteCount = viewServerMode.getTxtByteCount().getInteger();
	                int readings = viewServerMode.getTxtReadings().getInteger();
	                String port = viewServerMode.getCmbModelSerialPort().getSelectedItem().toString();

	                // Validate port selection
	                if (port == null || port.isEmpty() || port.equals("No ports available")) {
	                    logger.warning("No valid serial port selected.");
	                    JOptionPane.showMessageDialog(viewServerMode, "Please select a valid serial port.", "Error", JOptionPane.WARNING_MESSAGE);
	                    return;
	                }

	                // Additional settings
	                int sIndicatorPos = viewServerMode.getTxtStabilityIndicatorPosition().getInteger();
	                int fPoint = viewServerMode.getTxtFloatingPoint().getInteger();
	                int sIndicator = viewServerMode.getTxtStabilityIndicator().getInteger();
	                int startCut = viewServerMode.getTxtStartCutPosition().getInteger();
	                int endCut = viewServerMode.getTxtEndCutPosition().getInteger();

	                // Handle start and end characters
	                String startCharText = viewServerMode.getTxtStartCharacter().getText().trim();
	                String endCharText = viewServerMode.getTxtEndCharacter().getText().trim();
                	
	                // Set values to save
					SCUIFeature.set("DEFAULT_PARITY", viewServerMode.getTxtParity().getText().trim());
					SCUIFeature.set("DEFAULT_STOPBITS", viewServerMode.getTxtStopBits().getText().trim());
					SCUIFeature.set("DEFAULT_DATABITS", viewServerMode.getTxtDataBits().getText().trim());
					SCUIFeature.set("DEFAULT_BAUD", viewServerMode.getTxtBaud().getText().trim());
					SCUIFeature.set("DEFAULT_BYTECOUNT", viewServerMode.getTxtByteCount().getText().trim());
					SCUIFeature.set("DEFAULT_STARTCHARACTER", viewServerMode.getTxtStartCharacter().getText().trim());
					SCUIFeature.set("DEFAULT_ENDCHARACTER", viewServerMode.getTxtEndCharacter().getText().trim());
					SCUIFeature.set("DEFAULT_READINGS", viewServerMode.getTxtReadings().getText().trim());
					SCUIFeature.set("DEFAULT_STARTCUT", viewServerMode.getTxtStartCutPosition().getText().trim());
					SCUIFeature.set("DEFAULT_ENDCUT", viewServerMode.getTxtEndCutPosition().getText().trim());
					SCUIFeature.set("DEFAULT_SINDICATORPOS", viewServerMode.getTxtStabilityIndicatorPosition().getText().trim());
					SCUIFeature.set("DEFAULT_SINDICATOR", viewServerMode.getTxtStabilityIndicator().getText().trim());
					SCUIFeature.set("DEFAULT_FPOINT", viewServerMode.getTxtFloatingPoint().getText().trim());
					SCUIFeature.set("DEFAULT_SERIALPORT", port);
					
					// Try to save
	                try {
                		SCUIFeature.save();
                	} catch(Exception ex) {
                		ex.printStackTrace();
                	}
	                
	                // Initialize ScaleConnector
	                sc = new ScaleConnector(port, baud, dataBits, stopBits, parity);
	                sc.setByteCount(byteCount);
	                sc.setReadings(readings);

	                // Default to first and last characters if input is empty
	                byte[] bytesPortValue = sc.readBytes();
	                String portValue = sc.readValue();

	                if (startCharText.isEmpty()) {
	                    startCharText = portValue.substring(0, 1); // First character
	                    logger.info("Start character defaulted to: " + startCharText);
	                }
	                if (endCharText.isEmpty()) {
	                    endCharText = portValue.substring(portValue.length() - 1); // Last character
	                    logger.info("End character defaulted to: " + endCharText);
	                }

	                int startChar = startCharText.charAt(0);
	                int endChar = endCharText.charAt(0);

	                sc.setStartCharacter(startChar);
	                sc.setEndCharacter(endChar);
	                sc.setStartCutPosition(startCut);
	                sc.setEndCutPosition(endCut);
	                sc.setStabilityValuePosition(sIndicatorPos);
	                sc.setStabilityValue(sIndicator);
	                sc.setFloatingPoint(fPoint);

	                // Update UI with data
	                if (sc.isStableValue()) {
	                    viewServerMode.getLblStatusValue().setText(SCUILocale.get("ViewServerMode.STATUS_STABLE"));
	                    viewServerMode.getLblStatusValue().setForeground(Color.GREEN);
	                } else {
	                    viewServerMode.getLblStatusValue().setText(SCUILocale.get("ViewServerMode.STATUS_UNSTABLE"));
	                    viewServerMode.getLblStatusValue().setForeground(Color.RED);
	                }
	                viewServerMode.getLblPortValue().setText(portValue);
	                viewServerMode.getTxtFullString().setText(portValue);

	                for (int i = 0; i < bytesPortValue.length; i++) {
	                    viewServerMode.getTblModelBytesRead().addRow(new Object[] {
	                        i, (char) bytesPortValue[i], bytesPortValue[i], String.format("%02x", bytesPortValue[i]).toUpperCase()
	                    });
	                }
	            } catch (SerialPortTimeoutException e) {
	                logger.severe("Timeout while reading from the scale connector.");
	                JOptionPane.showMessageDialog(viewServerMode, SCUILocale.get("ViewServerMode.timeoutScaleConnector"), "Error", JOptionPane.ERROR_MESSAGE);
	                e.printStackTrace();
	            } catch (Exception e) {
	                logger.severe("Error in readPort(): " + e.getMessage());
	                e.printStackTrace();
	            } 
	        }
	    }).start();
	    
	}
	
	public synchronized void startLiveStream() {
		if(isStreaming) {
			logger.info("Live stream already running.");
			return;
		}
		isStreaming = true;
		new Thread(()-> {
			while(isStreaming) {
				try {
					readPort();
					Thread.sleep(250);
				}catch(InterruptedException e) {
					logger.warning("Live stream interrupted: " + e.getMessage());
					Thread.currentThread().interrupt();
				} catch(Exception e) {
					logger.severe("Error during live stream: " + e.getMessage());
					isStreaming = false;
				}
			}
		}).start();
		logger.info("Live stream started.");
	}
	
	public synchronized void stopLiveStream() {
		isStreaming = false;
		logger.info("Live steam stopped.");
	}
	
	private void handleLiveStreamToggle() {
	    JToggleButton toggleButton = viewServerMode.getBtnToggleStream();

	    if (toggleButton.isSelected()) {
	        toggleButton.setText("Stop Streaming");
	        startLiveStream();
	    } else {
	        toggleButton.setText("Start Streaming");
	        stopLiveStream();
	    }
	}


	private synchronized void stopServer() {
		try {
			server.stop();
			viewServerMode.getBtnStop().setEnabled(false);
			viewServerMode.getBtnStart().setEnabled(true);
			viewServerMode.getLblStatus().setText(SCUILocale.get("ViewServerMode.lblStatusStop"));
		} catch (Exception e1) {
			logger.warning("Error stoping server");
			JOptionPane.showMessageDialog(viewServerMode, SCUILocale.get("ViewServerMode.errorStopingServer"), "Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}

	private synchronized void startServer() {
		server = new Server(viewServerMode.getTxtPort().getInteger());
		SCUIFeature.set("DEFAULT_PORT", viewServerMode.getTxtPort().getText().trim());
		try {
			
			SCUIFeature.save();
		} catch (Exception ex) {

		}
		try {
			server.start();
			viewServerMode.getBtnStop().setEnabled(true);
			viewServerMode.getBtnStart().setEnabled(false);
			viewServerMode.getLblStatus().setText(SCUILocale.get("ViewServerMode.lblStatusStart"));
		} catch (Exception e1) {
			logger.warning("Error starting server");
			JOptionPane.showMessageDialog(viewServerMode, SCUILocale.get("ViewServerMode.errorStartingServer"), "Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		close();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

}
