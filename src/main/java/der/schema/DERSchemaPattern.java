package com.github.larsq.der.schema;

import com.github.larsq.der.*;

interface DERSchemaPattern {
    MatchResult matches(DEREntity<?> entity);
}
