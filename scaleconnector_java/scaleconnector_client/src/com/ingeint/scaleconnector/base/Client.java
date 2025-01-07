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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ingeint.scaleconnector.base.Request;
import com.ingeint.scaleconnector.base.Response;
import com.ingeint.scaleconnector.base.ResponseStatus;

/**
 * Send a request to the Scale Server.
 * 
 * <pre>
 * Client client = new Client(&quot;localhost&quot;, 5000);
 * client.setTimeout(1000);
 * Request request = new Request();
 * request.setType(RequestType.TEST);
 * request.setDate(Calendar.getInstance().getTime());
 * Response response = client.sendRequest(request);
 * </pre>
 */
public class Client {

	private static Logger logger = Logger.getLogger(Client.class.getName());

	private int port;
	private String host;
	private int timeout;
	private boolean isWebService;
	private String protocol;
	private String uri;

	/**
	 * @return Web Service Protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * Set protocol
	 * 
	 * @param protocol
	 *            Web Service Protocol
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return Web Service URI
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Set Web Service URI
	 * 
	 * @param uri
	 *            Web Service URI
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return Port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            Port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return Host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            Host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return Timeout, default 10000
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * Set timeout, default 10000
	 * 
	 * @param timeout
	 *            Timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return True: If the remote host is web service
	 */
	public boolean isWebService() {
		return isWebService;
	}

	/**
	 * Set type server
	 * 
	 * @param isWebService
	 */
	public void setWebService(boolean isWebService) {
		this.isWebService = isWebService;
	}

	/**
	 * @param host
	 *            The Host name
	 * @param port
	 *            The Port number
	 */
	public Client(String host, int port) {
		this.port = port;
		this.host = host;
		timeout = 10000;
		protocol = "http";
		uri = "/scaleconnector_webservice/get_response";
	}

	/**
	 * Sends a request to the server, the request must contain all information
	 * necessary to obtain the desired response
	 * 
	 * @param request
	 *            The request to send
	 * @return The response obtained
	 * @throws SocketTimeoutException
	 *             If timeout expires before connecting
	 * @throws IOException
	 *             If an error occurred when reading from the input stream
	 * @throws ClassNotFoundException
	 *             If the returned object is of a class unknown
	 * @see <a href="http://code.google.com/p/json-simple/">
	 *      http://code.google.com/p/json-simple/</a>
	 */
	public Response sendRequest(Request request) throws SocketTimeoutException, IOException, ClassNotFoundException {
	    logger.info("Connecting to server");
	    Response response = null;
	    if (isWebService) {
	        // Web Service Mode
	        String parameters = "requestType=" + request.getType().toString();
	        Iterator<String> i = request.getParameters().keySet().iterator();
	        while (i.hasNext()) {
	            String p = i.next();
	            parameters += "&" + p + "=" + request.getParameter(p);
	        }

	        String url = String.format("%s://%s:%s%s?%s", protocol, host, port, uri, parameters);
	        URL urlInput = new URL(url);
	        URLConnection con = urlInput.openConnection();
	        con.setConnectTimeout(timeout);
	        con.setReadTimeout(timeout);

	        BufferedReader buffer = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("ISO-8859-1")));
	        String temp;
	        StringBuilder file = new StringBuilder();
	        while ((temp = buffer.readLine()) != null) {
	            file.append(temp);
	        }

	        JSONParser parser = new JSONParser();
	        try {
	            JSONObject obj = (JSONObject) parser.parse(file.toString());
	            JSONObject objData = (JSONObject) parser.parse(obj.get("data").toString());

	            response = new Response();
	            response.setServerMessage(obj.get("serverMessage").toString());
	            response.setDate(Calendar.getInstance().getTime());
	            response.setStatus(ResponseStatus.valueOf(obj.get("status").toString().toUpperCase()));

	            @SuppressWarnings("unchecked")
	            Iterator<String> i2 = objData.keySet().iterator();
	            while (i2.hasNext()) {
	                String key = i2.next();
	                response.addValue(key, objData.get(key).toString());
	            }

	            // Debugging Logs
	            logger.info("Raw Response: " + obj.toJSONString());
	            logger.info("Parsed Response Data: " + objData.toJSONString());
	            logger.info("Response Status: " + response.getStatus());
	            logger.info("Response Server Message: " + response.getServerMessage());
	            logger.info("Response Values: " + response.getData());

	        } catch (ParseException e) {
	            logger.severe("Error creating JSON from response");
	            e.printStackTrace();
	        }
	    } else {
	        // Socket Mode
	        Socket socket = new Socket();
	        socket.connect(new InetSocketAddress(host, port), timeout);
	        socket.setSoTimeout(timeout);

	        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
	        out.writeObject(request);
	        out.flush();
	        logger.info("Data sent");

	        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
	        Object readObject = in.readObject();
	        response = readObject.getClass().equals(Response.class) ? (Response) readObject : null;

	        // Debugging Logs
	        logger.info("Data received");
	        if (response != null) {
	            logger.info("Response Status: " + response.getStatus());
	            logger.info("Response Status Notice: " + response.getStatusNotice());
	            logger.info("Response Server Message: " + response.getServerMessage());
	            logger.info("Response Date: " + response.getDate());

	            // Log all data in the response
	            HashMap<String, String> data = response.getData();
	            if (data != null && !data.isEmpty()) {
	                logger.info("Response Data:");
	                for (Map.Entry<String, String> entry : data.entrySet()) {
	                    logger.info("Key: " + entry.getKey() + ", Value: " + entry.getValue());
	                }
	            } else {
	                logger.warning("Response data is empty.");
	            }

	            // Log specific keys if expected
	            String[] expectedKeys = {"value", "isstable"};
	            for (String key : expectedKeys) {
	                String value = response.getValue(key);
	                if (value != null) {
	                    logger.info("Specific Response Value - Key: " + key + ", Value: " + value);
	                } else {
	                    logger.warning("Specific Response Value - Key: " + key + " is missing or null.");
	                }
	            }
	        } else {
	            logger.warning("Response is null");
	        }

	        out.close();
	        in.close();
	        socket.close();
	    }
	    return response;
	}



}
