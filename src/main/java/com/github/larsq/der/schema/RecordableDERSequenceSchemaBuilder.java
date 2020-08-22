package com.github.larsq.der.schema;

public class RecordableDERSequenceSchemaBuilder<E extends Enum<E>> extends AbstractDERSequenceSchemaBuilder<RecordableDERSequenceSchemaBuilder<E>> {
    private final Class<E> clz;

    public RecordableDERSequenceSchemaBuilder(Class<E> clz) {
        this.clz = clz;
    }

    public RecordableDERSequenceSchemaBuilder<E> integer(E property) {
        patterns.add(new DERSchemaPatterns.RecordableInteger<>(property));
        return this;
    }


    public RecordableDERSequenceSchemaBuilder<E> oid(E property) {
        patterns.add(new DERSchemaPatterns.RecordableOid<>(property));
        return this;
    }

    public RecordableDERSequenceSchemaBuilder<E> sequence(RecordableDERSequenceSchema<E> schema) {
        patterns.add(new DERSchemaPatterns.RecordableNestedSequence<>(schema));
        return this;
    }

    public RecordableDERSequenceSchemaBuilder<E> octetStringAsSequence(RecordableDERSequenceSchema<E> schema) {
        patterns.add(new DERSchemaPatterns.RecordableNestedOctetString<E>(schema));
        return this;
    }

    public RecordableDERSequenceSchema<E> build() {
        return new RecordableDERSequenceSchema<>(patterns.toArray(new DERSchemaPattern[0]), clz);
    }
}
