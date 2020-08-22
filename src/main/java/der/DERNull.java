package com.github.larsq.der;

public class DERNull implements DEREntity<Void> {
    static final DERNull INSTANCE = new DERNull();

    private DERNull() {
        //Explicit empty
    }

    public static DERNull decode(byte[] content) {
        if (content.length != 0) {
            throw new IllegalArgumentException("Expected no content");
        }

        return INSTANCE;
    }

    @Override
    public Void value() {
        return null;
    }

    @Override
    public String toString() {
        return "DERNull{}";
    }
}
