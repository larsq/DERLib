package com.github.larsq.der.octet;

import com.github.larsq.der.DEREntity;

import java.util.Arrays;
import java.util.Objects;

public class DERGeneric implements DEREntity<byte[]> {
    private final int tag;
    private final byte[] content;

    DERGeneric(int tag, byte[] content) {
        this.tag = tag;
        this.content = content;
    }

    public int tag() {
        return tag;
    }

    @Override
    public byte[] value() {
        return content == null ? new byte[0] : Arrays.copyOf(content, content.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DERGeneric that = (DERGeneric) o;
        return tag == that.tag &&
                Arrays.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(tag);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }

    @Override
    public String toString() {
        return "DERGeneric{" +
                "tag=" + tag +
                ", content=" + content.length + " bytes" +
                '}';
    }
}
