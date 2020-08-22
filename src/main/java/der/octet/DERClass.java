package com.github.larsq.der.octet;

enum DERClass {
    UNIVERSAL(0),
    APPLICATION(64),
    CONTEXT_SPECIFIC(128),
    PRIVATE(192);

    private final int value;

    DERClass(int value) {
        this.value = value;
    }

    static DERClass valueOf(int value) {
        for (DERClass clz : values()) {
            if (clz.value == value) {
                return clz;
            }
        }

        throw new IllegalArgumentException("Unsupported value: " + value);
    }
}
