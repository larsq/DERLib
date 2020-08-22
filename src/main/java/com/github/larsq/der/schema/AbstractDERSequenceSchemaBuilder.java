package com.github.larsq.der.schema;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
abstract class AbstractDERSequenceSchemaBuilder<T extends AbstractDERSequenceSchemaBuilder<T>> {
    protected final List<DERSchemaPattern> patterns;

    public AbstractDERSequenceSchemaBuilder() {
        this.patterns = new ArrayList<>();
    }

    public T anyInteger() {
        patterns.add(new DERSchemaPatterns.AnyInteger());
        return (T) this;
    }

    public T integer(BigInteger value) {
        patterns.add(new DERSchemaPatterns.IntegerValue(value));
        return (T) this;
    }

    public T anyOid() {
        patterns.add(new DERSchemaPatterns.AnyOid());
        return (T) this;
    }

    public T matchesOid(String pattern) {
        patterns.add(new DERSchemaPatterns.OidPattern(pattern));
        return (T) this;
    }

    public T nullValue() {
        patterns.add(new DERSchemaPatterns.NullInstance());
        return (T) this;
    }

    public T anySequence() {
        patterns.add(new DERSchemaPatterns.AnySequence());
        return (T) this;
    }

    public T sequence(DERSequenceSchema schema) {
        patterns.add(new DERSchemaPatterns.SequenceSchema(schema));
        return (T) this;
    }

    public T anyOctetString() {
        patterns.add(new DERSchemaPatterns.AnyOctetString());
        return (T) this;
    }

    public T octetString(DERSequenceSchema schema) {
        patterns.add(new DERSchemaPatterns.OctetStringAsSequence(schema));
        return (T) this;
    }
}
