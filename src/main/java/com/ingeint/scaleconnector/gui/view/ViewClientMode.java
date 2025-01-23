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

package com.ingeint.scaleconnector.gui.view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.ingeint.scaleconnector.base.core.RequestType;
import com.ingeint.scaleconnector.gui.controller.ControllerViewClientMode;
import com.ingeint.scaleconnector.gui.feature.SCUIFeature;
import com.ingeint.scaleconnector.gui.feature.SCUILocale;
import com.ingeint.scaleconnector.gui.feature.SCUIStandard;
import com.ingeint.scaleconnector.gui.util.JIntegerField;

import net.miginfocom.swing.MigLayout;

public class ViewClientMode extends JFrame {

	private static final long serialVersionUID = 6815114238534992691L;

	private List<JButton> buttons;
	private List<JMenuItem> menuItems;
	private JPanel pnlCentral;
	private JPanel pnlLateral;
	private JPanel pnlRequest;
	private JPanel pnlScaleParameters;
	private JPanel pnlResponse;

	private JButton btnSend;

	private JComboBox<RequestType> cmbRequestType;
	private DefaultComboBoxModel<RequestType> cmbModelRequestType;

	private JList<String> lstResponseData;
	private DefaultListModel<String> lstModelResponseData;

	private JMenuBar menuBar;
	private JMenu optionsMenu;
	private JMenu helpMenu;

	private JMenuItem menuItemChangeMode;
	private JMenuItem menuItemClose;
	private JMenuItem menuItemDocumentation;
	private JMenuItem menuItemAbout;

	private JLabel lblLogo;
	private JLabel lblHost;
	private JLabel lblPort;
	private JLabel lblRequestType;
	private JLabel lblListResponseData;
	private JLabel lblResponseServerMessage;
	private JLabel lblResponseStatusNotice;
	private JLabel lblResponseDate;
	private JLabel lblResponseStatus;
	private JLabel lblSetResponseStatus;
	private JLabel lblSetResponseDate;
	private JLabel lblSetResponseStatusNotice;
	private JLabel lblSetResponseServerMessage;
	private JLabel lblWebService;
	private JLabel lblBaud;
	private JLabel lblDataBits;
	private JLabel lblStopBits;
	private JLabel lblParity;
	private JLabel lblStartCharacter;
	private JLabel lblEndCharacter;
	private JLabel lblByteCount;
	private JLabel lblReadings;
	private JLabel lblSerialPort;
	private JLabel lblStartCutPosition;
	private JLabel lblEndCutPosition;
	private JLabel lblStabilityIndicatorPosition;
	private JLabel lblStabilityIndicator;
	private JLabel lblFloatingPoint;

	private JCheckBox cbxWebService;

	private JTextField txtHost;
	private JTextField txtPort;
	private JTextField txtSerialPort;

	private JIntegerField txtBaud;
	private JIntegerField txtDataBits;
	private JIntegerField txtStopBits;
	private JIntegerField txtParity;
	private JTextField txtStartCharacter;
	private JTextField txtEndCharacter;
	private JIntegerField txtByteCount;
	private JIntegerField txtReadings;
	private JIntegerField txtStartCutPosition;
	private JIntegerField txtEndCutPosition;
	private JIntegerField txtStabilityIndicatorPosition;
	private JIntegerField txtStabilityIndicator;
	private JIntegerField txtFloatingPoint;

