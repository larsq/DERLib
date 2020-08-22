package com.github.larsq.der;

import com.github.larsq.der.octet.DERTokenizer;
import com.github.larsq.der.schema.DERSequenceSchema;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DERSequence implements DEREntity<List<? extends DEREntity<?>>>, Iterable<DEREntity<?>> {
    private final List<DEREntity<?>> elements;

    DERSequence(List<DEREntity<?>> elements) {
        this.elements = elements;
    }

    public static DERSequence decode(byte[] content) {
        DERTokenizer tokenizer = new DERTokenizer(content);
        List<DEREntity<?>> entities = new ArrayList<>();
        tokenizer.forEachRemaining(entities::add);

        return new DERSequence(entities);
    }

    @Override
    public List<? extends DEREntity<?>> value() {
        return elements;
    }

    @Override
    public String toString() {
        return "DERSequence{" +
                "elements=" + elements +
                '}';
    }

    @Override
    @Nonnull
    public Iterator<DEREntity<?>> iterator() {
        return elements.iterator();
    }

    public <T extends DEREntity<?>> T element(int index, Class<T> clz) {
        DEREntity<?> entity = elements.get(index);
        if (clz.isInstance(entity)) {
            return clz.cast(entity);
        }

        throw new ClassCastException("Could not get element: instance of " + entity.getClass().getName());
    }

    public boolean verify(DERSequenceSchema schema) {
        return schema.validate(this);
    }
}
