package com.github.larsq.der.octet;

import com.github.larsq.der.DEREntity;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.List;

public class DERParser {
    @SuppressWarnings("unchecked")
    public <T extends DEREntity<?>> T parse(byte[] bytes) {
        List<DEREntity<?>> items = new ArrayList<>();
        new com.github.larsq.der.octet.DERTokenizer(bytes).forEachRemaining(items::add);

        return (T) Iterables.getOnlyElement(items);
    }
}
