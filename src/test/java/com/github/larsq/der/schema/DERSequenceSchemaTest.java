package com.github.larsq.der.schema;

import org.junit.jupiter.api.Test;

import static com.github.larsq.der.TestDEREntityFactory.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DERSequenceSchemaTest {
    @Test
    void givenEmptySequence_shouldMatchEmptyPattern() {
        DERSequenceSchema schema = DERSequenceSchema.builder().build();

        assertTrue(createSequence().verify(schema));
    }

    @Test
    void givenEmptySequence_shouldNotMatchNonEmptyPattern() {
        DERSequenceSchema schema = DERSequenceSchema.builder().anyInteger().build();

        assertFalse(createSequence().verify(schema));
    }

    @Test
    void givenSingleFlatSequence() {
        DERSequenceSchema schema = DERSequenceSchema.builder().anyInteger().build();

        assertTrue(schema.validate(createSequence(createInteger(0))));
        assertFalse(schema.validate(createSequence(createInteger(0), createInteger(1))));
    }

    @Test
    void givenDoubleFlatSequence() {
        DERSequenceSchema schema = DERSequenceSchema.builder().anyInteger().anyInteger().build();

        assertFalse(schema.validate(createSequence(createInteger(0))));
        assertTrue(schema.validate(createSequence(createInteger(0), createInteger(1))));
    }

    @Test
    void givenDoubleFlatSequenceInWrongOrder() {
        DERSequenceSchema schema = DERSequenceSchema.builder().anyInteger().anyOid().build();

        assertFalse(schema.validate(createSequence(createOid("1.2.15"), createInteger(1))));
        assertTrue(schema.validate(createSequence(createInteger(1), createOid("1.2.15"))));
    }

}