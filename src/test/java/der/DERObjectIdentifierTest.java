package com.github.larsq.der;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DERObjectIdentifierTest {
    @Test
    void decode() {
        assertEquals("8571.3.2",
                DERObjectIdentifier.decode(new byte[]{
                        (byte) 0xC2, 0x7B, 0x03, 0x02
                }, true).value());

    }
}