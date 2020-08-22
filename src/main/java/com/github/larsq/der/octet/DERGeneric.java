package com.github.larsq.der.octet;

import com.github.larsq.der.DEREntity;

public class DERGeneric implements DEREntity<byte[]> {
    private final int tag;
    private final byte[] content;

    DERGeneric(int tag, byte[] content) {
        this.tag = tag;
        this.content = content;
    }

    @Override
    public byte[] value() {
        return new byte[0];
    }

    @Override
    public String toString() {
        return "DERGenericEntity{" +
                "tag=" + tag +
                ", content=" + content.length + " bytes" +
                '}';
    }
}
