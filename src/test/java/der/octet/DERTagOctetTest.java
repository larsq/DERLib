package com.github.larsq.der.octet;

import org.junit.jupiter.api.Test;

import static com.github.larsq.der.octet.DERClass.*;
import static com.github.larsq.der.octet.DERClass.PRIVATE;
import static org.junit.jupiter.api.Assertions.*;

class DERTagOctetTest {
    @Test
    void shouldParseTagClassCorrectly() {
        assertEquals(UNIVERSAL, DERTagOctet.encode(TestSupport.fromReverseBits("00000000")).derClass);
        assertEquals(APPLICATION, DERTagOctet.encode(TestSupport.fromReverseBits("01000000")).derClass);
        assertEquals(CONTEXT_SPECIFIC, DERTagOctet.encode(TestSupport.fromReverseBits("10000000")).derClass);
        assertEquals(PRIVATE, DERTagOctet.encode(TestSupport.fromReverseBits("11000000")).derClass);
    }

    @Test
    void shouldParseConstructedFlag() {
        assertTrue(DERTagOctet.encode(TestSupport.fromReverseBits("00100000")).constructed);
        assertFalse(DERTagOctet.encode(TestSupport.fromReverseBits("00000000")).constructed);
    }

    @Test
    void shouldParseNumber() {
        assertEquals(31, DERTagOctet.encode(TestSupport.fromReverseBits("00111111")).tag);
        assertEquals(15, DERTagOctet.encode(TestSupport.fromReverseBits("00001111")).tag);
    }

    @Test
    void shouldSetContinue() {
        assertTrue(DERTagOctet.encode(TestSupport.fromReverseBits("00111111")).continueWithNext);
        assertFalse(DERTagOctet.encode(TestSupport.fromReverseBits("00111110")).continueWithNext);
    }


    @Test
    void tagLessThan31() {
        assertEquals(12, DERTagOctet.tag(new DERTagOctet(UNIVERSAL, false, 12, false)));
    }

    @Test
    void tagGreaterThan31() {
        assertEquals(8318, DERTagOctet.tag(new DERTagOctet(UNIVERSAL, false, 31, true),
                new byte[]{(byte) 192, 126}));
    }
}