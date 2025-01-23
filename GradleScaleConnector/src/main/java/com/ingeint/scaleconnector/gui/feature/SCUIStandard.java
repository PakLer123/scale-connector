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

package com.ingeint.scaleconnector.gui.feature;

import java.awt.Image;
import javax.swing.ImageIcon;

public class SCUIStandard {
    public static final ImageIcon LOGO = loadImageIcon("/image/logo.png");
    public static final Image ICON = loadImage("/image/logo16px.png");

    private static ImageIcon loadImageIcon(String resourcePath) {
        try {
            return new ImageIcon(SCUIStandard.class.getResource(resourcePath));
        } catch (Exception e) {
            System.err.println("Error loading image: " + resourcePath);
            e.printStackTrace();
            return null;
        }
    }

    private static Image loadImage(String resourcePath) {
        try {
            return new ImageIcon(SCUIStandard.class.getResource(resourcePath)).getImage();
        } catch (Exception e) {
            System.err.println("Error loading image: " + resourcePath);
            e.printStackTrace();
            return null;
        }
    }
}

