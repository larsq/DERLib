package com.github.larsq.der.octet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DERUtilTest {
    @Test
    void checkSizeExpectsCorrectSize() {
        assertDoesNotThrow(() -> DERUtil.checkSize(1, new byte[]{0x1}));
        assertThrows(IllegalArgumentException.class, () -> DERUtil.checkSize(0, new byte[]{0x1}));
    }
}