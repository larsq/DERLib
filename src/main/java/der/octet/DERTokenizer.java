package com.github.larsq.der.octet;

import com.github.larsq.der.DEREntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.github.larsq.der.octet.DERLengthOctet.DERLengthForm.FULL;

public class DERTokenizer implements Iterator<DEREntity<?>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DERTokenizer.class);

    private final DEREntityFactory factory = new DEREntityFactory();
    private final byte[] buffer;
    private int index;

    public DERTokenizer(byte[] buffer) {
        this.buffer = buffer;
    }

    @Override
    public boolean hasNext() {
        return index < buffer.length;
    }

    @Override
    public DEREntity<?> next() {
        LOGGER.debug("starting at {} of total {}", index, buffer.length);

        if (index == buffer.length) {
            throw new NoSuchElementException();
        }

        int tag = readTag();
        int size = readLength();
        byte[] content = new byte[size];
        readContent(content);

        DEREntity<?> derEntity = factory.create(tag, content);
        LOGGER.info("Creating entity: {}", derEntity);

        return derEntity;
    }

    private void readContent(byte[] buffer) {
        if (buffer.length == 0) {
            return;
        }

        if (reachedEOF(buffer.length)) {
            LOGGER.error("[{}] expects another {} bytes to read: {} remaining", index + 1, buffer.length, this.buffer.length - index);
            throw new IllegalArgumentException("Pre-mature end of stream");
        }

        System.arraycopy(this.buffer, index, buffer, 0, buffer.length);
        index += buffer.length;

        LOGGER.debug("[{}] Content read: {} bytes", index, buffer.length);
    }

    private int readLength() {
        checkReachEOF();
        byte next = buffer[index++];

        DERLengthOctet start = DERLengthOctet.encode(next);

        if (start.form == DERLengthOctet.DERLengthForm.INDEFINITE) {
            throw new UnsupportedOperationException();
        }

        byte[] remainingBytes = start.form != FULL ? new byte[0] : new byte[start.value];

        if (remainingBytes.length > 0) {
            System.arraycopy(buffer, index, remainingBytes, 0, remainingBytes.length);
        }

        index += remainingBytes.length;

        LOGGER.debug("[{}] Length read: {} bytes", index, 1 + remainingBytes.length);
        return DERLengthOctet.size(start, remainingBytes);
    }

    private int readTag() {
        boolean finished = false;

        DERTagOctet start = null;
        ByteBuffer intermediaries = ByteBuffer.allocate(4);

        while (!finished) {
            checkReachEOF();

            byte next = buffer[index++];
            if (start == null) {
                start = DERTagOctet.encode(next);
                finished = !start.continueWithNext;
                LOGGER.debug("[{}] first byte: {}", index, start);
            } else {
                intermediaries.put(next);
                finished = (next & 128) == 0;
            }

        }


        LOGGER.debug("[{}] Tag read: {} bytes", index, 1 + intermediaries.position());
        return DERTagOctet.tag(start, intermediaries.rewind().array());
    }

    private void checkReachEOF() {
        if (reachedEOF(0)) {
            throw new IllegalArgumentException("Pre-mature end of stream");
        }
    }

    private boolean reachedEOF(int delta) {
        return (index + delta) > buffer.length;
    }

}



