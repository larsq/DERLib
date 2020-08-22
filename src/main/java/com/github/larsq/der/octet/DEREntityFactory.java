package com.github.larsq.der.octet;

import com.github.larsq.der.*;

public class DEREntityFactory {
    public DEREntity<?> create(int tag, byte[] content) {
        switch (tag) {
            case 0x2:
                return DERInteger.decode(content);
            case 0x4:
                return DEROctetString.decode(content);
            case 0x5:
                return DERNull.decode(content);
            case 0x6:
                return DERObjectIdentifier.decode(content);
            case 0x10:
                return DERSequence.decode(content);
            default:
                return new DERGeneric(tag, content);
        }

    }
}
