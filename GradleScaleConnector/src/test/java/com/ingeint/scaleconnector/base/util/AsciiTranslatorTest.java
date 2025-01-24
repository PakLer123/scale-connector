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
}
