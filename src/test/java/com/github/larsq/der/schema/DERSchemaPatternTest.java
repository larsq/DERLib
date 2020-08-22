package com.github.larsq.der.schema;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.larsq.der.TestDEREntityFactory.*;
import static com.github.larsq.der.schema.MatchResult.MATCH;
import static com.github.larsq.der.schema.MatchResult.NOT_MATCH;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DERSchemaPatternTest {
    @Test
    void matchesDERInteger() {
        DERSchemaPatterns.AnyInteger pattern = new DERSchemaPatterns.AnyInteger();

        assertEquals(MATCH, pattern.matches(createInteger(0)));
        assertEquals(NOT_MATCH, pattern.matches(nullInstance()));
    }

    @Test
    void matchesDERNull() {
        DERSchemaPatterns.NullInstance pattern = new DERSchemaPatterns.NullInstance();

        assertEquals(MATCH, pattern.matches(nullInstance()));
        assertEquals(NOT_MATCH, pattern.matches(createInteger(1)));
    }

    @Test
    void matchedAnyOid() {
        DERSchemaPatterns.AnyOid pattern = new DERSchemaPatterns.AnyOid();

        assertEquals(MATCH, pattern.matches(createOid("1.2.814.15")));
        assertEquals(NOT_MATCH, pattern.matches(createInteger(1)));

    }

    @Test
    void matchesNestedSequence() {
        DERSchemaPatterns.AnySequence sequence = new DERSchemaPatterns.AnySequence();

        assertEquals(MATCH, sequence.matches(createSequence(createInteger(0))));
    }

    @Test
    void matchesInteger() {
        DERSchemaPatterns.IntegerValue integerValue = new DERSchemaPatterns.IntegerValue(BigInteger.valueOf(2));

        assertEquals(MATCH, integerValue.matches(createInteger(2)));
        assertEquals(NOT_MATCH, integerValue.matches(createInteger(4)));
        assertEquals(NOT_MATCH, integerValue.matches(createOid("1.2")));
    }

    @Test
    void matchesOidPattern() {
        String[] OIDS = new String[]{"1.2", "1.2.814", "1.2.814.1"};

        assertAll(
                () -> assertMatchOid("1.2.814", OIDS, 1),
                () -> assertMatchOid("1.2.*", OIDS, 1),
                () -> assertMatchOid("1.2.**", OIDS, 1, 2),
                () -> assertMatchOid("1.2**", OIDS, 0, 1, 2)
        );
    }

    private void assertMatchOid(String pattern, String[] candidates, int... matchingIndex) {
        Set<String> matchingCandidates = Arrays.stream(matchingIndex)
                .mapToObj(i -> candidates[i]).collect(Collectors.toSet());

        DERSchemaPatterns.OidPattern p = new DERSchemaPatterns.OidPattern(pattern);
        for (String candidate : candidates) {
            assertEquals(matchingCandidates.contains(candidate) ? MATCH : NOT_MATCH, p.matches(createOid(candidate)),
                    () -> String.format("%s:%s", pattern, candidate));
        }
    }
}