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

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.ingeint.scaleconnector.base.Client;
import com.ingeint.scaleconnector.base.Request;
import com.ingeint.scaleconnector.base.RequestType;
import com.ingeint.scaleconnector.base.Response;
import com.ingeint.scaleconnector.gui.feature.SCUIFeature;
import com.ingeint.scaleconnector.gui.feature.SCUILocale;
import com.ingeint.scaleconnector.gui.util.HelperDate;
import com.ingeint.scaleconnector.gui.view.ViewAbout;
import com.ingeint.scaleconnector.gui.view.ViewClientMode;
import com.ingeint.scaleconnector.gui.view.ViewWait;

public class ControllerViewClientMode implements ActionListener, WindowListener {

	private ViewClientMode viewClientMode;
	private Client client;
	private Request request;
	private ViewWait viewWait;
	private static Logger logger = Logger.getLogger(ControllerViewClientMode.class.getName());
	private HashMap<String, String> parameters;

	public ControllerViewClientMode() {
		parameters = new HashMap<String, String>();
		viewClientMode = new ViewClientMode();
		viewClientMode.addListener(this);
		initView();
		viewClientMode.setVisible(true);
	}

	public void initView() {
        viewClientMode.getTxtSerialPort().setText(SCUIFeature.get("DEFAULT_SERIALPORT"));
        viewClientMode.getTxtBaud().setText(SCUIFeature.get("DEFAULT_BAUD"));
        viewClientMode.getTxtDataBits().setText(SCUIFeature.get("DEFAULT_DATABITS"));
        viewClientMode.getTxtParity().setText(SCUIFeature.get("DEFAULT_PARITY"));
        viewClientMode.getTxtStopBits().setText(SCUIFeature.get("DEFAULT_STOPBITS"));
        viewClientMode.getTxtPort().setText(SCUIFeature.get("DEFAULT_PORT"));
        viewClientMode.getTxtHost().setText(SCUIFeature.get("DEFAULT_HOST"));
        viewClientMode.getTxtByteCount().setText(SCUIFeature.get("DEFAULT_BYTECOUNT"));
        viewClientMode.getTxtStartCharacter().setText(SCUIFeature.get("DEFAULT_STARTCHARACTER"));
        viewClientMode.getTxtEndCharacter().setText(SCUIFeature.get("DEFAULT_ENDCHARACTER"));
        viewClientMode.getTxtReadings().setText(SCUIFeature.get("DEFAULT_READINGS"));
        viewClientMode.getTxtStartCutPosition().setText(SCUIFeature.get("DEFAULT_STARTCUT"));
        viewClientMode.getTxtEndCutPosition().setText(SCUIFeature.get("DEFAULT_ENDCUT"));
        viewClientMode.getTxtStabilityIndicatorPosition().setText(SCUIFeature.get("DEFAULT_SINDICATORPOS"));
        viewClientMode.getTxtStabilityIndicator().setText(SCUIFeature.get("DEFAULT_SINDICATOR"));
        viewClientMode.getTxtFloatingPoint().setText(SCUIFeature.get("DEFAULT_FPOINT"));
    }

	public void close() {
		viewClientMode.dispose();
		System.exit(0);
	}

