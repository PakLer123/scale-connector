package com.ingeint.scaleconnector.base.scaleconnector;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;

import com.fazecast.jSerialComm.SerialPort;

public class ScaleConnectorTest {

    private static final char STX = 2;
    private static final char ETX = 3;

    private SerialPort mockSerialPort;
    private ScaleConnector scaleConnector;
    private List<String> warnings;

    @BeforeEach
    public void setUp() {
        mockSerialPort = mock(SerialPort.class);
        when(mockSerialPort.isOpen()).thenReturn(false);
        when(mockSerialPort.openPort()).thenReturn(true);

        scaleConnector = new ScaleConnector(mockSerialPort, "COM1", 9600, 8, 1, 0, STX, ETX);
        scaleConnector.setByteCount(16);

        warnings = new ArrayList<>();
        scaleConnector.setWarningHandler(warnings::add);
    }

    /** Makes the mock port return the given frames, one per read. */
    private void portSends(String... frames) {
        OngoingStubbing<Integer> stub = when(mockSerialPort.readBytes(any(byte[].class), anyInt()));
        for (String frame : frames) {
            stub = stub.thenAnswer(invocation -> {
                byte[] buffer = invocation.getArgument(0);
                byte[] bytes = frame.getBytes();
                System.arraycopy(bytes, 0, buffer, 0, bytes.length);
                return bytes.length;
            });
        }
    }

    private static String frame(String payload) {
        return STX + payload + ETX;
    }

    @Test
    void readBytesReturnsOnlyTheBytesRead() throws Exception {
        portSends("ABC");

        assertArrayEquals("ABC".getBytes(), scaleConnector.readBytes());
    }

    @Test
    void readBytesWarnsAndReturnsEmptyWhenNoDataIsRead() throws Exception {
        when(mockSerialPort.readBytes(any(byte[].class), anyInt())).thenReturn(0);

        assertEquals(0, scaleConnector.readBytes().length);
        assertEquals(List.of("No data read from port."), warnings);
    }

    @Test
    void readBytesThrowsWhenPortCannotBeOpened() {
        when(mockSerialPort.openPort()).thenReturn(false);

        Exception e = assertThrows(Exception.class, () -> scaleConnector.readBytes());
        assertEquals("Unable to open port: COM1", e.getMessage());
    }

    @Test
    void readBytesClosesPortAfterReading() throws Exception {
        when(mockSerialPort.isOpen()).thenReturn(false, true);
        portSends("ABC");

        scaleConnector.readBytes();

        verify(mockSerialPort).closePort();
    }

    @Test
    void readStringDecodesBytes() throws Exception {
        portSends("Test String");

        assertEquals("Test String", scaleConnector.readString());
    }

    @Test
    void readStringReturnsEmptyWhenNoDataIsRead() throws Exception {
        when(mockSerialPort.readBytes(any(byte[].class), anyInt())).thenReturn(0);

        assertEquals("", scaleConnector.readString());
    }

    @Test
    void readValueExtractsValueBetweenStartAndEndCharacters() {
        portSends(frame("+001250"));
        scaleConnector.setStartCutPosition(2);
        scaleConnector.setEndCutPosition(7);

        assertEquals("001250", scaleConnector.readValue());
    }

    @Test
    void readValueUsesConfiguredStartAndEndCharacters() {
        portSends("junk+001250Bjunk");
        scaleConnector.setStartCharacter('+');
        scaleConnector.setEndCharacter('B');

        assertEquals("001250", scaleConnector.readValue());
    }

    @Test
    void readValueIgnoresStxEtxWhenOtherCharactersAreConfigured() {
        portSends(frame("100"));
        scaleConnector.setStartCharacter('+');
        scaleConnector.setEndCharacter('B');

        assertEquals("", scaleConnector.readValue());
    }

    @Test
    void readValueAppliesFloatingPoint() {
        portSends(frame("+001250"));
        scaleConnector.setStartCutPosition(2);
        scaleConnector.setEndCutPosition(7);
        scaleConnector.setFloatingPoint(2);

        assertEquals("12.5", scaleConnector.readValue());
    }

    @Test
    void readValueIsStableWhenAllReadingsMatch() {
        portSends(frame("100"), frame("100"), frame("100"));
        scaleConnector.setReadings(3);
        scaleConnector.setStabilityValue(3);

        assertEquals("100", scaleConnector.readValue());
        assertTrue(scaleConnector.isStableValue());
    }

    @Test
    void readValueReturnsMostFrequentValueButIsNotStableWhenReadingsDiffer() {
        portSends(frame("100"), frame("105"), frame("100"));
        scaleConnector.setReadings(3);
        scaleConnector.setStabilityValue(3);

        assertEquals("100", scaleConnector.readValue());
        assertFalse(scaleConnector.isStableValue());
    }

    @Test
    void readValueSkipsReadingsThatFail() {
        when(mockSerialPort.readBytes(any(byte[].class), anyInt()))
                .thenThrow(new RuntimeException("Test Exception"))
                .thenAnswer(invocation -> {
                    byte[] bytes = frame("100").getBytes();
                    System.arraycopy(bytes, 0, invocation.getArgument(0), 0, bytes.length);
                    return bytes.length;
                });
        scaleConnector.setReadings(2);
        scaleConnector.setStabilityValue(1);

        assertEquals("100", scaleConnector.readValue());
        assertTrue(scaleConnector.isStableValue());
    }

    @Test
    void readValueReturnsEmptyWhenNoDataIsRead() {
        when(mockSerialPort.readBytes(any(byte[].class), anyInt())).thenReturn(0);
        scaleConnector.setReadings(3);

        assertEquals("", scaleConnector.readValue());
        assertFalse(scaleConnector.isStableValue());
    }

    @Test
    void readValueReturnsEmptyWhenStartCharacterIsMissing() {
        portSends("100" + ETX);

        assertEquals("", scaleConnector.readValue());
        assertFalse(scaleConnector.isStableValue());
    }

    @Test
    void readValueReturnsEmptyWhenEndCharacterComesBeforeStart() {
        portSends(ETX + "100" + STX);

        assertEquals("", scaleConnector.readValue());
    }
}
