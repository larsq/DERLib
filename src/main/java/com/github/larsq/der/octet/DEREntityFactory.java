package com.github.larsq.der.octet;

import com.github.larsq.der.*;

public class DEREntityFactory {


    public DEREntity<?> create(int tag, byte[] content) {
        switch (tag) {
            case 2:
                return DERInteger.decode(content);
            case 4:
                return DEROctetString.decode(content);
            case 5:
                return DERNull.decode(content);
            case 6:
                return DERObjectIdentifier.decode(content, false);
            case 16:
                return DERSequence.decode(content);
            default:
                return new DERGeneric(tag, content);
        }

    }
}
