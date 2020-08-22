package com.github.larsq.der.octet;

import java.math.BigInteger;

class DERLengthOctet {
    enum DERLengthForm {
        SHORT, FULL, INDEFINITE;
    }

    final DERLengthForm form;
    final int value;

    DERLengthOctet(DERLengthForm form, int value) {
        this.form = form;
        this.value = value;
    }

    static DERLengthOctet encode(byte value) {
        if ((value & 128) > 0 && (value & 127) == 0) {
            return new DERLengthOctet(DERLengthForm.INDEFINITE, -1);
        }

        if ((value & 128) > 0) {
            return new DERLengthOctet(DERLengthForm.FULL, value & 127);
        }

        return new DERLengthOctet(DERLengthForm.SHORT, value & 127);
    }

    public static int size(DERLengthOctet start, byte[] bytes) {
        if (start.form == DERLengthForm.SHORT) {
            DERUtil.isNotEmpty(bytes);
            return start.value;
        }

        if (start.form == DERLengthForm.INDEFINITE) {
            return -1;
        }

        DERUtil.checkSize(start.value, bytes);
        return new BigInteger(bytes).intValue();
    }
}
