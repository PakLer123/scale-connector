package com.ingeint.scaleconnector.base.util;

import java.util.Locale;

public class AsciiTranslator {

    // Standard mnemonics for the ASCII control characters 0-31, indexed by code
    private static final String[] ASCII_MNEMONICS = {
            "NUL", "SOH", "STX", "ETX", "EOT", "ENQ", "ACK", "BEL",
            "BS", "HT", "LF", "VT", "FF", "CR", "SO", "SI",
            "DLE", "DC1", "DC2", "DC3", "DC4", "NAK", "SYN", "ETB",
            "CAN", "EM", "SUB", "ESC", "FS", "GS", "RS", "US"
    };

    public static String getAsciiMnemonic(int code) {
        if (code >= 0 && code < ASCII_MNEMONICS.length) {
            return ASCII_MNEMONICS[code];
        }
        return "CTRL-" + code;
    }

    /**
     * Parses a start/end character setting as entered by the user. Accepts a
     * decimal character code ("2"), a control character mnemonic ("STX",
     * case-insensitive) or a single literal character ("+").
     *
     * @param text The setting text
     * @return The character it describes
     * @throws IllegalArgumentException If the text is empty or not recognised
     */
    public static char parseCharacter(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Character setting is empty");
        }
        String value = text.trim();

        if (value.chars().allMatch(Character::isDigit)) {
            int code = Integer.parseInt(value);
            if (code > 255) {
                throw new IllegalArgumentException("Character code out of range (0-255): " + value);
            }
            return (char) code;
        }

        String upper = value.toUpperCase(Locale.ROOT);
        for (int code = 0; code < ASCII_MNEMONICS.length; code++) {
            if (ASCII_MNEMONICS[code].equals(upper)) {
                return (char) code;
            }
        }

        if (value.length() == 1) {
            return value.charAt(0);
        }
        throw new IllegalArgumentException("Unknown character: " + value);
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
