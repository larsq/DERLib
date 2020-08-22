package com.github.larsq.der.octet;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DERClassTest {
    @Test
    void decodeByValue() {
        assertAll(
                Arrays.stream(DERClass.values())
                        .map(type -> () -> assertEquals(type, DERClass.valueOf(type.value)))
        );
    }

    @Test
    void raiseExceptionOnUnknownValue() {
        assertThrows(IllegalArgumentException.class, () -> DERClass.valueOf(63));
    }
}