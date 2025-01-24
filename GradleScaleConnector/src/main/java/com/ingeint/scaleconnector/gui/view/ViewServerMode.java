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
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.ingeint.scaleconnector.gui.controller.ControllerViewServerMode;
import com.ingeint.scaleconnector.gui.feature.SCUIFeature;
import com.ingeint.scaleconnector.gui.feature.SCUILocale;
import com.ingeint.scaleconnector.gui.feature.SCUIStandard;
import com.ingeint.scaleconnector.gui.util.JIntegerField;

import net.miginfocom.swing.MigLayout;

public class ViewServerMode extends JFrame {

	private static final long serialVersionUID = -733360274400948603L;

	private List<AbstractButton> buttons;
	private List<JMenuItem> menuItems;
	private JPanel pnlCentral;
	private JPanel pnlWest;
	private JPanel pnlEast;
	private JPanel pnlServer;
	private JPanel pnlScale;
	private JPanel pnlBytesRead;

	private JMenuBar menuBar;
	private JMenu optionsMenu;
	private JMenu helpMenu;

	private JMenuItem menuItemChangeMode;
	private JMenuItem menuItemClose;
	private JMenuItem menuItemAbout;

	private JLabel lblLogo;
	private JLabel lblStatus;
	private JLabel lblPort;
	private JIntegerField txtPort;

	private JLabel lblBaud;
	private JIntegerField txtBaud;

	private JLabel lblDataBits;
	private JIntegerField txtDataBits;

	private JLabel lblStopBits;
	private JIntegerField txtStopBits;

	private JLabel lblParity;
	private JIntegerField txtParity;

	private JLabel lblByteCount;
	private JIntegerField txtByteCount;

	private JLabel lblStartCharacter;
	private JTextField txtStartCharacter;

	private JLabel lblEndCharacter;
	private JTextField txtEndCharacter;

	private JLabel lblSerialPort;
	private JLabel lblPortValue;

	private JComboBox<String> cmbSerialPort;
	private DefaultComboBoxModel<String> cmbModelSerialPort;

	private JButton btnStart;
	private JButton btnStop;
	private JButton btnReadPort;
	
	private JToggleButton btnToggleStream;

	private JTable tblBytesRead;
	private DefaultTableModel tblModelBytesRead;

	private JLabel lblReadings;
	private JIntegerField txtReadings;

	private JLabel lblStartCutPosition;
	private JIntegerField txtStartCutPosition;

	private JLabel lblEndCutPosition;
	private JIntegerField txtEndCutPosition;

	private JLabel lblStabilityIndicatorPosition;
	private JIntegerField txtStabilityIndicatorPosition;

	private JLabel lblStabilityIndicator;
	private JIntegerField txtStabilityIndicator;

	private JLabel lblFloatingPoint;
	private JIntegerField txtFloatingPoint;

	private JLabel lblStatusValue;

	private JMenuItem menuItemDocumentation;
	
	private JLabel lblFullString; // Label for full string
	private JTextField txtFullString; // TextField for displaying the full string


