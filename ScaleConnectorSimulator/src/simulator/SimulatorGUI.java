
package simulator;

import javax.swing.*;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortTimeoutException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimulatorGUI {
    private static final Logger logger = Logger.getLogger(SimulatorGUI.class.getName());
    private SerialPort serialPort;
    private JTextArea receivedDataArea;
    private JTextArea sentDataArea;
    private Thread simulationThread;

    public SimulatorGUI() {
        JFrame frame = new JFrame("Simulator GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        receivedDataArea = new JTextArea();
        receivedDataArea.setEditable(false);
        JScrollPane receivedScrollPane = new JScrollPane(receivedDataArea);
        panel.add(receivedScrollPane, BorderLayout.CENTER);

        sentDataArea = new JTextArea();
        sentDataArea.setEditable(false);
        JScrollPane sentScrollPane = new JScrollPane(sentDataArea);
        panel.add(sentScrollPane, BorderLayout.NORTH);

        JButton startButton = new JButton("Start Simulation");
        JButton stopButton = new JButton("Stop Simulation");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulation();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopSimulation();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void startSimulation() {
        // Configure the logger
        logger.setLevel(Level.INFO);

        // Use COM3 port
        serialPort = SerialPort.getCommPort("COM3");
        serialPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);

        // Increase read/write timeout to 10000 ms (10 seconds)
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 10000, 10000);

        if (serialPort.openPort()) {
            logger.info("Port opened successfully.");

            // Simulate scale data in a separate thread
            simulationThread = new Thread(() -> {
                try {
                    int weight = 0;
                    int stableWeight = 100000; // Example stable weight
                    int iterations = 0;
                    int stableIterations = 0;
                    int maxStableIterations = 5; // Number of times to repeat 50000

                    while (!Thread.currentThread().isInterrupted()) {
                        // Simulate data in the specified format
                        String formattedNumber = String.format("%07d1", weight);
                        char startOfText = 2; // ASCII STX
                        char endOfText = 3; // ASCII ETX
                        String simulatedData = "+" + formattedNumber + "B" + startOfText + endOfText + "\r";

                        // Display the simulated data in the sentDataArea
                        SwingUtilities.invokeLater(() -> sentDataArea.append("Sent: " + simulatedData + "\n"));

                        // Send the simulated data to the serial port character by character
                        for (char ch : simulatedData.toCharArray()) {
                            serialPort.getOutputStream().write(ch);
                            serialPort.getOutputStream().flush();
                            logger.info("Simulated Data Sent: " + ch);
                        }

                        // Gradually change the weight to simulate the truck entering the scale
                        if (iterations < 50) {
                            weight += (stableWeight - weight) / 10; // Adjust the divisor to control the speed of stabilization
                        } else {
                            weight = stableWeight; // Stabilize the weight
                            stableIterations++;
                            if (stableIterations >= maxStableIterations) {
                                weight = 0; // Reset the weight
                                iterations = 0; // Reset the iterations
                                stableIterations = 0; // Reset the stable iterations
                            }
                        }

                        iterations++;

                        // Wait 1 second before sending the next data
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error in simulation thread", e);
                }
            });
            simulationThread.setDaemon(true);
            simulationThread.start();

            // Read data from the serial port
            new Thread(() -> {
                byte[] buffer = new byte[1024];
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        int bytesRead = serialPort.getInputStream().read(buffer);
                        if (bytesRead > 0) {
                            String receivedData = new String(buffer, 0, bytesRead).trim();
                            SwingUtilities.invokeLater(() -> receivedDataArea.append("Received: " + receivedData + "\n"));
                            logger.info("Received: " + receivedData);
                        }
                    } catch (SerialPortTimeoutException e) {
                        logger.warning("Read operation timed out after 10 seconds.");
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