	public ViewClientMode() {
		// CONFIG
		setLayout(new BorderLayout());
		setIconImage(SCUIStandard.ICON);
		setSize(800, 600);
		setTitle(String.format("%s - %s", SCUIFeature.get("APP_NAME"), SCUILocale.get("ViewClientMode.title")));
		setLocationRelativeTo(this);
		buttons = new ArrayList<JButton>();
		menuItems = new ArrayList<JMenuItem>();

		// MENU
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		optionsMenu = new JMenu(SCUILocale.get("ViewClientMode.optionsMenu"));
		menuBar.add(optionsMenu);

		menuItemChangeMode = new JMenuItem(SCUILocale.get("ViewClientMode.menuItemChangeMode"));
		optionsMenu.add(menuItemChangeMode);

		menuItemClose = new JMenuItem(SCUILocale.get("ViewClientMode.menuItemClose"));
		optionsMenu.add(menuItemClose);

		helpMenu = new JMenu(SCUILocale.get("ViewClientMode.helpMenu"));
		menuBar.add(helpMenu);

		menuItemDocumentation = new JMenuItem(SCUILocale.get("ViewClientMode.menuItemDocumentation"));
		helpMenu.add(menuItemDocumentation);

		menuItemAbout = new JMenuItem(String.format("%s %s", SCUILocale.get("ViewClientMode.menuItemAbout"), SCUIFeature.get("APP_NAME")));
		helpMenu.add(menuItemAbout);

		menuItems.add(menuItemChangeMode);
		menuItems.add(menuItemAbout);
		menuItems.add(menuItemDocumentation);
		menuItems.add(menuItemClose);

		// REQUEST PANEL

		pnlRequest = new JPanel();
		pnlRequest.setLayout(new MigLayout());
		pnlRequest.setBorder(BorderFactory.createTitledBorder(SCUILocale.get("ViewClientMode.pnlRequest")));

		lblHost = new JLabel(SCUILocale.get("ViewClientMode.lblHost"));
		lblPort = new JLabel(SCUILocale.get("ViewClientMode.lblPort"));
		lblRequestType = new JLabel(SCUILocale.get("ViewClientMode.lblRequestType"));

		txtHost = new JTextField();
		txtPort = new JIntegerField();

		cmbModelRequestType = new DefaultComboBoxModel<RequestType>(RequestType.values());
		cmbRequestType = new JComboBox<RequestType>(cmbModelRequestType);

		lblWebService = new JLabel(SCUILocale.get("ViewClientMode.lblWebService"));
		cbxWebService = new JCheckBox();

		pnlRequest.add(lblRequestType, "width 75");
		pnlRequest.add(cmbRequestType, "width 125, wrap");
		pnlRequest.add(lblHost);
		pnlRequest.add(txtHost, "grow, wrap");
		pnlRequest.add(lblPort);
		pnlRequest.add(txtPort, "grow, wrap");
		pnlRequest.add(lblWebService);
		pnlRequest.add(cbxWebService, "grow, wrap");

		// LATERAL PANEL
		pnlLateral = new JPanel();
		pnlLateral.setLayout(new MigLayout());
		add(pnlLateral, BorderLayout.WEST);

		lblLogo = new JLabel();
		lblLogo.setIcon(SCUIStandard.LOGO);
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);

		btnSend = new JButton(SCUILocale.get("ViewClientMode.btnSend"));

		pnlLateral.add(lblLogo, "width 220, wrap");
		pnlLateral.add(pnlRequest, "wrap 20");
		pnlLateral.add(btnSend, "grow, height 30");

		// PARAMETERS PANEL
		pnlScaleParameters = new JPanel();
		pnlScaleParameters.setLayout(new MigLayout());
		pnlScaleParameters.setBorder(BorderFactory.createTitledBorder(SCUILocale.get("ViewClientMode.pnlScaleParameters")));

		lblBaud = new JLabel(SCUILocale.get("ViewServerMode.lblBaud"));
		txtBaud = new JIntegerField();

		lblDataBits = new JLabel(SCUILocale.get("ViewServerMode.lblDataBits"));
		txtDataBits = new JIntegerField();

		lblStopBits = new JLabel(SCUILocale.get("ViewServerMode.lblStopBits"));
		txtStopBits = new JIntegerField();

		lblParity = new JLabel(SCUILocale.get("ViewServerMode.lblParity"));
		txtParity = new JIntegerField();

		lblStartCharacter = new JLabel(SCUILocale.get("ViewServerMode.lblStartCharacter"));
		txtStartCharacter = new JTextField();
		txtStartCharacter.setText("+");

		lblEndCharacter = new JLabel(SCUILocale.get("ViewServerMode.lblEndCharacter"));
		txtEndCharacter = new JTextField();
		txtEndCharacter.setText("B");

		lblByteCount = new JLabel(SCUILocale.get("ViewServerMode.lblByteCount"));
		txtByteCount = new JIntegerField();

