package com.github.larsq.der;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.github.larsq.der.TestDEREntityFactory.createInteger;
import static com.github.larsq.der.TestDEREntityFactory.createSequence;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DERSequenceTest {
    @Test
    void values() {
        DERSequence sequence = createSequence(createInteger(0));

        assertEquals(Collections.singletonList(createInteger(0)), sequence.value());
    }

    @Test
    void element() {
        DERSequence sequence = createSequence(createInteger(0));

        assertEquals(createInteger(0), sequence.element(0, DERInteger.class));
        assertThrows(ClassCastException.class, () -> sequence.element(0, DERNull.class));
    }

    @Test
    void equalsAndHashcode() {
        DERSequence instance = createSequence(createInteger(0));
        DERSequence other = createSequence(createInteger(0));

        TestSupport.assertEqualsAndHashcode(instance, other);
    }
}
