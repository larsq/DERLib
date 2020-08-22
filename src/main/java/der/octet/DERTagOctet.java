package com.github.larsq.der.octet;

import com.google.common.collect.Iterators;

import java.math.BigInteger;
import java.util.PrimitiveIterator;

class DERTagOctet {
    final DERClass derClass;
    final boolean constructed;
    final int tag;
    final boolean continueWithNext;

    DERTagOctet(DERClass derClass, boolean constructed, int tag, boolean continueWithNext) {
        this.derClass = derClass;
        this.constructed = constructed;
        this.tag = tag;
        this.continueWithNext = continueWithNext;
    }

    static DERTagOctet encode(byte value) {
        return new DERTagOctet(DERClass.valueOf(value & 192), (value & 32) > 0, value & 31, (value & 31) == 31);
    }

    static int tag(DERTagOctet start, byte... intermediaries) {
        if (!start.continueWithNext) {
            return start.tag;
        }

        if (intermediaries.length == 0) {
            throw new IllegalArgumentException("Expecting following tag bytes: got none");
        }

        BigInteger integer = BigInteger.ZERO;
        for (byte intermediary : intermediaries) {
            boolean shouldContinue = (intermediary & 128) > 0;
            integer = integer.shiftLeft(7).add(BigInteger.valueOf(intermediary & 127));

            if (!shouldContinue) {
                return integer.intValueExact();
            }
        }

        throw new IllegalArgumentException("Expected not to continue but could find any more bytes");
    }


    @Override
    public String toString() {
        return "DERTagOctet{" +
                "derClass=" + derClass +
                ", constructed=" + constructed +
                ", value=" + tag +
                ", continueWithNext=" + continueWithNext +
                '}';
    }
}
