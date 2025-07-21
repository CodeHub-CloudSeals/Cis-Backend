package com.spring.implementation.service;

import com.spring.implementation.model.UserPrincipal;
import com.spring.implementation.model.Users;
import com.spring.implementation.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private MyUserDetailsService service;

    private Users mockUser(String username) {
        return Users.builder()
                .id(1)
                .username(username)
                .email(username + "@cloudseal.ai")
                .role("admin")
                .password("securePass")
                .build();
    }

    @Test
    void testLoadUserByUsername_Found() {
        String username = "gopi.dev";
        Users user = mockUser(username);

        when(userRepo.findByUsername(username)).thenReturn(user);

        UserDetails result = service.loadUserByUsername(username);

        assertNotNull(result);
        assertTrue(result instanceof UserPrincipal);
        assertEquals("gopi.dev", result.getUsername());
    }

    @Test
    void testLoadUserByUsername_NotFound() {
        when(userRepo.findByUsername("unknown.user")).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername("unknown.user")
        );

        assertEquals("user not found", exception.getMessage());
    }
}