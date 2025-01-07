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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.logging.Logger;

import com.ingeint.scaleconnector.base.Request;
import com.ingeint.scaleconnector.base.Response;
import com.ingeint.scaleconnector.base.ResponseStatus;

/**
 * Running sent of the response
 */
public class ResponseExecutor implements Runnable {

	private static Logger logger = Logger.getLogger(Server.class.getName());

	private Thread thread;
	private Socket socket;
	private Server server;
	private static final ResponseBuilder rb = new ResponseBuilder();

	/**
	 * @return Server
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * Set server
	 * 
	 * @param server
	 *            Server
	 */
	public void setServer(Server server) {
		this.server = server;
	}

	/**
	 * @param socket
	 *            Socket
	 */
	public ResponseExecutor(Socket socket) {
		this.socket = socket;
		thread = new Thread(this);
	}

	/**
	 * Starts the execution
	 */
	public synchronized void start() {
		if (!thread.isAlive()) {
			thread.start();
			logger.info("Response started");
		}
	}

	/**
	 * @return True: Is running, False: Not running
	 */
	public synchronized boolean isAlive() {
		return thread.isAlive();
	}

	@Override
	public void run() {

		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			Object readObject = in.readObject();
			Request request = readObject.getClass().equals(Request.class) ? (Request) readObject : null;
			logger.info("Request received");

			Response response;
			if (request == null) {
				response = new Response();
				response.setDate(Calendar.getInstance().getTime());
				response.setServerMessage("Unsupported RequestType");
				response.setStatus(ResponseStatus.NOT_UNDERSTOOD);
			} else {
				response = rb.build(request);
			}
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(response);
			out.flush();
			logger.info("Response sent");

			in.close();
			out.close();
			socket.close();
			server.removerConnection(this);
		} catch (Exception e) {
			logger.severe("Error sending information to client");
			e.printStackTrace();
		}
	}

}
