package com.github.larsq.der;

import java.util.Arrays;

public class DEROctetString implements DEREntity<byte[]> {
    private final byte[] values;

    DEROctetString(byte[] values) {
        this.values = values;
    }

    @Override
    public byte[] value() {
        return Arrays.copyOf(values, values.length);
    }

    @Override
    public String toString() {
        return "DEROctetString{" +
                values.length + " bytes" +
                '}';
    }

    public static DEROctetString decode(byte[] content) {
        return new DEROctetString(Arrays.copyOf(content, content.length));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DEROctetString that = (DEROctetString) o;
        return Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }
}
