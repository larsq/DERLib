package com.github.larsq.der.octet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DERGenericTest {
    private static final int TAG = 0xa;
    private static final byte[] CONTENT = {0x1, 0x2, 0x4};

    @Test
    void create() {
        DERGeneric generic = new DERGeneric(TAG, CONTENT);

        assertEquals(TAG, generic.tag());
        assertArrayEquals(CONTENT, generic.value());
    }

    @Test
    void createWithEmptyContent() {
        DERGeneric generic = new DERGeneric(TAG, null);

        assertEquals(TAG, generic.tag());
        assertArrayEquals(new byte[0], generic.value());
    }

    @Test
    void textRepresentation() {
        DERGeneric generic = new DERGeneric(TAG, CONTENT);

        assertEquals("DERGeneric{tag=10, content=3 bytes}", generic.toString());
    }

    @Test
    void equalsAndHashcode() {
        DERGeneric instance = new DERGeneric(TAG, CONTENT);
        DERGeneric clone = new DERGeneric(TAG, CONTENT);

        assertAll("equals and hashcode",
                () -> assertEquals(instance, instance),
                () -> assertNotEquals(null, instance),
                () -> assertNotEquals(10, instance),
                () -> assertEquals(clone, instance),
                () -> assertEquals(instance.hashCode(), clone.hashCode())
        );

    }
}