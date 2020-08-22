package com.github.larsq.der.schema;

import com.github.larsq.der.DEREntity;
import com.github.larsq.der.DERSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class DERSequenceSchema {
    private static final Logger LOGGER = LoggerFactory.getLogger(DERSequenceSchema.class);
    protected final DERSchemaPattern[] patterns;

    DERSequenceSchema(DERSchemaPattern[] patterns) {
        this.patterns = patterns;
    }

    public boolean validate(DERSequence sequence) {
        Iterator<DEREntity<?>> entities = sequence.iterator();

        for (DERSchemaPattern pattern : patterns) {
            boolean finished = false;
            while (!finished) {
                if (!entities.hasNext()) {
                    LOGGER.info("Pre-mature end of sequence");
                    return false;
                }

                DEREntity<?> entity = entities.next();
                MatchResult result = pattern.matches(entity);
                switch (result) {
                    case NOT_MATCH:
                        return false;
                    case MATCH:
                        finished = true;
                        break;
                    default:
                        throw new UnsupportedOperationException("unkown label: " + result);
                }
            }
        }

        if (entities.hasNext()) {
            LOGGER.info("Sequence too long");
            return false;
        }

        return true;
    }

    public static DERSequenceSchemaBuilder builder() {
        return new DERSequenceSchemaBuilder();
    }
}
