package com.github.larsq.der;

import java.math.BigInteger;
import java.util.Arrays;

public class DERInteger implements DEREntity<BigInteger> {
    private final byte[] content;
    private String representation;

    DERInteger(byte[] content) {
        this.content = Arrays.copyOf(content, content.length);
    }


    private String representation() {
        if (representation == null) {
            String number = new BigInteger(content).toString();
            representation = number.length() < 9 ? number : String.format("<%d digits number>", number.length());
        }

        return representation;
    }

    public static DERInteger decode(byte[] content) {
        if (content.length == 0) {
            throw new IllegalArgumentException("Content could not be empty");
        }

        return new DERInteger(content);
    }

    @Override
    public BigInteger value() {
        return new BigInteger(content);
    }

    @Override
    public String toString() {
        return "DERInteger{" +
                "value=" + representation() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DERInteger that = (DERInteger) o;
        return Arrays.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(content);
    }
}
