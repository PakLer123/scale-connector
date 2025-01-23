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

package com.ingeint.scaleconnector.gui.console;

import java.util.logging.Logger;

import com.ingeint.scaleconnector.base.util.Server;
import com.ingeint.scaleconnector.gui.feature.SCUIFeature;

import jline.ConsoleReader;

public class ConsoleServerMode {
    private static Logger logger = Logger.getLogger(ConsoleServerMode.class.getName());
    private ConsoleReader console;
    private Server server;
    private String port;

    public ConsoleServerMode() {
        init();
        prompt();
    }

    private void init() {
        try {
            console = new ConsoleReader();
            boolean getPort;
            do {
                port = console.readLine(String.format("Port (%s): ", SCUIFeature.get("DEFAULT_PORT")));
                if (port.trim().isEmpty()) {
                    getPort = false;
                } else if (!port.matches("[0-9]+")) {
                    System.out.println("Input must be numbers");
                    getPort = true;
                } else {
                    getPort = false;
                    SCUIFeature.set("DEFAULT_PORT", port.trim());
                    try {
                        SCUIFeature.save();
                    } catch (Exception e) {
                        logger.info("Unsaved settings");
                    }
                }
            } while (getPort);
        } catch (Exception e) {
            logger.severe("Error reading data");
            e.printStackTrace();
            System.exit(0);
        }
        server = new Server(Integer.parseInt(SCUIFeature.get("DEFAULT_PORT").trim()));
        try {
            server.start();
        } catch (Exception e) {
            logger.severe("Error starting server");
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void prompt() {
        boolean run = true;
        while (run) {
            try {
                switch (console.readLine("")) {
                    case "stop":
                        run = false;
                        try {
                            server.stop();
                        } catch (Exception e) {
                            logger.severe("Error stoping server");
                            e.printStackTrace();
                        }
                        break;
                    case "clear":
                        try {
                            console.clearScreen();
                        } catch (Exception e) {
                            logger.severe("Failure to clear screen");
                            e.printStackTrace();
                        }
                        break;
                    default:
                        logger.warning("Unknown command");
                        break;
                }
            } catch (Exception e) {
                logger.severe("Error reading command");
                e.printStackTrace();
            }
        }
    }
}
