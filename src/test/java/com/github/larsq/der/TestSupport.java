package com.github.larsq.der;

import static org.junit.jupiter.api.Assertions.*;

public class TestSupport {
    static <T> void assertEqualsAndHashcode(T instance, T other) {
        assertAll("equalsAndHashcode",
                () -> assertEquals(instance, instance),
                () -> assertNotEquals(null, instance),
                () -> assertNotEquals(10, instance),
                () -> assertEquals(other, instance),
                () -> assertEquals(other.hashCode(), instance.hashCode())
        );
    }
}
