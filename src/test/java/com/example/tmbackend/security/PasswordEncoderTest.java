package com.example.tmbackend.security;


import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PasswordEncoderTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void testPasswordMatches() {
        // Mot de passe en clair
        String motDePasse = "1234";

        // Mot de passe déjà hashé (à partir de bcrypt)
        String hash = "$2a$11$b151cDIdSHpTz4adcBLIz.ldncBEQB5Iyn6pWGk71aeZJXce/JvRC";

        // Vérifie que le mot de passe correspond au hash
        assertTrue(passwordEncoder.matches(motDePasse, hash));
    }

    @Test
    void testPasswordDoesNotMatch() {
        // Mauvais mot de passe
        String motDePasseFaux = "5678";

        // Hash attendu pour "1234"
        String hash = "$2a$12$8dtDv99m2AHCjQpb2Mi7V.ynJLKGSqOKzmW.gBHwc7TOIW8zQWhU";

        // Vérifie que le faux mot de passe ne correspond pas
        assertFalse(passwordEncoder.matches(motDePasseFaux, hash));
    }

}