		lblReadings = new JLabel(SCUILocale.get("ViewServerMode.lblReadings"));
		txtReadings = new JIntegerField();

		lblSerialPort = new JLabel(SCUILocale.get("ViewServerMode.lblSerialPort"));
		txtSerialPort = new JTextField();

		lblStartCutPosition = new JLabel(SCUILocale.get("ViewServerMode.lblStartCutPosition"));
		txtStartCutPosition = new JIntegerField();

		lblEndCutPosition = new JLabel(SCUILocale.get("ViewServerMode.lblEndCutPosition"));
		txtEndCutPosition = new JIntegerField();

		lblStabilityIndicatorPosition = new JLabel(SCUILocale.get("ViewServerMode.lblStabilityIndicatorPosition"));
		txtStabilityIndicatorPosition = new JIntegerField();

		lblStabilityIndicator = new JLabel(SCUILocale.get("ViewServerMode.lblStabilityIndicator"));
		txtStabilityIndicator = new JIntegerField();

		lblFloatingPoint = new JLabel(SCUILocale.get("ViewServerMode.lblFloatingPoint"));
		txtFloatingPoint = new JIntegerField();

		pnlScaleParameters.add(lblSerialPort, "width 50%");
		pnlScaleParameters.add(txtSerialPort, "width 50%");

		pnlScaleParameters.add(lblByteCount, "width 50%");
		pnlScaleParameters.add(txtByteCount, "width 50%, wrap");

		pnlScaleParameters.add(lblBaud, "grow");
		pnlScaleParameters.add(txtBaud, "grow");
		pnlScaleParameters.add(lblParity, "grow");
		pnlScaleParameters.add(txtParity, "grow, wrap");

		pnlScaleParameters.add(lblDataBits, "grow");
		pnlScaleParameters.add(txtDataBits, "grow");
		pnlScaleParameters.add(lblStopBits, "grow");
		pnlScaleParameters.add(txtStopBits, "grow, wrap");

		pnlScaleParameters.add(lblStartCharacter, "grow");
		pnlScaleParameters.add(txtStartCharacter, "grow");
		pnlScaleParameters.add(lblEndCharacter, "grow");
		pnlScaleParameters.add(txtEndCharacter, "grow, wrap");

		pnlScaleParameters.add(lblStartCutPosition, "grow");
		pnlScaleParameters.add(txtStartCutPosition, "grow");
		pnlScaleParameters.add(lblEndCutPosition, "grow");
		pnlScaleParameters.add(txtEndCutPosition, "grow, wrap");

		pnlScaleParameters.add(lblStabilityIndicatorPosition, "grow");
		pnlScaleParameters.add(txtStabilityIndicatorPosition, "grow");
		pnlScaleParameters.add(lblStabilityIndicator, "grow");
		pnlScaleParameters.add(txtStabilityIndicator, "grow, wrap");

		pnlScaleParameters.add(lblReadings, "grow");
		pnlScaleParameters.add(txtReadings, "grow");
		pnlScaleParameters.add(lblFloatingPoint, "grow");
		pnlScaleParameters.add(txtFloatingPoint, "grow, wrap");

		// RESPONSE PANEL
		pnlResponse = new JPanel();
		pnlResponse.setLayout(new MigLayout());
		pnlResponse.setBorder(BorderFactory.createTitledBorder(SCUILocale.get("ViewClientMode.pnlResponse")));

		lblListResponseData = new JLabel(SCUILocale.get("ViewClientMode.lblListResponseData"));

		lblResponseStatus = new JLabel(SCUILocale.get("ViewClientMode.lblResponseStatus"));
		lblSetResponseStatus = new JLabel();

		lblResponseDate = new JLabel(SCUILocale.get("ViewClientMode.lblResponseDate"));
		lblSetResponseDate = new JLabel();

		lblResponseStatusNotice = new JLabel(SCUILocale.get("ViewClientMode.lblResponseStatusNotice"));
		lblSetResponseStatusNotice = new JLabel();

		lblResponseServerMessage = new JLabel(SCUILocale.get("ViewClientMode.lblResponseServerMessage"));
		lblSetResponseServerMessage = new JLabel();

