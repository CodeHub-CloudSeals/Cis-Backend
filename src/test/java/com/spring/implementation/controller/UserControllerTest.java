package com.spring.implementation.controller;

import com.spring.implementation.model.Users;
import com.spring.implementation.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    private Users mockUser(Integer id, String username) {
        return Users.builder()
            .id(id)
            .username(username)
            .email(username + "@cloudseal.ai")
            .role("admin")
            .build();
    }

    /*@Test
    void testRegisterUser() {
        Users input = mockUser(null, "gopi.dev");
        Users saved = mockUser(1, "gopi.dev");

        when(service.register(input)).thenReturn(saved);

        Users result = controller.register(input);

        assertNotNull(result);
        assertEquals("gopi.dev", result.getUsername());
        assertEquals(1, result.getId());
    }*/

    @Test
    void testLoginSuccess() {
        Users user = mockUser(null, "gopi.dev");

        when(service.verify(user)).thenReturn("mock-jwt-token");

        ResponseEntity<String> response = controller.login(user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("mock-jwt-token", response.getBody());
    }

    @Test
    void testLoginFailure() {
        Users user = mockUser(null, "invalid.user");

        when(service.verify(user)).thenReturn("");

        ResponseEntity<String> response = controller.login(user);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("User not found or invalid credentials", response.getBody());
    }

    @Test
    void testGetUserById_Found() {
        Users user = mockUser(1, "gopi.dev");
        ResponseEntity<Users> expected = ResponseEntity.of(Optional.of(user));

        when(service.loadUserById(1)).thenReturn(expected);

        ResponseEntity<Users> response = controller.getUser(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("gopi.dev", response.getBody().getUsername());
    }

    @Test
    void testGetUserById_NotFound() {
        ResponseEntity<Users> expected = ResponseEntity.notFound().build();

        when(service.loadUserById(99)).thenReturn(expected);

        ResponseEntity<Users> response = controller.getUser(99);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}