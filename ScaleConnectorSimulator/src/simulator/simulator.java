package simulator;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortTimeoutException;

import java.util.logging.Level;
import java.util.logging.Logger;


public class simulator {
	private static final Logger logger = Logger.getLogger(simulator.class.getName());

    public static void main(String[] args) {
        // Configure the logger
        logger.setLevel(Level.INFO);

        // Use COM4 port
        final SerialPort serialPort = SerialPort.getCommPort("COM3");
        serialPort.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);

        // Increase read/write timeout to 10000 ms (10 seconds)
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 10000, 10000);

        if (serialPort.openPort()) {
            logger.info("Port opened successfully.");

            // Simulate scale data in a separate thread
            Thread simulationThread = new Thread(() -> {
                try {
                    int counter = 0;
                    while (true) {
                        // Simulate data in the specified format
                        String formattedNumber = String.format("%08d1", counter);
                        String simulatedData = formattedNumber + "B"+ "\r";

                        // Send the simulated data to the serial port
                        serialPort.getOutputStream().write(simulatedData.getBytes());
                        serialPort.getOutputStream().flush();
                        logger.info("Simulated Data Sent: " + simulatedData.trim());

                        // Increment counter and reset after reaching 99999999
                        counter = (counter + 1) % 100000000;

                        // Wait 1 second before sending the next data
                        Thread.sleep(100);
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error in simulation thread", e);
                }
            });
            simulationThread.setDaemon(true);
            simulationThread.start();

            // Read data from the serial port
            byte[] buffer = new byte[1024];
            while (true) {
                try {
                    int bytesRead = serialPort.getInputStream().read(buffer);
                    if (bytesRead > 0) {
                        String receivedData = new String(buffer, 0, bytesRead).trim();
                        logger.info("Received: " + receivedData);
                    }
                } catch (SerialPortTimeoutException e) {
                    logger.warning("Read operation timed out after 10 seconds.");
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error reading from serial port", e);
                }
            }
        } else {
            logger.severe("Failed to open port.");
        }

        serialPort.closePort();
    }
}
