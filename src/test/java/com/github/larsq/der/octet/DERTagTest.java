package com.github.larsq.der.octet;

import org.junit.jupiter.api.Test;

import static com.github.larsq.der.octet.DERClass.*;
import static com.github.larsq.der.octet.DERClass.PRIVATE;
import static org.junit.jupiter.api.Assertions.*;

class DERTagTest {
    @Test
    void shouldParseTagClassCorrectly() {
        assertEquals(UNIVERSAL, DERTag.encode(TestSupport.fromReverseBits("00000000")).derClass);
        assertEquals(APPLICATION, DERTag.encode(TestSupport.fromReverseBits("01000000")).derClass);
        assertEquals(CONTEXT_SPECIFIC, DERTag.encode(TestSupport.fromReverseBits("10000000")).derClass);
        assertEquals(PRIVATE, DERTag.encode(TestSupport.fromReverseBits("11000000")).derClass);
    }

    @Test
    void shouldParseConstructedFlag() {
        assertTrue(DERTag.encode(TestSupport.fromReverseBits("00100000")).constructed);
        assertFalse(DERTag.encode(TestSupport.fromReverseBits("00000000")).constructed);
    }

    @Test
    void shouldParseNumber() {
        assertEquals(31, DERTag.encode(TestSupport.fromReverseBits("00111111")).tag);
        assertEquals(15, DERTag.encode(TestSupport.fromReverseBits("00001111")).tag);
    }

    @Test
    void shouldSetContinue() {
        assertTrue(DERTag.encode(TestSupport.fromReverseBits("00111111")).continueWithNext);
        assertFalse(DERTag.encode(TestSupport.fromReverseBits("00111110")).continueWithNext);
    }


    @Test
    void tagLessThan31() {
        assertEquals(12, DERTag.tag(new DERTag(UNIVERSAL, false, 12, false)));
    }

    @Test
    void tagGreaterThan31() {
        assertEquals(8318, DERTag.tag(new DERTag(UNIVERSAL, false, 31, true),
                new byte[]{(byte) 192, 126}));
    }

    @Test
    void textRepresentation() {
        DERTag tag = new DERTag(UNIVERSAL, false, 2, false);

        assertEquals("DERTagOctet{derClass=UNIVERSAL, constructed=false, value=2, continueWithNext=false}", tag.toString());
    }

    @Test
    void tagThatShouldContinueShouldHaveMoreBytes() {
        DERTag tag = new DERTag(UNIVERSAL, false, 31, true);

        assertThrows(IllegalArgumentException.class,
                () -> DERTag.tag(tag));
    }

    @Test
    void intermediaryTagsShouldNotHavePrematureEnd() {
        DERTag tag = new DERTag(UNIVERSAL, false, 31, true);

        assertThrows(IllegalArgumentException.class,
                () -> DERTag.tag(tag, (byte) 0x80, (byte) 0x80, (byte) 0x80));
    }
}