		lstModelResponseData = new DefaultListModel<String>();
		lstResponseData = new JList<String>(lstModelResponseData);
		JScrollPane scrollResponseData = new JScrollPane(lstResponseData);

		pnlResponse.add(lblResponseStatus, "width 75");
		pnlResponse.add(lblSetResponseStatus, "width 140");
		pnlResponse.add(lblResponseDate, "width 75");
		pnlResponse.add(lblSetResponseDate, "width 140, wrap");
		pnlResponse.add(lblResponseStatusNotice, "width 75");
		pnlResponse.add(lblSetResponseStatusNotice, "grow, span 3, wrap");
		pnlResponse.add(lblResponseServerMessage, "width 75");
		pnlResponse.add(lblSetResponseServerMessage, "grow, span 3, wrap");
		pnlResponse.add(lblListResponseData, "wrap");
		pnlResponse.add(scrollResponseData, "span 4, width 100%, height 100%");

		// CENTRAL PANEL
		pnlCentral = new JPanel();
		pnlCentral.setLayout(new MigLayout());
		add(pnlCentral, BorderLayout.CENTER);

		pnlCentral.add(pnlScaleParameters, "width 100%, height 45%, wrap");
		pnlCentral.add(pnlResponse, "width 100%, height 55%");

		// ADD BUTTONS
		buttons.add(btnSend);
	}

	public void addListener(ControllerViewClientMode listener) {
		for (JButton button : buttons) {
			button.addActionListener(listener);
		}

		for (JMenuItem menuItem : menuItems) {
			menuItem.addActionListener(listener);
		}

		addWindowListener(listener);
	}

	public JMenuItem getMenuItemClose() {
		return menuItemClose;
	}

	public JTextField getTxtHost() {
		return txtHost;
	}

	public JTextField getTxtPort() {
		return txtPort;
	}

	public JComboBox<RequestType> getCmbRequestType() {
		return cmbRequestType;
	}

	public JButton getBtnSend() {
		return btnSend;
	}

	public JLabel getLblSetResponseStatus() {
		return lblSetResponseStatus;
	}

	public JLabel getLblSetResponseDate() {
		return lblSetResponseDate;
	}

	public JLabel getLblSetResponseStatusNotice() {
		return lblSetResponseStatusNotice;
	}

	public JLabel getLblSetResponseServerMessage() {
		return lblSetResponseServerMessage;
	}

	public DefaultListModel<String> getLstModelResponseData() {
		return lstModelResponseData;
	}

	public JMenuItem getMenuItemChangeMode() {
		return menuItemChangeMode;
	}

	public JIntegerField getTxtReadings() {
		return txtReadings;
	}

	public JMenuItem getMenuItemDocumentation() {
		return menuItemDocumentation;
	}

	public JMenuItem getMenuItemAbout() {
		return menuItemAbout;
	}

	public JCheckBox getCbxWebService() {
		return cbxWebService;
	}

	public JIntegerField getTxtBaud() {
		return txtBaud;
	}

	public JIntegerField getTxtDataBits() {
		return txtDataBits;
	}

	public JIntegerField getTxtStopBits() {
		return txtStopBits;
	}

	public JIntegerField getTxtParity() {
		return txtParity;
	}

	public JTextField getTxtStartCharacter() {
		return txtStartCharacter;
	}

	public JTextField getTxtEndCharacter() {
		return txtEndCharacter;
	}

	public JIntegerField getTxtByteCount() {
		return txtByteCount;
	}

	public JTextField getTxtSerialPort() {
		return txtSerialPort;
	}

	public JIntegerField getTxtStartCutPosition() {
		return txtStartCutPosition;
	}

	public JIntegerField getTxtEndCutPosition() {
		return txtEndCutPosition;
	}

	public JIntegerField getTxtStabilityIndicatorPosition() {
		return txtStabilityIndicatorPosition;
	}

	public JIntegerField getTxtStabilityIndicator() {
		return txtStabilityIndicator;
	}

	public JIntegerField getTxtFloatingPoint() {
		return txtFloatingPoint;
	}

}
