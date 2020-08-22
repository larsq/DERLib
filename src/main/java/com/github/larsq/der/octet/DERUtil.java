package com.github.larsq.der.octet;

import java.math.BigInteger;

public class DERUtil {

    private DERUtil() {
        //Explicit empty
    }

    public static int uint7msf(int firstInclusive, int lastExclusive, byte[] bytes) {
        BigInteger value = BigInteger.ZERO;
        for (int i = 0; i < lastExclusive - firstInclusive; i++) {
            int current = (bytes[firstInclusive + i] & 127);
            value = value.shiftLeft(7).add(BigInteger.valueOf(current));
        }

        return value.intValueExact();
    }

    public static void checkSize(int expected, byte[] bytes) {
        if (bytes.length != expected) {
            throw new IllegalArgumentException("Expected " + expected + " octets: found " + bytes.length);
        }
    }

    public static void isNotEmpty(byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            throw new IllegalArgumentException("For short form, no intermediary length octets expected");
        }
    }
}
