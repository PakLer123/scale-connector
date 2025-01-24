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

package com.ingeint.scaleconnector.base.scaleconnector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortTimeoutException;
import com.fazecast.jSerialComm.*;

import com.ingeint.scaleconnector.base.util.AsciiTranslator;

import javax.swing.*;

/**
 * Reads the serial port of the scale.
 * 
 * To read the serial ports used JSSC,
 * http://code.google.com/p/java-simple-serial-connector/
 * 
 * <pre>
 * ScaleConnector sc = new ScaleConnector(&quot;COM1&quot;, 9600, 8, 1, 0);
 * sc.setByteCount(15);
 * System.out.println(sc.readString());
 * </pre>
 *  
 * @see <a href="http://code.google.com/p/java-simple-serial-connector/">
 *      http://code.google.com/p/java-simple-serial-connector/</a>
 * @see <a
 *      href="http://en.wikibooks.org/wiki/Serial_Programming/RS-232_Connections"
 *      > http://en.wikibooks.org/wiki/Serial_Programming/RS-232_Connections</a>
 * 
 */
public class ScaleConnector {

	private final static Logger log = Logger.getLogger(ScaleConnector.class.getName());

	private int timeout;
	private int baud;
	private int dataBits;
	private int stopBits;
	private int parity;
	private int byteCount;
	private char endCharacter;
	private char startCharacter;
	private String serialPort;
	private int readings;
	private int floatingPoint;
	private boolean isStableValue;
	private int stabilityValue;
	private int stabilityValuePosition;
	private int startCutPosition;
	private int endCutPosition;
	
	private SerialPort port;

	public int getStabilityValue() {
		return stabilityValue;
	}

	public void setStabilityValue(int stabilityValue) {
		this.stabilityValue = stabilityValue;
	}

	public int getFloatingPoint() {
		return floatingPoint;
	}

	public void setFloatingPoint(int floatingPoint) {
		this.floatingPoint = floatingPoint;
	}

	public boolean isStableValue() {
		return isStableValue;
	}

	public void setIsStableValue(boolean isStableValue) {
		this.isStableValue = isStableValue;
	}

	public int getStabilityValuePosition() {
		return stabilityValuePosition;
	}

	public void setStabilityValuePosition(int stabilityValuePosition) {
		this.stabilityValuePosition = stabilityValuePosition;
	}

	public int getStartCutPosition() {
		return startCutPosition;
	}

	public void setStartCutPosition(int startCutPosition) {
		this.startCutPosition = startCutPosition;
	}

	public int getEndCutPosition() {
		return endCutPosition;
	}

	public void setEndCutPosition(int endCutPosition) {
		this.endCutPosition = endCutPosition;
	}

	/**
	 * @return Number of readings
	 */
	public int getReadings() {
		return readings;
	}

	/**
	 * @param readings
	 *
	 */
	public void setReadings(int readings) {
		this.readings = readings;
	}

	/**
	 * @return End Character, default 3
	 */
	public char getEndCharacter() {
		return endCharacter;
	}

	/**
	 * Set the end character to read the value, default 3
	 * 
	 * @param endCharacter
	 */
	public void setEndCharacter(char endCharacter) {
		this.endCharacter = endCharacter;
	}

	/**
	 * @return Start Character, default 2
	 */
	public char getStartCharacter() {
		return startCharacter;
	}

	/**
	 * Set the start character to read the value, default 2
	 * 
	 * @param startCharacter
	 */
	public void setStartCharacter(char startCharacter) {
		this.startCharacter = startCharacter;
	}

	/**
	 * @return Byte count, default 20
	 */
	public int getByteCount() {
		return byteCount;
	}

	/**
	 * Set byte count, number of bytes to read and stored in a buffer, default
	 * 20
	 * 
	 * @param byteCount
	 *            Byte count
	 */
	public void setByteCount(int byteCount) {
		this.byteCount = byteCount;
	}

	/**
	 * @return Timeout, default 10000
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * Set timeout for get port value, default 10000
	 * 
	 * @param timeout
	 *            Timeout
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return Baud
	 */
	public int getBaud() {
		return baud;
	}

	/**
	 * Set communication speed
	 * 
	 * @param baud
	 *            Baud
	 */
	public void setBaud(int baud) {
		this.baud = baud;
	}

	/**
	 * @return Data bits
	 */
	public int getDataBits() {
		return dataBits;
	}

