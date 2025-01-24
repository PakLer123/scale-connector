package com.ingeint.scaleconnector.gui.app;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fazecast.jSerialComm.SerialPort;
import com.ingeint.scaleconnector.gui.feature.SCUIFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.*;

class MainTest {

    private final ByteArrayOutputStream logContent = new ByteArrayOutputStream();
    private final PrintStream logStream = new PrintStream(logContent);

    @BeforeEach
    void setUp() {
        System.setErr(logStream);
    }

    @Test
    void commandVersion_displaysVersionInfo() {
        // Capture the output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.commandVersion();

        String expectedOutput = String.format("%s %s%n%s%n%s%n%n", SCUIFeature.get("APP_NAME"), SCUIFeature.get("VERSION"), SCUIFeature.get("VENDOR"), SCUIFeature.get("WEB"));
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void commandHelp_displaysHelpInfo() {
        // Capture the output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Main.commandHelp();

        String expectedOutput = String.format("%10s\t%s%n%10s\t%s%n%10s\t%s%n%10s\t%s%n%10s\t%s%n%10s\t%s%n%n",
                "-version", "Displays the version of the application",
                "-help", "Displays the commands that can be used",
                "-host", "Displays the values of the server TCP",
                "-scale", "Displays the values of the scales",
                "-ports", "Displays the serial ports",
                "-server", "Application server console starts");
        assertEquals(expectedOutput, outContent.toString());
    }

//    @Test
//    void commandPorts_displaysAvailablePorts() {
//        // Create a custom implementation of SerialPort
//        SerialPort[] ports = {new SerialPortWrapper(new SerialPort("COM1")).getSystemPortName()};
//
//        // Mock SerialPort.getCommPorts()
//        try (MockedStatic<SerialPort> mockedStatic = mockStatic(SerialPort.class)) {
//            mockedStatic.when(SerialPort::getCommPorts).thenReturn(ports);
//
//            // Capture the output
//            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//            System.setOut(new PrintStream(outContent));
//
//            Main.commandPorts();
//
//            String expectedOutput = "Port: COM1\n\n";
//            assertEquals(expectedOutput, outContent.toString());
//        }
//    }
//
//    @Test
//    void commandPorts_displaysNoAvailablePorts() {
//        // Mock SerialPort.getCommPorts()
//        try (MockedStatic<SerialPort> mockedStatic = mockStatic(SerialPort.class)) {
//            mockedStatic.when(SerialPort::getCommPorts).thenReturn(new SerialPort[0]);
//
//            // Capture the output
//            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//            System.setOut(new PrintStream(outContent));
//
//            Main.commandPorts();
//
//            String expectedOutput = "No available serial ports.\n\n";
//            assertEquals(expectedOutput, outContent.toString());
//        }
//    }
//
//    @Test
//    void commandHost_displaysHostInfo() throws IOException {
//        // Mock SCUIFeature.get()
//        try (MockedStatic<SCUIFeature> mockedStatic = mockStatic(SCUIFeature.class)) {
//            mockedStatic.when(() -> SCUIFeature.get("OS")).thenReturn("Windows");
//            mockedStatic.when(() -> SCUIFeature.get("DEFAULT_PORT")).thenReturn("8080");
//
//            // Mock Runtime.getRuntime().exec()
//            Process mockProcess = mock(Process.class);
//            InputStream mockInputStream = new ByteArrayInputStream("Mocked IP Config Output".getBytes());
//            when(mockProcess.getInputStream()).thenReturn(mockInputStream);
//            try (MockedStatic<Runtime> runtimeMockedStatic = mockStatic(Runtime.class)) {
//                runtimeMockedStatic.when(() -> Runtime.getRuntime().exec("ipconfig /all")).thenReturn(mockProcess);
//
//                // Capture the output
//                ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//                System.setOut(new PrintStream(outContent));
//
//                Main.commandHost();
//
//                String expectedOutput = "Operative system: Windows\nPort TCP Server: 8080\nMocked IP Config Output\n\n";
//                assertEquals(expectedOutput, outContent.toString());
//            }
//        }
//    }

    // Custom implementation of SerialPort for testing
    static class SerialPortWrapper {
        private final SerialPort portName;

        SerialPortWrapper(SerialPort portName) {
            this.portName = portName;
        }

        public SerialPort getSystemPortName() {
            return portName;
        }
    }
}