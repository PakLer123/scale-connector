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
				HashMap<String, String> parameters = request.getParameters();

				if (parameters.get("bytecount") == null)
					throw new InvalidParameterException("[bytecount]");
				else if (parameters.get("baud") == null)
					throw new InvalidParameterException("[baud]");
				else if (parameters.get("databits") == null)
					throw new InvalidParameterException("[databits]");
				else if (parameters.get("stopbits") == null)
					throw new InvalidParameterException("[stopbits]");
				else if (parameters.get("parity") == null)
					throw new InvalidParameterException("[parity]");
				else if (parameters.get("serialport") == null)
					throw new InvalidParameterException("[serialport]");
				else if (parameters.get("startcharacter") == null)
					throw new InvalidParameterException("[startcharacter]");
				else if (parameters.get("endcharacter") == null)
					throw new InvalidParameterException("[endcharacter]");
				else if (parameters.get("readings") == null)
					throw new InvalidParameterException("[readings]");
				else if (parameters.get("stabilitypos") == null)
					throw new InvalidParameterException("[stabilitypos]");
				else if (parameters.get("floatingpoint") == null)
					throw new InvalidParameterException("[floatingpoint]");
				else if (parameters.get("stability") == null)
					throw new InvalidParameterException("[stability]");
				else if (parameters.get("startcut") == null)
					throw new InvalidParameterException("[startcut]");
				else if (parameters.get("endcut") == null)
					throw new InvalidParameterException("[endcut]");			

				int baud = Integer.parseInt(parameters.get("baud").toString().trim());
				int dataBits = Integer.parseInt(parameters.get("databits").toString().trim());
				int stopBits = Integer.parseInt(parameters.get("stopbits").toString().trim());
				int parity = Integer.parseInt(parameters.get("parity").toString().trim());
				int byteCount = Integer.parseInt(parameters.get("bytecount").toString().trim());
				int startCharacter = Integer.parseInt(parameters.get("startcharacter").toString().trim());
				int endCharacter = Integer.parseInt(parameters.get("endcharacter").toString().trim());
				int readings = Integer.parseInt(parameters.get("readings").toString().trim());
				String serialPort = parameters.get("serialport").toString().trim();
				int sIndicatorPos = Integer.parseInt(parameters.get("stabilitypos").toString().trim());
				int fPoint = Integer.parseInt(parameters.get("floatingpoint").toString().trim());
				int sIndicator = Integer.parseInt(parameters.get("stability").toString().trim());
				int startCut = Integer.parseInt(parameters.get("startcut").toString().trim());
				int endCut = Integer.parseInt(parameters.get("endcut").toString().trim());

				logger.info(String.format("[serialport=%s,baud=%d,databits=%d,stopbits=%d,parity=%d,startcharacter=%d,endcharacter=%d,startcut=%d,endcut=%d,bytecount=%d,readings=%d,stabilitypos=%d,stability=%d,floatingpoint=%d]", serialPort, baud, dataBits, stopBits, parity, startCharacter,
						endCharacter, startCut, endCut, byteCount, readings, sIndicatorPos, sIndicator, fPoint));

				ScaleConnector sc = new ScaleConnector(serialPort, baud, dataBits, stopBits, parity);
				sc.setByteCount(byteCount);
				sc.setStartCharacter(0);
				sc.setEndCharacter(1);
				sc.setStartCutPosition(startCut);
				sc.setEndCutPosition(endCut);
				sc.setStabilityValuePosition(sIndicatorPos);
				sc.setStabilityValue(sIndicator);
				sc.setFloatingPoint(fPoint);
				sc.setReadings(readings);

				HashMap<String, String> dataResponse = new HashMap<String, String>();
				dataResponse.put("value", sc.readValue());
				dataResponse.put("isstable", Boolean.toString(sc.isStableValue()));
				dataResponse.put("serialport", serialPort);

				response.setServerMessage("Query value");
				response.setStatus(ResponseStatus.SUCCESS);
				response.setData(dataResponse);
			} catch (InvalidParameterException e) {
				logger.severe("Invalid Parameters");
				response.setServerMessage("Invalid Parameters " + e.getMessage());
				response.setStatus(ResponseStatus.ERROR);
				e.printStackTrace();
			} catch (Exception e) {
				logger.severe("Error reading serial port");
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
