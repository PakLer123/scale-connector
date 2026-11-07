package com.ingeint.scaleconnector.base.util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AsciiTranslatorTest {

    @Test
    void getAsciiMnemonicReturnsCorrectMnemonic() {
        assertEquals("STX", AsciiTranslator.getAsciiMnemonic(2));
        assertEquals("ETX", AsciiTranslator.getAsciiMnemonic(3));
        assertEquals("EOT", AsciiTranslator.getAsciiMnemonic(4));
        assertEquals("ESC", AsciiTranslator.getAsciiMnemonic(27));
    }

    @Test
    void getAsciiMnemonicReturnsDefaultForUnknownCode() {
        assertEquals("CTRL-99", AsciiTranslator.getAsciiMnemonic(99));
    }

    @Test
    void translateReturnsCorrectAsciiRepresentation() {
        byte[] buffer = {2, 3, 4, 65, 66, 67};
        String result = AsciiTranslator.translate(buffer, buffer.length);
        assertEquals("ASCII Interpretation: STX ETX EOT A B C ", result);
    }

    @Test
    void translateHandlesEmptyBuffer() {
        byte[] buffer = {};
        String result = AsciiTranslator.translate(buffer, buffer.length);
        assertEquals("ASCII Interpretation: ", result);
    }

    @Test
    void translateHandlesControlCharacters() {
        byte[] buffer = {2, 27, 127};
        String result = AsciiTranslator.translate(buffer, buffer.length);
        assertEquals("ASCII Interpretation: STX ESC CTRL-127 ", result);
    }

    @Test
    void translateHandlesMixedCharacters() {
        byte[] buffer = {2, 65, 27, 66, 127, 67};
        String result = AsciiTranslator.translate(buffer, buffer.length);
        assertEquals("ASCII Interpretation: STX A ESC B CTRL-127 C ", result);
    }

    @Test
    void parseCharacterAcceptsDecimalCode() {
        assertEquals((char) 2, AsciiTranslator.parseCharacter("2"));
        assertEquals((char) 13, AsciiTranslator.parseCharacter(" 13 "));
    }

    @Test
    void parseCharacterAcceptsMnemonicIgnoringCase() {
        assertEquals((char) 2, AsciiTranslator.parseCharacter("STX"));
        assertEquals((char) 3, AsciiTranslator.parseCharacter("etx"));
        assertEquals((char) 10, AsciiTranslator.parseCharacter("LF"));
    }

    @Test
    void parseCharacterAcceptsLiteralCharacter() {
        assertEquals('+', AsciiTranslator.parseCharacter("+"));
        assertEquals('B', AsciiTranslator.parseCharacter("B"));
    }

    @Test
    void parseCharacterRejectsInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> AsciiTranslator.parseCharacter(""));
        assertThrows(IllegalArgumentException.class, () -> AsciiTranslator.parseCharacter(null));
        assertThrows(IllegalArgumentException.class, () -> AsciiTranslator.parseCharacter("256"));
        assertThrows(IllegalArgumentException.class, () -> AsciiTranslator.parseCharacter("ABC"));
    }

    @Test
    void parseCharacterRoundTripsAllMnemonics() {
        for (int code = 0; code < 32; code++) {
            assertEquals((char) code, AsciiTranslator.parseCharacter(AsciiTranslator.getAsciiMnemonic(code)));
        }
    }
}
