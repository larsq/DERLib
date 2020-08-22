package com.github.larsq.der;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DERObjectIdentifierTest {
    @Test
    void decode() {
        byte[] bytes = new byte[]{0x2a, (byte) 0x86, 0x48, (byte) 0x86, (byte) 0xf7, 0x0d, 0x01, 0x01, 0x01};
        DERObjectIdentifier objectIdentifier = DERObjectIdentifier.decode(bytes);

        assertEquals("1.2.840.113549.1.1.1", objectIdentifier.value());
    }

    @Test
    void representation() {
        DERObjectIdentifier objectIdentifier = new DERObjectIdentifier(new int[]{1, 2, 840});

        assertEquals("DERObjectIdentifier{1.2.840}", objectIdentifier.toString());
    }

    @Test
    void equalsAndHashcode() {
        DERObjectIdentifier integer = new DERObjectIdentifier(new int[]{1, 2, 3});
        DERObjectIdentifier other = new DERObjectIdentifier(new int[]{1, 2, 3});

        TestSupport.assertEqualsAndHashcode(integer, other);
    }

}