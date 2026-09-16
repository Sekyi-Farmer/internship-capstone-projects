package com.genkey.configmaster;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Week 3 deliverable: schema and CLI tests.
 *
 * TODO (Week 3):
 * - required field missing
 * - wrong type (port as string)
 * - unknown type in spec is ignored or documented
 * - validate command exit codes
 * - fuzz: truncated JSON, empty object, huge nested maps
 */
class SchemaValidatorTest {

    @Test
    @Disabled("Week 3 — implement required-field and type checks")
    void missingRequiredFieldIsAnIssue() {
        // write this
    }

    @Test
    @Disabled("Week 3 — implement type mismatch")
    void portMustBeInteger() {
        // write this
    }
}
