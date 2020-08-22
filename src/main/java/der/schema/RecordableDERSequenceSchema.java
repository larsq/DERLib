package com.github.larsq.der.schema;

import com.github.larsq.der.DEREntity;
import com.github.larsq.der.DERSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class RecordableDERSequenceSchema<E extends Enum<E>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordableDERSequenceSchema.class);
    private final Class<E> clz;
    private final DERSchemaPattern[] patterns;

    RecordableDERSequenceSchema(DERSchemaPattern[] patterns, Class<E> clz) {
        this.patterns = patterns;
        this.clz = clz;
    }

    public ValidationContext<E> validate(DERSequence sequence) {
        ValidationContext<E> context = new ValidationContext<>(clz);

        boolean match = validate(sequence, context);
        context.setMatch(match);

        return context;
    }

    @SuppressWarnings("unchecked")
    private boolean validate(DERSequence sequence, ValidationContext<E> context) {
        Iterator<DEREntity<?>> entities = sequence.iterator();

        for (DERSchemaPattern pattern : patterns) {
            if (!entities.hasNext()) {
                LOGGER.info("Pre-mature end of sequence");
                return false;
            }

            DEREntity<?> entity = entities.next();

            MatchResult result;
            if (pattern instanceof DERSchemaPatterns.RecordableNestedType) {
                DERSchemaPatterns.RecordableNestedType<E, ?> casted = (DERSchemaPatterns.RecordableNestedType<E, ?>) pattern;
                result = casted.matches(entity, context);
            } else if (pattern instanceof DERSchemaPatterns.RecordableSimpleType) {
                DERSchemaPatterns.RecordableSimpleType<E, ?> casted = (DERSchemaPatterns.RecordableSimpleType<E, ?>) pattern;
                result = casted.matches(entity, context);
            } else {
                result = pattern.matches(entity);
            }

            if (result == MatchResult.NOT_MATCH) {
                return false;
            }
        }

        return true;
    }

    public static <E extends Enum<E>> RecordableDERSequenceSchemaBuilder<E> builder(Class<E> clz) {
        return new RecordableDERSequenceSchemaBuilder<>(clz);
    }
}
