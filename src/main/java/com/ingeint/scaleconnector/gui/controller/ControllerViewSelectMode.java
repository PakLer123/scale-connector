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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import com.ingeint.scaleconnector.gui.feature.SCUIFeature;
import com.ingeint.scaleconnector.gui.feature.SCUILocale;
import com.ingeint.scaleconnector.gui.view.ViewSelectMode;

public class ControllerViewSelectMode implements ActionListener, WindowListener, ItemListener {

	private static Logger logger = Logger.getLogger(ControllerViewSelectMode.class.getName());
	private ViewSelectMode viewSelectMode;

	public ControllerViewSelectMode() {
		logger.info("Initializing ControllerViewSelectMode...");
		try {
			viewSelectMode = new ViewSelectMode();
			logger.info("ViewSelectMode initialized successfully");
			viewSelectMode.setVisible(true);
			System.out.println("ViewSelectMode dimensions after setVisible: Width = " +
                    viewSelectMode.getWidth() + ", Height = " + viewSelectMode.getHeight());
		} catch (Exception e) {
			logger.severe("Error initializing ViewSelectMode:" );
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to initialize the user interface.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		viewSelectMode.addListener(this);
		viewSelectMode.setVisible(true);
		logger.info("ControllerViewSelectMode is now visible");
	}

	public void close() {
		logger.info("Closing ViewSelectMode...");
		if(viewSelectMode != null) {
			viewSelectMode.dispose();
		}
		System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		logger.info("Action performed: " + e.getActionCommand());
		if (viewSelectMode == null) {
			logger.severe("viewSelectMode is null. Cannot process actions.");
			return;
		}
		try {
			if (e.getSource().equals(viewSelectMode.getBtnClient())) {
				logger.info("Client Mode activated. ");
				new ControllerViewClientMode();
				viewSelectMode.dispose();
			} else if (e.getSource().equals(viewSelectMode.getBtnServer())) {
				logger.info("Server Mode selected");
				new ControllerViewServerMode();
				logger.info("ControllerViewServerMode Initialized succssfully");
				viewSelectMode.dispose();
			}
		} catch(Exception ex) {
			logger.severe("Error while processing action: " + ex.getMessage());
			ex.printStackTrace();
	        JOptionPane.showMessageDialog(null, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
		logger.info("Window is closing");
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

	@Override
	public void itemStateChanged(ItemEvent ie) {
		logger.info("Locale selection changed.");
		if (viewSelectMode == null || viewSelectMode.getCmbModelSelectLocale() == null) {
            logger.severe("Locale combo box is not initialized.");
            return;
        }
		try {
			String locale = viewSelectMode.getCmbModelSelectLocale().getSelectedItem().toString();
			logger.info("Selected locale: " + locale);
			SCUILocale.load();
			SCUIFeature.set("DEFAULT_LOCALE", locale);
			viewSelectMode.loadLocale();
			try {
				SCUIFeature.save();
				logger.info("Locale settings saved successfully.");
			} catch (Exception ex) {
				logger.info("Unsaved settings");
				logger.warning("Failed to save settings: " );
			}
		} catch (Exception e) {
			logger.severe("Error loading locale" + e.getMessage());
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, SCUILocale.get("ViewSelectMode.errorLocale"), "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

}
