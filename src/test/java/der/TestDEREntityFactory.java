package com.github.larsq.der;

import com.google.common.collect.ImmutableList;

import java.math.BigInteger;
import java.util.Arrays;

public class TestDEREntityFactory {
    public static DERInteger createInteger(int value) {
        return new DERInteger(BigInteger.valueOf(value).toByteArray());
    }

    public static DERSequence createSequence(DEREntity<?>... entities) {
        return new DERSequence(ImmutableList.copyOf(entities));
    }

    public static DERObjectIdentifier createOid(String oid) {
        return new DERObjectIdentifier(Arrays.stream(oid.split("\\.")).mapToInt(Integer::parseInt).toArray());
    }

    public static DERNull nullInstance() {
        return DERNull.INSTANCE;
    }
}
