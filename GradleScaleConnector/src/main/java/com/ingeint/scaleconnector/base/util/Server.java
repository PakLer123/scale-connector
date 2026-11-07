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

package com.ingeint.scaleconnector.base.util;

import com.ingeint.scaleconnector.base.core.ResponseExecutor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Attend information requests
 * 
 * @see Socket
 * @see ServerSocket
 */
public class Server implements Runnable {

	private static Logger logger = Logger.getLogger(Server.class.getName());

	private int port;
	private ServerSocket serverSocket;
	private volatile boolean run;
	private Thread thread;
	private final List<ResponseExecutor> responses;
	private int timeout;

	/**
	 * @return Port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Set port
	 * 
	 * @param port
	 *            Port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return Timeout, default 1000
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * Set timeout, default 1000
	 * 
	 * @param timeout
	 *            Timeout
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @param port
	 *            Port
	 */
	public Server(int port) {
		this.port = port;
		thread = new Thread(this);
		responses = new ArrayList<>();
		timeout = 1000;
	}

	/**
	 * Starts the execution
	 * 
	 * @throws IOException
	 *             If an error occurred when start server
	 */
	public synchronized void start() throws IOException {
		if (!thread.isAlive()) {
			serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(timeout);
			run = true;
			thread.start();
			logger.info("Server started, port: " + port);
		}
	}

	/**
	 * Stop server
	 * 
	 * @throws InterruptedException
	 *             If an error occurred when stop thread
	 * @throws IOException
	 *             If an error occurred when close server socket
	 */
	public synchronized void stop() throws InterruptedException, IOException {
		if (thread.isAlive()) {
			run = false;
			// Closing the socket unblocks accept(), so the thread can exit right away
			serverSocket.close();
			thread.join(timeout);
			logger.info("Stoping server");
		}
	}

	/**
	 * @return True: Is running, False: Not running
	 */
	public synchronized boolean isAlive() {
		return thread.isAlive();
	}

	/**
	 * Add a response execution
	 * 
	 * @param responseExecutor
	 *            ResponseExecutor
	 */
	public synchronized void addConnection(ResponseExecutor responseExecutor) {
		responses.add(responseExecutor);
		responseExecutor.setServer(this);
	}

	/**
	 * Remove a response execution
	 * 
	 * @param responseExecutor
	 *            ResponseExecutor to remove
	 */
	public synchronized void removerConnection(ResponseExecutor responseExecutor) {
		responses.remove(responseExecutor);
	}

	/**
	 * @return Count active connections
	 */
	public synchronized int getActiveConnections() {
		return responses.size();
	}

	@Override
	public void run() {
		while (run) {
			try {
				Socket socket = serverSocket.accept();
				logger.info(String.format("Connecting to %s", socket.getInetAddress().getHostName()));

				ResponseExecutor responseExecutor = new ResponseExecutor(socket);
				addConnection(responseExecutor);
				responseExecutor.start();

			} catch (SocketTimeoutException ste) {
				// NOTHING
			} catch (Exception e) {
				if (!run) {
					break; // Socket closed by stop()
				}
				logger.warning("Error connecting to client");
				e.printStackTrace();
			}
		}
	}

}
