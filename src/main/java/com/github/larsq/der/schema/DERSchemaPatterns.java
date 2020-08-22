package com.github.larsq.der.schema;

import com.github.larsq.der.*;
import com.github.larsq.der.octet.DERParser;
import com.google.common.base.Splitter;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.regex.Pattern;

import static com.github.larsq.der.schema.MatchResult.MATCH;
import static com.github.larsq.der.schema.MatchResult.NOT_MATCH;

class DERSchemaPatterns {
    private DERSchemaPatterns() {
        //Explicit Empty
    }

    abstract static class TypeSafeMatch<T extends DEREntity<?>> extends IsTypeOf<T> {
        protected TypeSafeMatch(Class<T> type) {
            super(type);
        }

        @Override
        public MatchResult matches(DEREntity<?> entity) {
            if (!type.isInstance(entity)) {
                return NOT_MATCH;
            }

            return typedMatches(type.cast(entity));
        }

        abstract MatchResult typedMatches(T instance);
    }

    static class IsTypeOf<T extends DEREntity<?>> implements DERSchemaPattern {
        protected final Class<T> type;

        private IsTypeOf(Class<T> type) {
            this.type = type;
        }

        @Override
        public MatchResult matches(DEREntity<?> entity) {
            return type.isInstance(entity) ? MATCH : NOT_MATCH;
        }
    }

    abstract static class RecordableNestedType<E extends Enum<E>, T extends DEREntity<?>> extends TypeSafeMatch<T> {
        protected final RecordableDERSequenceSchema<E> schema;

        protected RecordableNestedType(Class<T> type, RecordableDERSequenceSchema<E> schema) {
            super(type);
            this.schema = schema;
        }

        MatchResult matches(DEREntity<?> entity, ValidationContext<E> provided) {
            if (type.isInstance(entity)) {
                return typedMatches(type.cast(entity), provided);
            } else {
                provided.setMatch(false);
                return NOT_MATCH;
            }
        }

        protected abstract MatchResult typedMatches(T instance, ValidationContext<E> provided);
    }

    abstract static class RecordableSimpleType<E extends Enum<E>, T extends DEREntity<?>> extends IsTypeOf<T> {
        private final E property;

        RecordableSimpleType(Class<T> type, E property) {
            super(type);
            this.property = property;
        }

        MatchResult matches(DEREntity<?> entity, ValidationContext<E> context) {
            MatchResult result = matches(entity);

            if (result == NOT_MATCH) {
                return result;
            }

            context.set(property, entity);

            return MATCH;
        }
    }


    static class RecordableNestedOctetString<E extends Enum<E>> extends RecordableNestedType<E, DEROctetString> {
        protected RecordableNestedOctetString(RecordableDERSequenceSchema<E> schema) {
            super(DEROctetString.class, schema);
        }

        @Override
        protected MatchResult typedMatches(DEROctetString instance, ValidationContext<E> provided) {
            ValidationContext<E> context = schema.validate(new DERParser().parse(instance.value()));

            if (context.isMatch()) {
                provided.mergeWith(context);
            }

            context.setMatch(context.isMatch());
            return context.isMatch() ? MATCH : NOT_MATCH;
        }

        @Override
        MatchResult typedMatches(DEROctetString instance) {
            return schema.validate(new DERParser().parse(instance.value())).isMatch() ? MATCH : NOT_MATCH;
        }
    }

    static class RecordableNestedSequence<E extends Enum<E>> extends RecordableNestedType<E, DERSequence> {
        RecordableNestedSequence(RecordableDERSequenceSchema<E> schema) {
            super(DERSequence.class, schema);
        }

        @Override
        protected MatchResult typedMatches(DERSequence instance, ValidationContext<E> provided) {
            ValidationContext<E> context = schema.validate(instance);

            if (context.isMatch()) {
                provided.mergeWith(context);
            }

            context.setMatch(context.isMatch());
            return context.isMatch() ? MATCH : NOT_MATCH;
        }

        @Override
        MatchResult typedMatches(DERSequence instance) {
            return schema.validate(instance).isMatch() ? MATCH : NOT_MATCH;
        }
    }