	private void sendRequest() {
        viewWait = new ViewWait();

        new Thread(() -> {
            try {
                // Save inputs
                saveInputs();

                parameters.clear();
                populateParameters();

                client = new Client(
                        viewClientMode.getTxtHost().getText(),
                        Integer.parseInt(viewClientMode.getTxtPort().getText())
                );
                client.setWebService(viewClientMode.getCbxWebService().isSelected());

                request = new Request();
                request.setType((RequestType) viewClientMode.getCmbRequestType().getSelectedItem());
                request.setDate(HelperDate.now());
                request.setParameters(parameters);

                Response response = client.sendRequest(request);

                updateResponseView(response);
            } catch (SocketTimeoutException e) {
                logger.warning("Error connection timeout");
                JOptionPane.showMessageDialog(viewClientMode, SCUILocale.get("ViewClientMode.errorTimeOut"), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (Exception e) {
                logger.warning("Error connecting to server");
                JOptionPane.showMessageDialog(viewClientMode, SCUILocale.get("ViewClientMode.errorMessageSend"), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } finally {
                viewWait.close();
            }
        }).start();

        viewWait.display();
    }
	private void saveInputs() {
        SCUIFeature.set("DEFAULT_HOST", viewClientMode.getTxtHost().getText().trim());
        SCUIFeature.set("DEFAULT_PORT", viewClientMode.getTxtPort().getText().trim());
        SCUIFeature.set("DEFAULT_PARITY", viewClientMode.getTxtParity().getText().trim());
        SCUIFeature.set("DEFAULT_STOPBITS", viewClientMode.getTxtStopBits().getText().trim());
        SCUIFeature.set("DEFAULT_DATABITS", viewClientMode.getTxtDataBits().getText().trim());
        SCUIFeature.set("DEFAULT_BAUD", viewClientMode.getTxtBaud().getText().trim());
        SCUIFeature.set("DEFAULT_BYTECOUNT", viewClientMode.getTxtByteCount().getText().trim());
        SCUIFeature.set("DEFAULT_STARTCHARACTER", viewClientMode.getTxtStartCharacter().getText().trim());
        SCUIFeature.set("DEFAULT_ENDCHARACTER", viewClientMode.getTxtEndCharacter().getText().trim());
        SCUIFeature.set("DEFAULT_READINGS", viewClientMode.getTxtReadings().getText().trim());
        SCUIFeature.set("DEFAULT_STARTCUT", viewClientMode.getTxtStartCutPosition().getText().trim());
        SCUIFeature.set("DEFAULT_ENDCUT", viewClientMode.getTxtEndCutPosition().getText().trim());
        SCUIFeature.set("DEFAULT_SINDICATORPOS", viewClientMode.getTxtStabilityIndicatorPosition().getText().trim());
        SCUIFeature.set("DEFAULT_SINDICATOR", viewClientMode.getTxtStabilityIndicator().getText().trim());
        SCUIFeature.set("DEFAULT_FPOINT", viewClientMode.getTxtFloatingPoint().getText().trim());
        SCUIFeature.set("DEFAULT_SERIALPORT", viewClientMode.getTxtSerialPort().getText().trim());
        try {
            SCUIFeature.save();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	private void populateParameters() {
        parameters.put("serialport", SCUIFeature.get("DEFAULT_SERIALPORT"));
        parameters.put("baud", SCUIFeature.get("DEFAULT_BAUD"));
        parameters.put("databits", SCUIFeature.get("DEFAULT_DATABITS"));
        parameters.put("parity", SCUIFeature.get("DEFAULT_PARITY"));
        parameters.put("stopbits", SCUIFeature.get("DEFAULT_STOPBITS"));
        parameters.put("bytecount", SCUIFeature.get("DEFAULT_BYTECOUNT"));

        String startCharacter = SCUIFeature.get("DEFAULT_STARTCHARACTER");
        String endCharacter = SCUIFeature.get("DEFAULT_ENDCHARACTER");
        
        if (startCharacter == null || startCharacter.isEmpty()) {
            startCharacter = "0"; // Default start character
            logger.warning("Start character is empty. Defaulting to: " + startCharacter);
        }
        if (endCharacter == null || endCharacter.isEmpty()) {
            endCharacter = "1"; // Default end character
            logger.warning("End character is empty. Defaulting to: " + endCharacter);
        }

        parameters.put("startcharacter", startCharacter);
        parameters.put("endcharacter", endCharacter);

        logger.info("Start Character: " + parameters.get("startcharacter"));
        logger.info("End Character: " + parameters.get("endcharacter"));

        parameters.put("startcharacter", startCharacter);
        parameters.put("endcharacter", endCharacter);
        parameters.put("readings", SCUIFeature.get("DEFAULT_READINGS"));
        parameters.put("startcut", SCUIFeature.get("DEFAULT_STARTCUT"));
        parameters.put("endcut", SCUIFeature.get("DEFAULT_ENDCUT"));
        parameters.put("stabilitypos", SCUIFeature.get("DEFAULT_SINDICATORPOS"));
        parameters.put("stability", SCUIFeature.get("DEFAULT_SINDICATOR"));
        parameters.put("floatingpoint", SCUIFeature.get("DEFAULT_FPOINT"));
    }
	private void updateResponseView(Response response) {
        viewClientMode.getLblSetResponseServerMessage().setText(response.getServerMessage());
        viewClientMode.getLblSetResponseDate().setText(HelperDate.format(response.getDate(), "yyyy-MM-dd H:mm:ss"));
        viewClientMode.getLblSetResponseStatus().setText(response.getStatus().toString());
        viewClientMode.getLblSetResponseStatusNotice().setText(response.getStatusNotice());

        viewClientMode.getLstModelResponseData().clear();
        Iterator<String> iterator = response.getData().keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            viewClientMode.getLstModelResponseData().addElement(String.format("[%s] = %s", key, response.getValue(key)));
        }
    }

	@Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(viewClientMode.getMenuItemClose())) {
            close();
        } else if (e.getSource().equals(viewClientMode.getMenuItemAbout())) {
            new ViewAbout().setVisible(true);
        } else if (e.getSource().equals(viewClientMode.getMenuItemChangeMode())) {
            viewClientMode.dispose();
            new ControllerViewSelectMode();
        } else if (e.getSource().equals(viewClientMode.getBtnSend())) {
            sendRequest();
        } else if (e.getSource().equals(viewClientMode.getMenuItemDocumentation())) {
            goDocumentation();
        }
    }

	public void goDocumentation() {
		try {
			Desktop.getDesktop().browse(new URI(SCUIFeature.get("DOCUMENTATION")));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(viewClientMode, SCUILocale.get("ViewClientMode.errorGoDocumentation"), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// NOTHING
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// NOTHING
	}

	@Override
	public void windowClosing(WindowEvent e) {
		close();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// NOTHING
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// NOTHING
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// NOTHING
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// NOTHING
	}
}
