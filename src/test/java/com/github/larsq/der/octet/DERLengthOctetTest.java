package com.github.larsq.der.octet;

import org.junit.jupiter.api.Test;

import static com.github.larsq.der.octet.DERLengthOctet.DERLengthForm.*;
import static com.github.larsq.der.octet.TestSupport.fromReverseBits;
import static org.junit.jupiter.api.Assertions.*;

class DERLengthOctetTest {
    @Test
    void lengthShortForm() {
        assertLengthOctet(DERLengthOctet.encode(fromReverseBits("00000011")), SHORT, 3);
    }

    @Test
    void lengthFullForm() {
        assertLengthOctet(DERLengthOctet.encode(fromReverseBits("11000000")), FULL, 64);
    }

    @Test
    void lengthIndefiniteForm() {
        assertLengthOctet(DERLengthOctet.encode(fromReverseBits("10000000")), INDEFINITE, -1);
    }

    @Test
    void calculatesSizeForShortForm() {
        assertEquals(10, DERLengthOctet.size(new DERLengthOctet(SHORT, 10), new byte[0]));
    }

    @Test
    void calculateSizeForLongForm() {
        // 1000000 1111110
        assertEquals(16510, DERLengthOctet.size(new DERLengthOctet(FULL, 2),
                new byte[]{64, 126}));
    }

    @Test
    void shortFormCouldNotHaveAnyIntermediaries() {
        assertThrows(IllegalArgumentException.class,
                () -> DERLengthOctet.size(new DERLengthOctet(SHORT, 2), new byte[]{0, 1, 2}));
    }

    @Test
    void indefiniteSize() {
        assertEquals(-1, DERLengthOctet.size(new DERLengthOctet(INDEFINITE, 0), new byte[0]));
    }

    private void assertLengthOctet(DERLengthOctet octet, DERLengthOctet.DERLengthForm form, int value) {
        assertEquals(form, octet.form);
        assertEquals(value, octet.value);
    }


}