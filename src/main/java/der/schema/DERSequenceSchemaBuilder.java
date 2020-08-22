package com.github.larsq.der.schema;

public class DERSequenceSchemaBuilder extends AbstractDERSequenceSchemaBuilder<DERSequenceSchemaBuilder> {
    DERSequenceSchemaBuilder() {
        super();
    }

    public DERSequenceSchema build() {
        return new DERSequenceSchema(patterns.toArray(new DERSchemaPattern[0]));
    }
}
