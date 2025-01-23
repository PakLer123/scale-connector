package com.ingeint.scaleconnector.base;

import java.util.HashMap;
import java.util.Map;

public class AsciiTranslator {

    private static final Map<Integer, String> asciiMnemonics = new HashMap<>();

    static {
        asciiMnemonics.put(2, "STX");
        asciiMnemonics.put(3, "ETX");
        asciiMnemonics.put(4, "EOT");
        asciiMnemonics.put(27, "ESC");
        // Add more control characters as needed
    }

    public static String getAsciiMnemonic(int code) {
        return asciiMnemonics.getOrDefault(code, "CTRL-" + code);
    }

    public static String translate(byte[] buffer, int bytesRead) {
        StringBuilder asciiRepresentation = new StringBuilder("ASCII Interpretation: ");
        for (int i = 0; i < bytesRead; i++) {
            int value = buffer[i] & 0xFF; // Ensure unsigned byte value
            if (value < 32 || value == 127) {
                asciiRepresentation.append(getAsciiMnemonic(value)).append(" ");
            } else {
                asciiRepresentation.append((char) value).append(" ");
            }
        }
        return asciiRepresentation.toString();
    }
}
