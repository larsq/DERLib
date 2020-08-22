package com.github.larsq.der;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DERNullTest {
    @Test
    void returnsSameInstance() {
        assertSame(DERNull.INSTANCE, DERNull.decode(new byte[0]));
    }

    @Test
    void alwaysReturnsNull() {
        assertNull(DERNull.INSTANCE.value());
    }

    @Test
    void textRepresentation() {
        assertEquals("DERNull", DERNull.INSTANCE.toString());
    }

    @Test
    void decodeExpectsNoContent() {
        assertThrows(IllegalArgumentException.class, () -> DERNull.decode(new byte[]{0x0}));
    }

}