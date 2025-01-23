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

package com.ingeint.scaleconnector.base.core;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Response, contains the required information
 */
public class Response implements Serializable {

	private static final long serialVersionUID = -8591190206790041233L;

	private ResponseStatus status;
	private String serverMessage;
	private HashMap<String, String> data;
	private Request request;
	private Date date;

	public Response() {
		data = new HashMap<String, String>();
	}

	/**
	 * Get value
	 * 
	 * @param key
	 *            Value name
	 * @return Value
	 */
	public String getValue(String key) {
		return data.get(key);
	}

	/**
	 * Add value
	 * 
	 * @param key
	 *            Value name
	 * @param value
	 *            Value
	 */
	public void addValue(String key, String value) {
		data.put(key, value);
	}

	/**
	 * @return Notice from the status
	 */
	public String getStatusNotice() {
		return status.notice;
	}

	/**
	 * @return Response status
	 */
	public ResponseStatus getStatus() {
		return status;
	}

	/**
	 * Set status
	 * 
	 * @param status
	 *            ResponseStatus
	 */
	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	/**
	 * @return Message from server
	 */
	public String getServerMessage() {
		return serverMessage;
	}

	/**
	 * Set server message
	 * 
	 * @param serverMessage
	 *            Message
	 */
	public void setServerMessage(String serverMessage) {
		this.serverMessage = serverMessage;
	}

	/**
	 * @return Data
	 */
	public HashMap<String, String> getData() {
		return data;
	}

	/**
	 * Set data
	 * 
	 * @param data
	 *            Data
	 */
	public void setData(HashMap<String, String> data) {
		this.data = data;
	}

	/**
	 * @return Request object
	 */
	public Request getRequest() {
		return request;
	}

	/**
	 * Set request object
	 * 
	 * @param request
	 *            Request
	 */
	public void setRequest(Request request) {
		this.request = request;
	}

	/**
	 * @return Date response
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Set the date
	 * 
	 * @param date
	 *            Date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}
