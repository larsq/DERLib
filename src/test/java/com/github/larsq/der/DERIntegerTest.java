package com.github.larsq.der;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class DERIntegerTest {
    @Test
    void encoding() {
        assertAll(
                () -> assertEquals(BigInteger.ZERO, DERInteger.decode(new byte[]{0x0}).value()),
                () -> assertEquals(BigInteger.valueOf(127), DERInteger.decode(new byte[]{0x7F}).value()),
                () -> assertEquals(BigInteger.valueOf(128), DERInteger.decode(new byte[]{0x0, (byte) 0x80}).value()),
                () -> assertEquals(BigInteger.valueOf(256), DERInteger.decode(new byte[]{0x01, 0x0}).value()),
                () -> assertEquals(BigInteger.valueOf(-128), DERInteger.decode(new byte[]{(byte) 0x80}).value()),
                () -> assertEquals(BigInteger.valueOf(-129), DERInteger.decode(new byte[]{(byte) 0xFF, 0x7F}).value())
        );
    }
}