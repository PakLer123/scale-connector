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

package com.ingeint.scaleconnector.ws.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.ingeint.scaleconnector.base.Request;
import com.ingeint.scaleconnector.base.RequestType;
import com.ingeint.scaleconnector.base.Response;
import com.ingeint.scaleconnector.base.ResponseBuilder;
import com.ingeint.scaleconnector.ws.feature.SCWSFeature;
import com.ingeint.scaleconnector.ws.util.HelperDate;

/**
 * Send response
 * @see Request
 * @see Response
 * @see RequestType
 * @see <a href="http://code.google.com/p/json-simple/">
 *      http://code.google.com/p/json-simple/</a>
 */
@WebServlet("/get_response")
public class GetResponse extends HttpServlet {

	private static final long serialVersionUID = 6774779761546158957L;

	private static final ResponseBuilder rb = new ResponseBuilder();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetResponse() {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response(request, response);
	}

	/**
	 * Send response
	 * 
	 * @param request
	 *            Request
	 * @param response
	 *            Response
	 * @throws ServletException
	 *             If an error occurred connecting
	 * @throws IOException
	 *             If an error occurred when reading from the input stream
	 */
	public void response(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		SCWSFeature.load(getServletContext());

		Request scRequest = buildRequest(request, response);
		Response scResponse = rb.build(scRequest);

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.print(buildJSON(scResponse));
		out.flush();

	}

	/**
	 * Build the JSON from response
	 * 
	 * @param response
	 *            Response
	 * @return JSON
	 * @see <a href="http://code.google.com/p/json-simple/">
	 *      http://code.google.com/p/json-simple/</a>
	 */
	@SuppressWarnings("unchecked")
	public String buildJSON(Response response) {
		HashMap<String, String> data = response.getData();
		Iterator<String> i = data.keySet().iterator();

		JSONObject dataJson = new JSONObject();

		while (i.hasNext()) {
			String parameter = i.next();
			dataJson.put(parameter, data.get(parameter));
		}

		JSONObject resposeJson = new JSONObject();

		resposeJson.put("serverMessage", response.getServerMessage());
		resposeJson.put("status", response.getStatus().toString());
		resposeJson.put("date", response.getDate().toString());
		resposeJson.put("data", dataJson);

		return resposeJson.toJSONString();
	}

	/**
	 * Build request from url query
	 * 
	 * @param request
	 *            Request
	 * @param response
	 *            Response
	 * @return Request
	 * @throws ServletException
	 *             If an error occurred connecting
	 * @throws IOException
	 *             If an error occurred when reading from the input stream
	 */
	public Request buildRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Request scRequest = new Request();
		scRequest.setDate(HelperDate.now());

		if (request.getParameter("requestType") == null) {
			scRequest.setType(RequestType.UNKNOWN);
		} else if (RequestType.FEATURES.toString().equals(request.getParameter("requestType").toUpperCase())) {
			scRequest.setType(RequestType.FEATURES);
		} else if (RequestType.READ_PORT.toString().equals(request.getParameter("requestType").toUpperCase())) {
			scRequest.setType(RequestType.READ_PORT);
		} else if (RequestType.TEST.toString().equals(request.getParameter("requestType").toUpperCase())) {
			scRequest.setType(RequestType.TEST);
		} else {
			scRequest.setType(RequestType.UNKNOWN);
		}

		Iterator<String> iParameters = request.getParameterMap().keySet().iterator();

		while (iParameters.hasNext()) {
			String parameter = iParameters.next();
			scRequest.addParameter(parameter, request.getParameter(parameter).trim());
		}

		if (request.getParameter("baud") != null)
			SCWSFeature.set("DEFAULT_BAUD", request.getParameter("baud").trim());

		if (request.getParameter("stopbits") != null)
			SCWSFeature.set("DEFAULT_STOPBITS", request.getParameter("stopbits").trim());

		if (request.getParameter("parity") != null)
			SCWSFeature.set("DEFAULT_PARITY", request.getParameter("parity").trim());

		if (request.getParameter("databits") != null)
			SCWSFeature.set("DEFAULT_DATABITS", request.getParameter("databits").trim());

		if (request.getParameter("bytecount") != null)
			SCWSFeature.set("DEFAULT_BYTECOUNT", request.getParameter("bytecount").trim());

		if (request.getParameter("startcharacter") != null)
			SCWSFeature.set("DEFAULT_STARTCHARACTER", request.getParameter("startcharacter").trim());

		if (request.getParameter("endcharacter") != null)
			SCWSFeature.set("DEFAULT_ENDCHARACTER", request.getParameter("endcharacter").trim());

		if (request.getParameter("serialport") != null)
			SCWSFeature.set("DEFAULT_SERIALPORT", request.getParameter("serialport").trim());

		if (request.getParameter("readings") != null)
			SCWSFeature.set("DEFAULT_READINGS", request.getParameter("readings").trim());

		if (request.getParameter("stabilitypos") != null)
			SCWSFeature.set("DEFAULT_STABILITYPOS", request.getParameter("stabilitypos").trim());

		if (request.getParameter("stability") != null)
			SCWSFeature.set("DEFAULT_STABILITY", request.getParameter("stability").trim());

		if (request.getParameter("startcut") != null)
			SCWSFeature.set("DEFAULT_STARTCUT", request.getParameter("startcut").trim());

		if (request.getParameter("endcut") != null)
			SCWSFeature.set("DEFAULT_ENDCUT", request.getParameter("endcut").trim());

		if (request.getParameter("floatingpoint") != null)
			SCWSFeature.set("DEFAULT_FLOATINGPOINT", request.getParameter("floatingpoint").trim());

		try {
			SCWSFeature.save();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return scRequest;
	}

}
