package com.github.larsq.der;

import com.github.larsq.der.octet.DERUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DERObjectIdentifier implements DEREntity<String> {
    private final int[] identifiers;

    DERObjectIdentifier(int[] identifiers) {
        this.identifiers = identifiers;
    }

    @Override
    public String value() {
        return asString();
    }

    public static DERObjectIdentifier decode(byte[] bytes) {
        List<Integer> identifiers = new ArrayList<>();

        int last = 0;
        for (int current = 0; current < bytes.length; current++) {
            boolean isLast = (bytes[current] & 128) == 0;
            if (isLast) {
                identifiers.add(DERUtil.uint7msf(last, current + 1, bytes));
                last = current + 1;
            }
        }

        if (last != bytes.length) {
            throw new IllegalArgumentException("Unexpected value");
        }

        int identifier = identifiers.remove(0);
        identifiers.add(0, identifier % 40);
        identifiers.add(0, identifier / 40);

        return new DERObjectIdentifier(identifiers.stream().mapToInt(Integer::intValue).toArray());
    }

    @Override
    public String toString() {
        return "DERObjectIdentifier{" +
                asString() +
                '}';
    }

    private String asString() {
        return Arrays.stream(identifiers).mapToObj(Integer::toString).collect(Collectors.joining("."));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DERObjectIdentifier that = (DERObjectIdentifier) o;
        return Arrays.equals(identifiers, that.identifiers);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(identifiers);
    }
}
