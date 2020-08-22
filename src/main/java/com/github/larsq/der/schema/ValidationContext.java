package com.github.larsq.der.schema;

import com.github.larsq.der.DEREntity;
import com.github.larsq.der.DERInteger;

import java.math.BigInteger;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class ValidationContext<T extends Enum<T>> {
    private Map<T, DEREntity<?>> map;
    private boolean match;

    public ValidationContext(Class<T> enumClz) {
        map = new EnumMap<>(enumClz);
    }

    public <V, S extends DEREntity<V>> V get(T key, Class<S> type) {
        return type.cast(map.get(key)).value();
    }

    public BigInteger getInteger(T key) {
        return ((DERInteger) map.get(key)).value();
    }

    void setMatch(boolean match) {
        this.match = match;

        if (!match) {
            map = Collections.emptyMap();
        } else {
            map = Collections.unmodifiableMap(map);
        }
    }

    void set(T key, DEREntity<?> value) {
        if (map.containsKey(key)) {
            throw new IllegalArgumentException("Value already defined for key: " + key);
        }

        map.put(key, value);
    }

    public boolean isMatch() {
        return match;
    }

    void mergeWith(ValidationContext<T> other) {
        map.putAll(other.map);
    }
}