	public ViewServerMode() {
		setLayout(new BorderLayout());
		setIconImage(SCUIStandard.ICON);
		setSize(1400, 600);
		setTitle(String.format("%s - %s", SCUIFeature.get("APP_NAME"), SCUILocale.get("ViewServerMode.title")));
		setLocationRelativeTo(this);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		buttons = new ArrayList<AbstractButton>();
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
		menuItems.add(menuItemDocumentation);
		menuItems.add(menuItemAbout);
		menuItems.add(menuItemClose);

		pnlWest = new JPanel();
		pnlWest.setLayout(new MigLayout());
		add(pnlWest, BorderLayout.WEST);

		lblLogo = new JLabel();
		lblLogo.setIcon(SCUIStandard.LOGO);
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		pnlWest.add(lblLogo, "width 220, wrap");

		pnlServer = new JPanel();
		pnlServer.setLayout(new MigLayout());
		pnlServer.setBorder(BorderFactory.createTitledBorder(SCUILocale.get("ViewServerMode.pnlServer")));

		lblStatus = new JLabel(SCUILocale.get("ViewServerMode.lblStatusStop"));

		pnlWest.add(pnlServer, "width 220, wrap");
		pnlWest.add(lblStatus, "width 220");

		lblPort = new JLabel(SCUILocale.get("ViewServerMode.lblPort"));
		txtPort = new JIntegerField();

		btnStart = new JButton(SCUILocale.get("ViewServerMode.btnStart"));
		btnStop = new JButton(SCUILocale.get("ViewServerMode.btnStop"));
		
		btnToggleStream = new JToggleButton("Start/Stop Streaming");
		
		pnlServer.add(lblPort, "width 50%");
		pnlServer.add(txtPort, "width 50%, wrap 10");
		pnlServer.add(btnStart, "grow, height 30");
		pnlServer.add(btnStop, "grow, height 30");
		
		// CENTRAL PANEL
		pnlCentral = new JPanel();
		pnlCentral.setLayout(new MigLayout());
		add(pnlCentral, BorderLayout.CENTER);

		pnlScale = new JPanel();
		pnlScale.setLayout(new MigLayout());
		pnlScale.setBorder(BorderFactory.createTitledBorder(SCUILocale.get("ViewServerMode.pnlScale")));

		pnlCentral.add(pnlScale, "width 100%, height 100%");

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
		cmbModelSerialPort = new DefaultComboBoxModel<String>();
		cmbSerialPort = new JComboBox<String>(cmbModelSerialPort);

		btnReadPort = new JButton(SCUILocale.get("ViewServerMode.btnReadPort"));

		btnToggleStream = new JToggleButton("Start/Stop Streaming");
		
		lblStatusValue = new JLabel();
		lblStatusValue.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), SCUILocale.get("ViewServerMode.lblStatusValue")));
		lblStatusValue.setFont(new Font(lblStatusValue.getFont().getName(), Font.BOLD, 16));
		lblStatusValue.setHorizontalAlignment(SwingConstants.RIGHT);

		lblPortValue = new JLabel();
		lblPortValue.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), SCUILocale.get("ViewServerMode.lblPortValue")));
		lblPortValue.setFont(new Font(lblPortValue.getFont().getName(), Font.BOLD, 40));
		lblPortValue.setForeground(Color.BLUE);
		lblPortValue.setHorizontalAlignment(SwingConstants.RIGHT);

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
		
		lblFullString = new JLabel("Full String:");
		txtFullString = new JTextField();
		txtFullString.setEditable(false); // Make it read-only
		txtFullString.setColumns(20); // Set appropriate width


		pnlScale.add(lblSerialPort, "width 50%");
		pnlScale.add(cmbSerialPort, "width 50%");

		pnlScale.add(lblByteCount, "width 50%");
		pnlScale.add(txtByteCount, "width 50%, wrap");

		pnlScale.add(lblBaud, "grow");
		pnlScale.add(txtBaud, "grow");
		pnlScale.add(lblParity, "grow");
		pnlScale.add(txtParity, "grow, wrap");

		pnlScale.add(lblDataBits, "grow");
		pnlScale.add(txtDataBits, "grow");
		pnlScale.add(lblStopBits, "grow");
		pnlScale.add(txtStopBits, "grow, wrap");

		pnlScale.add(lblStartCharacter, "grow");
		pnlScale.add(txtStartCharacter, "grow");
		pnlScale.add(lblEndCharacter, "grow");
		pnlScale.add(txtEndCharacter, "grow, wrap");

		pnlScale.add(lblStartCutPosition, "grow");
		pnlScale.add(txtStartCutPosition, "grow");
		pnlScale.add(lblEndCutPosition, "grow");
		pnlScale.add(txtEndCutPosition, "grow, wrap");

		pnlScale.add(lblStabilityIndicatorPosition, "grow");
		pnlScale.add(txtStabilityIndicatorPosition, "grow");
		pnlScale.add(lblStabilityIndicator, "grow");
		pnlScale.add(txtStabilityIndicator, "grow, wrap");

		pnlScale.add(lblReadings, "grow");
		pnlScale.add(txtReadings, "grow");
		pnlScale.add(lblFloatingPoint, "grow");
		pnlScale.add(txtFloatingPoint, "grow, wrap");

		pnlScale.add(btnReadPort, "grow, height 70, span 1 2");
		pnlScale.add(lblPortValue, "span 3, grow,  height 70, wrap");
		pnlScale.add(lblStatusValue, "span 3, grow,  height 40, wrap");
		
		pnlScale.add(btnToggleStream, "grow, span 1 2, wrap, height 40");
		
		pnlScale.add(lblFullString, "grow");
		pnlScale.add(txtFullString, "span 2, grow, wrap");


		// PANEL EAST
		pnlEast = new JPanel();
		pnlEast.setLayout(new MigLayout());
		add(pnlEast, BorderLayout.EAST);

		pnlBytesRead = new JPanel();
		pnlBytesRead.setLayout(new MigLayout());
		pnlBytesRead.setBorder(BorderFactory.createTitledBorder(SCUILocale.get("ViewServerMode.pnlBytesRead")));

		pnlEast.add(pnlBytesRead, "width 230, height 100%");

		// In the ViewServerMode constructor, after initializing the table model and table
		tblModelBytesRead = new DefaultTableModel();
		tblModelBytesRead.setColumnIdentifiers(new Object[] { "POS", "CHAR", "INT", "HEX", "ASCII" }); // Set column identifiers here
		tblBytesRead = new JTable(tblModelBytesRead);
		tblBytesRead.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Disable auto-resize to manually set column widths

		// Set preferred column widths
		tblBytesRead.getColumnModel().getColumn(0).setPreferredWidth(50);  // POS
		tblBytesRead.getColumnModel().getColumn(1).setPreferredWidth(50);  // CHAR
		tblBytesRead.getColumnModel().getColumn(2).setPreferredWidth(50);  // INT
		tblBytesRead.getColumnModel().getColumn(3).setPreferredWidth(50);  // HEX
		tblBytesRead.getColumnModel().getColumn(4).setPreferredWidth(150); // ASCII (make this column wider)

		// Add the table to a scroll pane
		JScrollPane scrollBytesRead = new JScrollPane(tblBytesRead);
		pnlBytesRead.add(scrollBytesRead, "width 100%, height 100%");

		buttons.add(btnReadPort);
		buttons.add(btnStart);
		buttons.add(btnStop);
		buttons.add(btnToggleStream);
	}

	public void addListener(ControllerViewServerMode listener) {
		for (AbstractButton button : buttons) {
			button.addActionListener(listener);
		}

		for (JMenuItem menuItem : menuItems) {
			menuItem.addActionListener(listener);
		}

		addWindowListener(listener);
	}

	public JComboBox<String> getCmbSerialPort() {
		return cmbSerialPort;
	}

	public DefaultComboBoxModel<String> getCmbModelSerialPort() {
		return cmbModelSerialPort;
	}

	public JIntegerField getTxtPort() {
		return txtPort;
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

	public JButton getBtnStart() {
		return btnStart;
	}

	public JButton getBtnStop() {
		return btnStop;
	}

	public JButton getBtnReadPort() {
		return btnReadPort;
	}

	public JMenuItem getMenuItemChangeMode() {
		return menuItemChangeMode;
	}

	public JMenuItem getMenuItemClose() {
		return menuItemClose;
	}

	public JMenuItem getMenuItemAbout() {
		return menuItemAbout;
	}

	public JLabel getLblStatus() {
		return lblStatus;
	}

	public JLabel getLblPortValue() {
		return lblPortValue;
	}

	public JIntegerField getTxtByteCount() {
		return txtByteCount;
	}

	public JTextField getTxtStartCharacter() {
		return txtStartCharacter;
	}

	public JTextField getTxtEndCharacter() {
		return txtEndCharacter;
	}

	public JTable getTblBytesRead() {
		return tblBytesRead;
	}

	public DefaultTableModel getTblModelBytesRead() {
		return tblModelBytesRead;
	}

	public JIntegerField getTxtReadings() {
		return txtReadings;
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

	public JLabel getLblFloatingPoint() {
		return lblFloatingPoint;
	}

	public JIntegerField getTxtFloatingPoint() {
		return txtFloatingPoint;
	}

	public JLabel getLblStatusValue() {
		return lblStatusValue;
	}
	
	public JToggleButton getBtnToggleStream() {
		return btnToggleStream;
	}

	public JMenuItem getMenuItemDocumentation() {
		return menuItemDocumentation;
	}
	
	public JTextField getTxtFullString() {
	    return txtFullString;
	}


	public Iterable<? extends AbstractButton> getButtons() {
		return buttons;
	}

	public Iterable<? extends JMenuItem> getMenuItems() {
		return menuItems;
	}
}
