package com.github.larsq.der;

import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Base64;
import java.util.List;

class TestPayload {
    byte[] buffer() {
        try {
            List<String> strings = CharStreams.readLines(new InputStreamReader(getClass().getResourceAsStream("/payload.txt")));
            return Base64.getDecoder().decode(String.join("", strings));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
