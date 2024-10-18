package com.jialin.BulletinBoard.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {"jwt.secret=secretkey123secretkey123secretkey123secretkey123"})
class JwtUtilTests {

    @Autowired
    private JwtUtil jwtUtil;

    private final UserDetails userDetails = new User("johndoe", "", Collections.emptyList());

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(token);
        assertEquals("johndoe", jwtUtil.extractUsername(token));
    }

    @Test
    void testValidateToken() {
        String token = jwtUtil.generateToken(userDetails);
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void testValidateTokenExpired() {
        String token = jwtUtil.generateToken(userDetails);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assertFalse(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken(userDetails);
        assertEquals("johndoe", jwtUtil.extractUsername(token));
    }

    @Test
    void testIsTokenExpiredFalse() {
        String token = jwtUtil.generateToken(userDetails);
        assertFalse(jwtUtil.isTokenExpired(token));
    }

    @Test
    void testIsTokenExpiredTrue() {
        String token = jwtUtil.generateToken(userDetails);
        try {
            // sleep to ensure the token expires if set for a very short expiration
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assertTrue(jwtUtil.isTokenExpired(token));
    }
}
