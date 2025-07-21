package com.spring.implementation.config;

import com.spring.implementation.service.JWTService;
import com.spring.implementation.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JWTService jwtService;

    @Mock
    private ApplicationContext context;

    @Mock
    private MyUserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtFilter jwtFilter;

    @BeforeEach
    void setupSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testValidTokenAuthentication() throws ServletException, IOException {
        String token = "Bearer mock.jwt.token";
        String username = "gopi";

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtService.extractUserName("mock.jwt.token")).thenReturn(username);
        when(context.getBean(MyUserDetailsService.class)).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.validateToken("mock.jwt.token", userDetails)).thenReturn(true);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        jwtFilter.doFilterInternal(request, response, filterChain);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertTrue(auth instanceof UsernamePasswordAuthenticationToken);
      //  assertEquals(username, ((UserDetails) auth.getPrincipal()).getUsername());

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testInvalidHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void testInvalidTokenFailsValidation() throws ServletException, IOException {
        String token = "Bearer mock.jwt.token";
        String username = "gopi";

        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtService.extractUserName("mock.jwt.token")).thenReturn(username);
        when(context.getBean(MyUserDetailsService.class)).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.validateToken("mock.jwt.token", userDetails)).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }
}