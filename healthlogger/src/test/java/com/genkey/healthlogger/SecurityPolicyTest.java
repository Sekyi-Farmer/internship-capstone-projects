package com.genkey.healthlogger;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityPolicyTest {

    @Test
    void securityPolicyContainsRequiredSections() throws Exception {
        Path policy = Path.of("SECURITY.md");
        String content = Files.readString(policy);

        assertTrue(content.contains("## Overview"));
        assertTrue(content.contains("## Reporting a Vulnerability"));
        assertTrue(content.contains("## Event Store"));
        assertTrue(content.contains("## HTTP Bind Address"));
        assertTrue(content.contains("## Scope and Limitations"));
        assertTrue(content.contains("privately"));
        assertTrue(content.contains("project supervisor"));
        assertTrue(content.contains("repository maintainer"));
        assertTrue(content.contains("revoke or rotate"));
        assertTrue(content.contains("Git history"));
        assertTrue(content.contains("127.0.0.1"));
    }
}