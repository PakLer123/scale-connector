package com.ingeint.scaleconnector.base.scaleconnector;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.fazecast.jSerialComm.SerialPort;
//import com.fazecast.jSerialComm.SerialPortException;
import com.fazecast.jSerialComm.SerialPortTimeoutException;
import java.io.ByteArrayInputStream;

public class ScaleConnectorTest {

    private ScaleConnector scaleConnector;
    private SerialPort mockSerialPort;

    @BeforeEach
    public void setUp() {
        mockSerialPort = Mockito.mock(SerialPort.class);
        scaleConnector = new ScaleConnector("COM1", 9600, 8, 1, 0, (char) 2, (char) 3);
        //scaleConnector.port = mockSerialPort;
        scaleConnector.setSerialPort(mockSerialPort.getDescriptivePortName());
    }

    @Test
    void readValueReturnsMostFrequentValue() throws Exception {
        Mockito.when(mockSerialPort.isOpen()).thenReturn(true);
        Mockito.when(mockSerialPort.readBytes(Mockito.any(byte[].class), Mockito.anyInt()))
                .thenReturn(10, 10, 10, 10, 10);
        Mockito.when(mockSerialPort.getInputStream()).thenReturn(new ByteArrayInputStream("123".getBytes()));

        scaleConnector.setReadings(5);
        scaleConnector.setStabilityValue(5);

        String result = scaleConnector.readValue();
        assertEquals("123", result);
        assertTrue(scaleConnector.isStableValue());
    }

    @Test
    void readValueHandlesEmptyInput() throws Exception {
        Mockito.when(mockSerialPort.isOpen()).thenReturn(true);
        Mockito.when(mockSerialPort.readBytes(Mockito.any(byte[].class), Mockito.anyInt()))
                .thenReturn(0);

        scaleConnector.setReadings(5);

        String result = scaleConnector.readValue();
        assertEquals("", result);
        assertFalse(scaleConnector.isStableValue());
    }

//    @Test
//    void readValueHandlesSerialPortException() throws Exception {
//        Mockito.when(mockSerialPort.isOpen()).thenReturn(true);
//        Mockito.when(mockSerialPort.readBytes(Mockito.any(byte[].class), Mockito.anyInt()))
//                .thenThrow(new SerialPortException("COM1" + "readBytes" + "Test Timeout"));
//
//        scaleConnector.setReadings(5);
//
//        String result = scaleConnector.readValue();
//        assertEquals("", result);
//        assertFalse(scaleConnector.isStableValue());
//    }

    @Test
    void readValueHandlesSerialPortTimeoutException() throws Exception {
        Mockito.when(mockSerialPort.isOpen()).thenReturn(true);
        Mockito.when(mockSerialPort.readBytes(Mockito.any(byte[].class), Mockito.anyInt()))
                .thenThrow(new SerialPortTimeoutException("COM1" + "readBytes" + "Test Timeout"));

        scaleConnector.setReadings(5);

        String result = scaleConnector.readValue();
        assertEquals("", result);
        assertFalse(scaleConnector.isStableValue());
    }

    @Test
    void readStringReturnsCorrectString() throws Exception {
        Mockito.when(mockSerialPort.isOpen()).thenReturn(true);
        Mockito.when(mockSerialPort.readBytes(Mockito.any(byte[].class), Mockito.anyInt()))
                .thenReturn(10);
        Mockito.when(mockSerialPort.getInputStream()).thenReturn(new ByteArrayInputStream("Test String".getBytes()));

        String result = scaleConnector.readString();
        assertEquals("Test String", result);
    }

    @Test
    void readBytesReturnsCorrectBytes() throws Exception {
        byte[] expectedBytes = "Test Bytes".getBytes();
        Mockito.when(mockSerialPort.isOpen()).thenReturn(true);
        Mockito.when(mockSerialPort.readBytes(Mockito.any(byte[].class), Mockito.anyInt()))
                .thenReturn(expectedBytes.length);
        Mockito.when(mockSerialPort.getInputStream()).thenReturn(new ByteArrayInputStream(expectedBytes));

        byte[] result = scaleConnector.readBytes();
        assertArrayEquals(expectedBytes, result);
    }
}