    static class AnySequence extends IsTypeOf<DERSequence> {
        AnySequence() {
            super(DERSequence.class);
        }
    }

    static class AnyInteger extends IsTypeOf<DERInteger> {
        AnyInteger() {
            super(DERInteger.class);
        }
    }

    static class RecordableInteger<E extends Enum<E>> extends RecordableSimpleType<E, DERInteger> {
        RecordableInteger(E property) {
            super(DERInteger.class, property);
        }
    }

    static class RecordableOid<E extends Enum<E>> extends RecordableSimpleType<E, DERObjectIdentifier> {
        RecordableOid(E property) {
            super(DERObjectIdentifier.class, property);
        }
    }

    static class NullInstance extends IsTypeOf<DERNull> {
        NullInstance() {
            super(DERNull.class);
        }
    }

    static class AnyOid extends IsTypeOf<DERObjectIdentifier> {
        AnyOid() {
            super(DERObjectIdentifier.class);
        }
    }

    static class AnyOctetString extends IsTypeOf<DEROctetString> {
        AnyOctetString() {
            super(DEROctetString.class);
        }
    }

    static class IntegerValue extends TypeSafeMatch<DERInteger> {
        private final BigInteger value;

        IntegerValue(BigInteger value) {
            super(DERInteger.class);
            this.value = value;
        }

        @Override
        MatchResult typedMatches(DERInteger instance) {
            return instance.value().equals(value) ? MATCH : NOT_MATCH;
        }
    }

    static class SequenceSchema extends TypeSafeMatch<DERSequence> {
        private final DERSequenceSchema schema;

        protected SequenceSchema(DERSequenceSchema schema) {
            super(DERSequence.class);
            this.schema = schema;
        }

        @Override
        MatchResult typedMatches(DERSequence instance) {
            return schema.validate(instance) ? MATCH : NOT_MATCH;
        }
    }

    static class OidPattern extends TypeSafeMatch<DERObjectIdentifier> {
        private static final Pattern NUMBER_OR_STAR = Pattern.compile("(\\*|[0-9]+)");
        private static final Pattern NUMBER_AND_REMAINING = Pattern.compile("[0-9]+\\*\\*");
        private static final String ANY = "*";
        private static final String REMAINING = "**";
        private final String pattern;

        protected OidPattern(String pattern) {
            super(DERObjectIdentifier.class);
            this.pattern = pattern;
        }

        @Override
        MatchResult typedMatches(DERObjectIdentifier instance) {
            Iterator<String> elements = Splitter.on('.').splitToList(instance.value()).iterator();
            for (String part : Splitter.on('.').splitToList(pattern)) {

                if (isNumberOrStar(part) && elements.hasNext() && matchesNumberOrStar(part, elements.next())) {
                    continue;
                }

                if (isNumberAndRemaining(part) && elements.hasNext() && matchesNumberPart(part, elements.next())) {
                    return MATCH;
                }

                if (isRemaining(part) && elements.hasNext()) {
                    return MATCH;
                }

                return NOT_MATCH;
            }

            return elements.hasNext() ? NOT_MATCH : MATCH;
        }

        private static boolean matchesNumberOrStar(String part, String next) {
            return part.equals(ANY) || part.equals(next);
        }

        private static boolean isNumberAndRemaining(String part) {
            return NUMBER_AND_REMAINING.matcher(part).matches();
        }

        private static boolean isRemaining(String part) {
            return part.equals(REMAINING);
        }

        private static boolean matchesNumberPart(String part, String next) {
            return part.replaceAll("\\*", "").equals(next);
        }

        private static boolean isNumberOrStar(String part) {
            return NUMBER_OR_STAR.matcher(part).matches();
        }
    }

    static class OctetStringAsSequence extends TypeSafeMatch<DEROctetString> {
        private final DERSequenceSchema schema;

        OctetStringAsSequence(DERSequenceSchema schema) {
            super(DEROctetString.class);
            this.schema = schema;
        }

        @Override
        MatchResult typedMatches(DEROctetString instance) {
            DERSequence sequence = new DERParser().parse(instance.value());
            return schema.validate(sequence) ? MATCH : NOT_MATCH;
        }
    }


}
