DERLIB
======

The intention for this library is to encode/decode according to DER format.

There are plenty of libraries that are doing the same thing and currently so right now don't use this
for production-grade code. 

Motivation
-----------
The focus for this project is to provide means for validating complex structures and to extracting parts for DER sequences

### Schema

```java
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

```

As shown in the example above the sequence is validated to consist a sequence of nine integers where the first one must
be zero


### Recordable scehma

```java
    enum RsaKeyParameter {
        VERSION, MOD, PUB_EXP, PRIV_EXP, P1, P2, EXP1, EXP2, COEFFICIENT
    };


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
```

As shown in the example above the sequence is validated to
consist a sequence of nine integers. The integers are extracted
and stored to a map of values that can be easily captured in the
returning instance to be further validated or used in the code.

# Supported types
Currently the following types are supported:

* Integer, decoding only
* Null, decoding only
* Sequence, decoding only
* Object Identifier, decoding only
* Octet String, decoding only

That is, a minimalistic approach to decode RSA Private Keys

# Building

It is recommended to use ```make``` command. The following targets are available: 

* Clean
* Build
* Assemble
* Version
* Publish (not yet implemented)

The clean, build and assemble targets are using gradle's counterparts. Version return the derived 
application version that is set when assembly is created

## Version

By specifiying ```APPLICATION_VERSION```, a candidate version is set as
```<APPLICATION_VERSION>.<patch version>+<git sha>``` where patch version is the timestamp
when the last commit was made for HEAD. If ```APPLICATION_VERSION``` is not defined,
a dev version is generated as ```dev-<git sha>```.

```bash
$ APPLICATION_VERSION=1.0 make version
1.0.202008221057+745764a.dirty  #dirty means there are uncommited parts

$ make version
dev-745764a.dirty
```