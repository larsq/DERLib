package com.github.larsq.der.octet;

import java.util.BitSet;

public class TestSupport {
    static byte fromReverseBits(String bitstring) {
        if (bitstring.length() != 8) {
            throw new IllegalArgumentException();
        }

        if (bitstring.equals("00000000")) {
            return 0;
        }

        BitSet bs = new BitSet(8);
        for (int i = 0; i < 8; i++) {
            bs.set(7 - i, bitstring.charAt(i) == '1');
        }

        return bs.toByteArray()[0];
    }
}
