package com.ingeint.scaleconnector.gui.app;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import jline.ConsoleReader;

import com.fazecast.jSerialComm.*;

import com.ingeint.scaleconnector.base.scaleconnector.ScaleConnector;
import com.ingeint.scaleconnector.gui.console.ConsoleServerMode;
import com.ingeint.scaleconnector.gui.controller.ControllerViewSelectMode;
import com.ingeint.scaleconnector.gui.feature.SCUIFeature;
import com.ingeint.scaleconnector.gui.feature.SCUILocale;
import com.ingeint.scaleconnector.gui.theme.CustomFlatLaf;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    static void commandVersion() {
        System.out.printf("%s %s%n", SCUIFeature.get("APP_NAME"), SCUIFeature.get("VERSION"));
        System.out.printf("%s%n", SCUIFeature.get("VENDOR"));
        System.out.printf("%s%n%n", SCUIFeature.get("WEB"));
    }

    static void commandHelp() {
        System.out.printf("%10s\t%s%n", "-version", "Displays the version of the application");
        System.out.printf("%10s\t%s%n", "-help", "Displays the commands that can be used");
        System.out.printf("%10s\t%s%n", "-host", "Displays the values of the server TCP");
        System.out.printf("%10s\t%s%n", "-scale", "Displays the values of the scales");
        System.out.printf("%10s\t%s%n", "-ports", "Displays the serial ports");
        System.out.printf("%10s\t%s%n", "-server", "Application server console starts");
        System.out.println();
    }

    static void commandPorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports.length > 0) {
            for (SerialPort port : ports) {
                System.out.println("Port: " + port.getSystemPortName());
            }
        } else {
            System.out.println("No available serial ports.");
        }
        System.out.println();
    }

    static void commandScale() {
        ConsoleReader console = null;
        try {
            console = new ConsoleReader();
            updateFeature(console, "DEFAULT_SERIALPORT", "Serial Port");
            updateFeature(console, "DEFAULT_BAUD", "Baud");
            updateFeature(console, "DEFAULT_DATABITS", "Data Bits");
            updateFeature(console, "DEFAULT_STOPBITS", "Stop Bits");
            updateFeature(console, "DEFAULT_PARITY", "Parity");
            updateFeature(console, "DEFAULT_STARTCHARACTER", "Start Character");
            updateFeature(console, "DEFAULT_ENDCHARACTER", "End Character");
            updateFeature(console, "DEFAULT_BYTECOUNT", "Byte Count");
            updateFeature(console, "DEFAULT_READINGS", "Readings");
            updateFeature(console, "DEFAULT_STARTCUT", "Start Cut Position");
            updateFeature(console, "DEFAULT_ENDCUT", "End Cut Position");
            updateFeature(console, "DEFAULT_SINDICATORPOS", "Stability Position");
            updateFeature(console, "DEFAULT_SINDICATOR", "Stability");
            updateFeature(console, "DEFAULT_FPOINT", "Floating Point");

            SCUIFeature.save();

            ScaleConnector sc = createScaleConnector();
            System.out.println("Value: " + sc.readValue());
        } catch (IOException e) {
            logger.severe("Error reading data");
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            logger.severe("Error reading serial port");
            e.printStackTrace();
        } finally {
            if (console != null) {
                // No need to close ConsoleReader
            }
        }
        System.out.println();
    }

    private static void updateFeature(ConsoleReader console, String key, String prompt) throws IOException {
        String temp;
        boolean readNext;
        do {
            temp = console.readLine(String.format("%s (%s): ", prompt, SCUIFeature.get(key)));
            if (temp.trim().isEmpty()) {
                readNext = true;
            } else if (!temp.matches("[0-9]+")) {
                System.out.println("Input must be numbers");
                readNext = false;
            } else {
                readNext = true;
                SCUIFeature.set(key, temp.trim());
            }
        } while (!readNext);
    }

    private static ScaleConnector createScaleConnector() {
        int baud = Integer.parseInt(SCUIFeature.get("DEFAULT_BAUD").trim());
        int dataBits = Integer.parseInt(SCUIFeature.get("DEFAULT_DATABITS").trim());
        int stopBits = Integer.parseInt(SCUIFeature.get("DEFAULT_STOPBITS").trim());
        int parity = Integer.parseInt(SCUIFeature.get("DEFAULT_PARITY").trim());
        int byteCount = Integer.parseInt(SCUIFeature.get("DEFAULT_BYTECOUNT").trim());
        int readings = Integer.parseInt(SCUIFeature.get("DEFAULT_READINGS").trim());
        String serialPort = SCUIFeature.get("DEFAULT_SERIALPORT");
        char startCharacter = SCUIFeature.get("DEFAULT_STARTCHARACTER").trim().charAt(0);
        char endCharacter = SCUIFeature.get("DEFAULT_ENDCHARACTER").trim().charAt(0);
        int startCut = Integer.parseInt(SCUIFeature.get("DEFAULT_STARTCUT"));
        int endCut = Integer.parseInt(SCUIFeature.get("DEFAULT_ENDCUT"));
        int sIndicatorPos = Integer.parseInt(SCUIFeature.get("DEFAULT_SINDICATORPOS"));
        int sIndicator = Integer.parseInt(SCUIFeature.get("DEFAULT_SINDICATOR"));
        int fPoint = Integer.parseInt(SCUIFeature.get("DEFAULT_FPOINT"));

        ScaleConnector sc = new ScaleConnector(serialPort, baud, dataBits, stopBits, parity, startCharacter, endCharacter);
        sc.setByteCount(byteCount);
        sc.setStartCharacter(startCharacter);
        sc.setEndCharacter(endCharacter);
        sc.setReadings(readings);
        sc.setStartCutPosition(startCut);
        sc.setEndCutPosition(endCut);
        sc.setStabilityValuePosition(sIndicatorPos);
        sc.setStabilityValue(sIndicator);
        sc.setFloatingPoint(fPoint);
        return sc;
    }

    static void commandHost() {
        String os = SCUIFeature.get("OS");
        System.out.println("Operative system: " + os);
        System.out.println("Port TCP Server: " + SCUIFeature.get("DEFAULT_PORT"));

        try {
            String command = os.toLowerCase().contains("windows") ? "ipconfig /all" : "ifconfig";
            Process p = Runtime.getRuntime().exec(command);
            try (InputStream out = new BufferedInputStream(p.getInputStream())) {
                StringBuilder buffer = new StringBuilder();
                int c;
                while ((c = out.read()) != -1) {
                    buffer.append((char) c);
                }
                System.out.println(buffer.toString());
            }
        } catch (Exception e) {
            logger.severe("Error, command not complete");
            e.printStackTrace();
        }
        System.out.println();
    }

    private static void initServerConsole() {
        new ConsoleServerMode();
    }

    private static void initUI() {
        loadFeaturesUI();
        System.out.println("Initializing UI components...");
        JFrame mainFrame = new JFrame("Scale Connector");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);

        // Center the window
        mainFrame.setLocationRelativeTo(null);

        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(new CustomFlatLaf());
            System.out.println("FlatLaf look and feel applied");
        } catch (Exception e) {
            System.err.println("Error applying Flatlaf Look-and-feel: " + e.getMessage());
            e.printStackTrace();
        }
        new ControllerViewSelectMode();
    }

    private static void loadFeaturesUI() {
        try {
            SCUIFeature.load();
            SCUILocale.load();
        } catch (Exception e) {
            logger.severe("Error loading features");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading features", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private static void loadFeatures() {
        try {
            SCUIFeature.load();
        } catch (Exception e) {
            logger.severe("Error loading features");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void configureLogging() {
        Logger rootLogger = Logger.getLogger(""); // Root logger
        rootLogger.setLevel(Level.INFO); // Set global logging level to INFO

        // Set all handlers to INFO
        for (java.util.logging.Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(Level.INFO);
        }

        // Suppress specific noisy loggers
        Logger.getLogger("java.awt.Component").setLevel(Level.WARNING);
        Logger.getLogger("java.awt.DefaultKeyboardFocusManager").setLevel(Level.WARNING);
        Logger.getLogger("org.jvnet.substance").setLevel(Level.WARNING);
        Logger.getLogger("java.awt.EventDispatchThread").setLevel(Level.WARNING);
        Logger.getLogger("org.jvnet.substance.utils.SubstanceCoreUtilities").setLevel(Level.WARNING);

        logger.info("Logging configured successfully.");
    }

    public static void main(String[] args) {
        configureLogging();
        loadFeatures();
        if (args.length > 0) {
            for (String arg : args) {
                switch (arg) {
                    case "-version" -> commandVersion();
                    case "-server" -> initServerConsole();
                    case "-host" -> commandHost();
                    case "-scale" -> commandScale();
                    case "-ports" -> commandPorts();
                    case "-help" -> commandHelp();
                    default -> commandHelp();
                }
            }
        } else {
            initUI();
        }
    }
}