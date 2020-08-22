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
                "value=" + values.length + " bytes" +
                '}';
    }

    public static DEROctetString decode(byte[] content) {
        return new DEROctetString(Arrays.copyOf(content, content.length));
    }
}
