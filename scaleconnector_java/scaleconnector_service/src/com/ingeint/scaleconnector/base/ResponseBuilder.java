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

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Logger;

import com.ingeint.scaleconnector.base.Request;
import com.ingeint.scaleconnector.base.Response;
import com.ingeint.scaleconnector.base.ResponseStatus;

import jssc.SerialPortTimeoutException;

/**
 * Build the response
 */
public class ResponseBuilder {
	private static Logger logger = Logger.getLogger(ResponseBuilder.class.getName());

	/**
	 * Creates a response depending on the request
	 * 
	 * @param request
	 *            Request from client
	 * @return Response to send
	 */
	public synchronized Response build(Request request) {
		Response response = new Response();

		switch (request.getType()) {
		case TEST:
			response.setServerMessage("Successful test");
			response.setStatus(ResponseStatus.SUCCESS);
			break;

		case FEATURES:
			HashMap<String, String> data = new HashMap<String, String>();
			Enumeration<Object> features = ScaleConectorInfo.getKeys();
			while (features.hasMoreElements()) {
				String key = (String) features.nextElement();
				data.put(key, ScaleConectorInfo.get(key));
			}
			response.setServerMessage("Server features");
			response.setStatus(ResponseStatus.SUCCESS);
			response.setData(data);
			break;

		case READ_PORT:
		    try {
		        // Parse and validate parameters
		        HashMap<String, String> parameters = request.getParameters();
		        String serialPort = parameters.get("serialport").trim();
		        int baud = Integer.parseInt(parameters.get("baud").trim());
		        int dataBits = Integer.parseInt(parameters.get("databits").trim());
		        int stopBits = Integer.parseInt(parameters.get("stopbits").trim());
		        int parity = Integer.parseInt(parameters.get("parity").trim());
		        int byteCount = Integer.parseInt(parameters.get("bytecount").trim());
		        int readings = Integer.parseInt(parameters.get("readings").trim());
		        int startCut = Integer.parseInt(parameters.get("startcut").trim());
		        int endCut = Integer.parseInt(parameters.get("endcut").trim());
		        int stabilityValue = Integer.parseInt(parameters.get("stability").trim());
		        int stabilityValuePosition = Integer.parseInt(parameters.get("stabilitypos").trim());
		        int floatingPoint = Integer.parseInt(parameters.get("floatingpoint").trim());

		        char startCharText = parameters.get("startcharacter").charAt(0);
		        char endCharText = parameters.get("endcharacter").charAt(0);

		        // Open ScaleConnector
		        ScaleConnector scaleConnector = new ScaleConnector(serialPort, baud, dataBits, stopBits, parity, startCharText, endCharText);
		        scaleConnector.setByteCount(byteCount);
		        scaleConnector.setReadings(readings);
		        scaleConnector.setStartCharacter(startCharText);
		        scaleConnector.setEndCharacter(endCharText);
		        scaleConnector.setStartCutPosition(startCut);
		        scaleConnector.setEndCutPosition(endCut);
		        scaleConnector.setStabilityValuePosition(stabilityValuePosition);
		        scaleConnector.setStabilityValue(stabilityValue);
		        scaleConnector.setFloatingPoint(floatingPoint);

		        // Read value and check stability
		        byte[] rawBytes = scaleConnector.readBytes();
		        logger.info("Raw bytes read: " + Arrays.toString(rawBytes));

		        String value = scaleConnector.readValue();
		        boolean isStable = scaleConnector.isStableValue();

		        if (value.isEmpty()) {
		            response.setServerMessage("No data read from serial port");
		            response.setStatus(ResponseStatus.ERROR);
		        } else {
		            // Prepare response
		            HashMap<String, String> dataResponse = new HashMap<>();
		            dataResponse.put("value", value);
		            dataResponse.put("isstable", Boolean.toString(isStable));
		            dataResponse.put("serialport", serialPort);

		            response.setServerMessage("Query value");
		            response.setStatus(ResponseStatus.SUCCESS);
		            response.setData(dataResponse);
		        }
		    } catch (Exception e) {
		        logger.severe("Error reading serial port: " + e.getMessage());
		        response.setServerMessage("Error reading serial port");
		        response.setStatus(ResponseStatus.ERROR);
		        e.printStackTrace();
		    }
		    break;



		default:
			response.setServerMessage("Unsupported RequestType");
			response.setStatus(ResponseStatus.NOT_UNDERSTOOD);
			break;
		}

		response.setDate(Calendar.getInstance().getTime());
		response.setRequest(request);
		return response;
	}
}