	/**
	 * Set data bits
	 * 
	 * @param dataBits
	 *            Data bits
	 */
	public void setDataBits(int dataBits) {
		this.dataBits = dataBits;
	}

	/**
	 * @return Stop bits
	 */
	public int getStopBits() {
		return stopBits;
	}

	/**
	 * Set stop bits
	 * 
	 * @param stopBits
	 */
	public void setStopBits(int stopBits) {
		this.stopBits = stopBits;
	}

	/**
	 * @return Parity
	 */
	public int getParity() {
		return parity;
	}

	/**
	 * Set parity
	 * 
	 * @param parity
	 *            Parity
	 */
	public void setParity(int parity) {
		this.parity = parity;
	}

	/**
	 * @return Serial port
	 */
	public String getSerialPort() {
		return serialPort;
	}

	/**
	 * Set serial port to connect
	 * 
	 * @param serialPort
	 */
	public void setSerialPort(String serialPort) {
		this.serialPort = serialPort;
	}

	/**
	 * Initial settings connection
	 * 
	 * @param serialPort
	 *            Serial port
	 * @param baud
	 *            Baud rate
	 * @param dataBits
	 *            Data bits
	 * @param stopBits
	 *            Stop bits
	 * @param parity
	 *            Parity
	 */
	public ScaleConnector(String serialPort, int baud, int dataBits, int stopBits, int parity, char startCharacter, char endCharacter) {
		timeout = 5000;
		byteCount = 9;
		this.startCharacter = startCharacter;
		this.endCharacter = endCharacter;
		readings = 1;
		isStableValue = false;
		floatingPoint = 0;
		stabilityValue = '0';
		stabilityValuePosition = 1;
		startCutPosition = 1;
		endCutPosition = 18;
		this.baud = baud;
		this.dataBits = dataBits;
		this.stopBits = stopBits;
		this.parity = parity;
		this.serialPort = serialPort;
		port = SerialPort.getCommPort(serialPort);
        port.setComPortParameters(baud, dataBits, stopBits, parity);
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, timeout, timeout);
        
    }

	/**
	 * Processes the input string to extract a value between the specified start and end characters.
	 *
	 * @param string The input string to process.
	 * @param startCharacter The character indicating the start of the value.
	 * @param endCharacter The character indicating the end of the value.
	 * @return The processed value as a string, or an empty string if the value cannot be extracted.
	 */
	private String getValue(String string, char startCharacter, char endCharacter) {
	    log.info("Input string for processing: " + string);

	    try {
	        // Log configurations
	        log.info("Configured start character: " + (char) startCharacter + " (" + startCharacter + ")");
	        log.info("Configured end character: " + (char) endCharacter + " (" + endCharacter + ")");

	        // Find positions of start and end characters
//	        int startIdx = string.indexOf((char) startCharacter);
//	        int endIdx = string.lastIndexOf((char) endCharacter);
        	
	        int startIdx = string.indexOf((char) 2);
	        int endIdx = string.lastIndexOf((char) 3);
	        
	        log.info("Start character index: " + startIdx);
	        log.info("End character index: " + endIdx);

	        // Validate positions
	        if (startIdx == -1) {
	            log.warning("Start character not found in the input string.");
	            return "";
	        }
	        if (endIdx == -1) {
	            log.warning("End character not found in the input string.");
	            return "";
	        }
	        if (endIdx <= startIdx) {
	            log.warning("End character occurs before start character.");
	            return "";
	        }

	        // Extract substring between start and end characters
	        String raw = string.substring(startIdx, endIdx); // +1 to skip the start character
	        log.info("Raw value after extracting start and end positions: " + raw);

	        // Apply start and end cut positions
	        int startCut = Math.max(0, startCutPosition);
	        int endCut = Math.min(raw.length(), endCutPosition + 1);

	        if (startCut >= endCut) {
	            log.warning("Invalid cut positions. Start cut is greater than or equal to end cut.");
	            return "";
	        }

	        String value = raw.substring(startCut, endCut);
	        log.info("Value after applying cut positions: " + value);

	        // Apply floating-point adjustment if configured
	        if (!value.isEmpty() && floatingPoint > 0) {
	            value = String.valueOf(Double.parseDouble(value) / Math.pow(10, floatingPoint));
	        }

	        // Append stability if needed
	        value = "0" + value;
	        log.info("Processed value: " + value);

	        return value;

	    } catch (Exception e) {
	        log.warning("Error processing value: " + e.getMessage());
	        return "";
	    }
	}

	/**
	 * Read the value from the serial port
	 *
	 * @return Value
	 */
	public synchronized String readValue() {
	    HashMap<String, Integer> frequencyMap = new HashMap<>();
	    log.info("Starting to collect readings...");

	    try {
	        for (int i = 0; i < readings; i++) {
	            String rawValue;
	            try {
	                rawValue = readString(); // Read raw string
	                log.info("Raw string read: " + rawValue);
	            } catch (Exception e) {
	                log.warning("Error reading value from port: " + e.getMessage());
	                continue;
	            }

	            String processedValue = getValue(rawValue, getStartCharacter(), getEndCharacter());
	            log.info("Processed value: " + processedValue);

	            if (processedValue == null || processedValue.isEmpty()) {
	                log.warning("Processed value is empty. Skipping...");
	                continue;
	            }

	            frequencyMap.put(processedValue, frequencyMap.getOrDefault(processedValue, 0) + 1);
	        }

	        // Determine the most frequent value
	        String mostFrequentValue = "";
	        int maxFrequency = 0;

	        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
	            log.info("Value: " + entry.getKey() + ", Frequency: " + entry.getValue());
	            if (entry.getValue() > maxFrequency) {
	                maxFrequency = entry.getValue();
	                mostFrequentValue = entry.getKey();
	            }
	        }
	        System.out.println(!mostFrequentValue.isEmpty());
	        System.out.println(mostFrequentValue.toString());
	        System.out.println(maxFrequency);
	        System.out.println(stabilityValue);
	        System.out.println(maxFrequency == stabilityValue);
	        isStableValue = !mostFrequentValue.isEmpty() && maxFrequency == stabilityValue;
	        return mostFrequentValue.isEmpty() ? "" : mostFrequentValue.substring(1);

	    } catch (Exception e) {
	        log.severe("Unexpected error in readValue: " + e.getMessage());
	        return "";
	    }
	}


	/**
	 * Read string from the serial port
	 * 
	 * @return String
	 * @throws SerialPortException
	 *             If an error occurred when reading from the input stream
	 * @throws SerialPortTimeoutException
	 *             If timeout expires before connecting
	 */
	public synchronized String readString() throws Exception {
		byte[] buffer = readBytes();
		if(buffer.length == 0) {
			System.out.println("No buffer");
			return "";
		}
		return new String(buffer);
	}

	/**
	 * Read bytes from the serial port
	 * 
	 * @return Bytes
	 * @throws SerialPortTimeoutException
	 *             If an error occurred when reading from the input stream
	 * @throws SerialPortException
	 *             If timeout expires before connecting
	 */
	public synchronized byte[] readBytes() throws Exception {
	    SerialPort sp = this.port;
	    if (!sp.isOpen() && !sp.openPort()) {
	        throw new Exception("Unable to open port: " + serialPort);
	    }
	    byte[] buffer = new byte[byteCount];
	    try {
	        // Configure the serial port
	        sp.setBaudRate(baud);
	        sp.setNumDataBits(dataBits);
	        sp.setNumStopBits(stopBits);
	        sp.setParity(parity);
	        sp.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, timeout, timeout);
	        // Open the port if not already open
	        if (!sp.openPort()) {
				JOptionPane.showMessageDialog(null, "Unable to open port: " + serialPort, "Warning", JOptionPane.WARNING_MESSAGE);
	            throw new Exception("Unable to open port: " + serialPort);
	        }
	        int bytesRead = sp.readBytes(buffer, buffer.length);
	        if (bytesRead <= 0) {
	            log.warning("No data read from port.");
				JOptionPane.showMessageDialog(null, "No data read from port.", "Warning", JOptionPane.WARNING_MESSAGE);
	            return new byte[0]; // Return an empty byte array if no data is read
	        }
	        // Log raw data and interpret ASCII control characters
	        log.info("Bytes Read: " + bytesRead);
	        log.info("Raw Data: " + Arrays.toString(buffer));
	        //StringBuilder asciiRepresentation = new StringBuilder("ASCII Interpretation: ");
	        String asciiRepresentation = AsciiTranslator.translate(buffer, bytesRead);
	        log.info(asciiRepresentation);
	        System.out.println("ASCII Representation: " + asciiRepresentation);
	        return Arrays.copyOf(buffer, bytesRead); // Return only the read bytes
	    } catch (Exception e) {
	        log.severe("Error reading bytes: " + e.getMessage());
	        throw e;
	    } finally {
	        if (sp.isOpen()) {
	            sp.closePort();
	        }
	    }
	}
}
