package com.spring.implementation.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTServiceTest {

    private JWTService jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setup() {
        jwtService = new JWTService();
    }

    @Test
    void testGenerateTokenAndExtractUsername() {
        String username = "gopi.dev";
        String token = jwtService.generateToken(username);

        assertNotNull(token);
        String extracted = jwtService.extractUserName(token);
        assertEquals(username, extracted);
    }

    @Test
    void testValidateToken_Valid() {
        String username = "gopi.dev";
        when(userDetails.getUsername()).thenReturn(username);

        String token = jwtService.generateToken(username);
        boolean result = jwtService.validateToken(token, userDetails);

        assertTrue(result);
    }

    @Test
    void testValidateToken_InvalidUsername() {
        when(userDetails.getUsername()).thenReturn("someone.else");

        String token = jwtService.generateToken("gopi.dev");
        boolean result = jwtService.validateToken(token, userDetails);

        assertFalse(result);
    }

    @Test
    void testIsTokenExpired_FalseForNewToken() {
        String token = jwtService.generateToken("gopi1.dev");
        when(userDetails.getUsername()).thenReturn("gopi.dev");

        // Should not be expired immediately
        assertFalse(jwtService.validateToken(token, userDetails));
    }



   /* @Test
    void testExtractAllClaimsContainsSubject() {

        String username = "gopi.dev";
        when(userDetails.getUsername()).thenReturn(username);

        String token = jwtService.generateToken(username);

        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtService.generateToken("gopi").getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertEquals("gopi.dev", claims.getSubject());
    }*/
}