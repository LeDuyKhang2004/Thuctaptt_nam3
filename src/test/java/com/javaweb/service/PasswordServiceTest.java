package com.javaweb.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordServiceTest {

    private final PasswordService passwordService = new PasswordService();

    @Test
    void hashesAndVerifiesPassword() {
        String encoded = passwordService.hash("mat-khau-an-toan");

        assertNotEquals("mat-khau-an-toan", encoded);
        assertTrue(passwordService.isEncoded(encoded));
        assertTrue(passwordService.matches("mat-khau-an-toan", encoded));
        assertFalse(passwordService.matches("sai-mat-khau", encoded));
    }

    @Test
    void acceptsLegacyPlainTextOnlyWhenItMatches() {
        assertTrue(passwordService.matches("legacy-password", "legacy-password"));
        assertFalse(passwordService.matches("wrong", "legacy-password"));
    }
}
