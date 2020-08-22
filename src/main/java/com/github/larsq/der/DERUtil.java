package com.github.larsq.der;

import java.math.BigInteger;
import java.util.Arrays;

public class DERUtil {

    private DERUtil() {
        //Explicit empty
    }

    // Encode unsigned integer using 7 bits, most significant
    public static int uint7msf(int firstInclusive, int lastExclusive, byte[] bytes) {
        BigInteger value = BigInteger.ZERO;
        for (int i = 0; i < lastExclusive - firstInclusive; i++) {
            int current = (bytes[firstInclusive + i] & 127);
            value = value.shiftLeft(7).add(BigInteger.valueOf(current));
        }

        return value.intValueExact();
    }

    public static int[] unsigned(byte[] bytes) {
        int[] ints = new int[bytes.length];
        Arrays.setAll(ints, index -> bytes[index] & 255);
        return ints;
    }
}
