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

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Request to send
 */
public class Request implements Serializable {

	private static final long serialVersionUID = -1326990908589781451L;

	private RequestType type;
	private HashMap<String, String> parameters;
	private Date date;

	public Request() {
		parameters = new HashMap<String, String>();
	}

	/**
	 * Get parameter
	 * 
	 * @param parameter
	 *            Parameter name
	 * @return Parameter value
	 */
	public String getParameter(String parameter) {
		return parameters.get(parameter);
	}

	/**
	 * Add a parameter
	 * 
	 * @param key
	 *            Parameter name
	 * @param parameter
	 *            Parameter value
	 */
	public void addParameter(String key, String parameter) {
		parameters.put(key, parameter);
	}

	/**
	 * @return Request type
	 */
	public RequestType getType() {
		return type;
	}

	/**
	 * Set RequestType
	 * 
	 * @param type
	 *            Request type
	 */
	public void setType(RequestType type) {
		this.type = type;
	}

	/**
	 * @return Parameters
	 */
	public HashMap<String, String> getParameters() {
		return parameters;
	}

	/**
	 * Set parameters
	 * 
	 * @param parameters
	 *            Parameters
	 */
	public void setParameters(HashMap<String, String> parameters) {
		this.parameters = parameters;
	}

	/**
	 * Get date to request
	 * 
	 * @return Date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Set date to requets
	 * 
	 * @param date
	 *            Date request
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}
