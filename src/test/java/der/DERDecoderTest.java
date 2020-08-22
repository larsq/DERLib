package com.github.larsq.der;

import com.github.larsq.der.octet.DERParser;
import com.github.larsq.der.schema.DERSequenceSchema;
import com.github.larsq.der.schema.RecordableDERSequenceSchema;
import com.github.larsq.der.schema.ValidationContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;

import static com.github.larsq.der.DERDecoderTest.RsaKeyParameter.*;
import static java.math.BigInteger.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DERDecoderTest {
    private static byte[] stream;

    @BeforeAll
    static void setupStream() {
        stream = new TestPayload().buffer();
    }

    @Test
    void compliesToSchema() {
        DERSequence sequence = new DERParser().parse(stream);

        DERSequenceSchema schema = DERSequenceSchema.builder()
                .integer(ZERO)
                .anyInteger()
                .anyInteger()
                .anyInteger()
                .anyInteger()
                .anyInteger()
                .anyInteger()
                .anyInteger()
                .anyInteger()
                .build();

        assertTrue(schema.validate(sequence));
    }

    @Test
    void canCreateKeysFromEncoding() throws Exception {
        DERSequence sequence = new DERParser().parse(stream);

        RecordableDERSequenceSchema<RsaKeyParameter> schema = RecordableDERSequenceSchema.builder(RsaKeyParameter.class)
                .integer(VERSION)
                .integer(MOD)
                .integer(PUB_EXP)
                .integer(PRIV_EXP)
                .integer(P1)
                .integer(P2)
                .integer(EXP1)
                .integer(EXP2)
                .integer(COEFFICIENT)
                .build();

        ValidationContext<RsaKeyParameter> ctx = schema.validate(sequence);

        assertTrue(ctx.isMatch());
        assertEquals(ZERO, ctx.get(VERSION, DERInteger.class));
        assertAsymmetricKey(ctx);
    }

    private void assertAsymmetricKey(ValidationContext<RsaKeyParameter> ctx) throws Exception {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PrivateKey priv = factory.generatePrivate(new RSAPrivateCrtKeySpec(
                ctx.getInteger(MOD),
                ctx.getInteger(PUB_EXP),
                ctx.getInteger(PRIV_EXP),
                ctx.getInteger(P1),
                ctx.getInteger(P2),
                ctx.getInteger(EXP1),
                ctx.getInteger(EXP2),
                ctx.getInteger(COEFFICIENT)
        ));

        PublicKey pub = factory.generatePublic(new RSAPublicKeySpec(ctx.getInteger(MOD), ctx.getInteger(PUB_EXP)));

        String message = "Secret";

        Cipher encrypt = Cipher.getInstance("RSA");
        encrypt.init(Cipher.ENCRYPT_MODE, pub);

        byte[] encrypted = encrypt.doFinal(message.getBytes());

        Cipher decrypt = Cipher.getInstance("RSA");
        decrypt.init(Cipher.DECRYPT_MODE, priv);

        String decrypted = new String(decrypt.doFinal(encrypted));

        assertEquals(message, decrypted);
    }

    enum RsaKeyParameter {
        VERSION, MOD, PUB_EXP, PRIV_EXP, P1, P2, EXP1, EXP2, COEFFICIENT
    }
}