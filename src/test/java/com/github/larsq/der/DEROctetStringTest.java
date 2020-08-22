package com.github.larsq.der;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DEROctetStringTest {
    @Test
    void create() {
        byte[] bytes = {1, 2, 3};
        DEROctetString octetString = new DEROctetString(bytes);

        assertNotSame(bytes, octetString.value());
        assertArrayEquals(bytes, octetString.value());
        assertEquals(octetString, DEROctetString.decode(bytes));
    }

    @Test
    void representation() {
        DEROctetString octetString = new DEROctetString(new byte[]{1, 2, 3});

        assertEquals("DEROctetString{3 bytes}", octetString.toString());
    }

    @Test
    void equalsAndHashCode() {
        DEROctetString instance = new DEROctetString(new byte[]{1, 2, 3});
        DEROctetString other = new DEROctetString(new byte[]{1, 2, 3});

        TestSupport.assertEqualsAndHashcode(instance, other);
    }
}