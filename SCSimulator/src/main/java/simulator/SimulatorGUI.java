package simulator;

import javax.swing.*;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortTimeoutException;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.formdev.flatlaf.FlatDarculaLaf;


public class SimulatorGUI {
    private static final Logger logger = Logger.getLogger(SimulatorGUI.class.getName());
    private SerialPort serialPort;
    private JTextArea receivedDataArea;
    private JTextArea sentDataArea;
    private JTextArea rawDataArea;  // New text area for raw data
    private Thread simulationThread;

    public SimulatorGUI() {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException e) {
            logger.log(Level.SEVERE, "Failed to set custom look and feel", e);
        }
        JFrame frame = new JFrame("Simulator GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);  // Increased window size

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create a split pane for sent and received data
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        // Top panel for sent data
        JPanel topPanel = new JPanel(new BorderLayout());
        sentDataArea = new JTextArea(5, 40);  // Formatted sent data
        sentDataArea.setEditable(false);
        rawDataArea = new JTextArea(5, 40);   // Raw sent data
        rawDataArea.setEditable(false);

        // Labels for the text areas
        JPanel sentPanel = new JPanel(new BorderLayout());
        sentPanel.add(new JLabel("Formatted Sent Data:"), BorderLayout.NORTH);
        sentPanel.add(new JScrollPane(sentDataArea), BorderLayout.CENTER);

        JPanel rawPanel = new JPanel(new BorderLayout());
        rawPanel.add(new JLabel("Raw Sent Data:"), BorderLayout.NORTH);
        rawPanel.add(new JScrollPane(rawDataArea), BorderLayout.CENTER);

        // Add both to top panel
        JSplitPane topSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sentPanel, rawPanel);
        topPanel.add(topSplitPane, BorderLayout.CENTER);

        // Bottom panel for received data
        JPanel bottomPanel = new JPanel(new BorderLayout());
        receivedDataArea = new JTextArea(10, 40);
        receivedDataArea.setEditable(false);
        bottomPanel.add(new JLabel("Received Data:"), BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(receivedDataArea), BorderLayout.CENTER);

        splitPane.setTopComponent(topPanel);
        splitPane.setBottomComponent(bottomPanel);
        splitPane.setDividerLocation(300);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Start Simulation");
        JButton stopButton = new JButton("Stop Simulation");
        JButton clearButton = new JButton("Clear Display");  // New clear button

        startButton.addActionListener(e -> startSimulation());
        stopButton.addActionListener(e -> stopSimulation());
        clearButton.addActionListener(e -> {
            sentDataArea.setText("");
            rawDataArea.setText("");
            receivedDataArea.setText("");
        });

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(clearButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void startSimulation() {
        logger.setLevel(Level.INFO);
        serialPort = SerialPort.getCommPort("COM3");
        serialPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 10000, 10000);

        if (serialPort.openPort()) {
            logger.info("Port opened successfully.");
            StringBuilder rawDataBuilder = new StringBuilder();  // To accumulate raw data

            simulationThread = new Thread(() -> {
                try {
                    int weight = 0;
                    int stableWeight = 100000;
                    int iterations = 0;
                    int stableIterations = 0;
                    int maxStableIterations = 5;

                    while (!Thread.currentThread().isInterrupted()) {
                        String formattedNumber = String.format("%07d1", weight);
                        char startOfText = 2;
                        char endOfText = 3;
                        String simulatedData = startOfText + "+" + formattedNumber + "B" + endOfText;

                        // Update formatted display (with \n for readability)
                        SwingUtilities.invokeLater(() ->
                                sentDataArea.append("Sent: " + simulatedData + "\n"));

                        // Update raw data display (continuous stream)
                        rawDataBuilder.append(simulatedData);
                        SwingUtilities.invokeLater(() ->
                                rawDataArea.setText(rawDataBuilder.toString()));

                        // Send data to serial port
                        for (char ch : simulatedData.toCharArray()) {
                            serialPort.getOutputStream().write(ch);
                            serialPort.getOutputStream().flush();
                            logger.info("Sent character: " + (int)ch);
                        }

                        if (iterations < 50) {
                            weight += (stableWeight - weight) / 10;
                        } else {
                            weight = stableWeight;
                            stableIterations++;
                            if (stableIterations >= maxStableIterations) {
                                weight = 0;
                                iterations = 0;
                                stableIterations = 0;
                            }
                        }

                        iterations++;
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error in simulation thread", e);
                }
            });
            simulationThread.setDaemon(true);
            simulationThread.start();

            // Read thread
            new Thread(() -> {
                byte[] buffer = new byte[1024];
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        int bytesRead = serialPort.getInputStream().read(buffer);
                        if (bytesRead > 0) {
                            String receivedData = new String(buffer, 0, bytesRead);
                            SwingUtilities.invokeLater(() -> receivedDataArea.append(receivedData));
                            logger.info("Received raw data: " + receivedData);
                        }
                    } catch (SerialPortTimeoutException e) {
                        logger.warning("Read timeout after 10 seconds.");
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Error reading from serial port", e);
                    }
                }
            }).start();
        } else {
            logger.severe("Failed to open port.");
        }
    }

    private void stopSimulation() {
        if (simulationThread != null && simulationThread.isAlive()) {
            simulationThread.interrupt();
        }
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            logger.info("Port closed.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SimulatorGUI::new);
    }
}