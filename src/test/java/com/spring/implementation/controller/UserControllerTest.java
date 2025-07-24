package com.spring.implementation.controller;

import com.spring.implementation.exception.DuplicateResourceException;
import com.spring.implementation.exception.ResourceNotFoundException;
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
                .status("Inactive")
            .build();
    }

    @Test
    void testRegisterUser_Success() {
        Users input = mockUser(null, "mahesh");
        Users saved = mockUser(1001, "mahesh");

        when(service.register(input)).thenReturn(saved);

        ResponseEntity<?> response = controller.register(input);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof Users);
        assertEquals("mahesh", ((Users) response.getBody()).getUsername());
    }

    @Test
    void testRegisterUser_DuplicateException() {
        Users input = mockUser(null, "mahesh");
        DuplicateResourceException ex = new DuplicateResourceException("User", "username", "mahesh");

        when(service.register(input)).thenThrow(ex);

        ResponseEntity<?> response = controller.register(input);

        assertEquals(ex.getStatus().value(), response.getStatusCodeValue());
        assertEquals(ex.getMessage(), response.getBody());
    }

    @Test
    void testRegisterUser_ResourceNotFoundException() {
        Users input = mockUser(null, "mahesh");
        ResourceNotFoundException ex = new ResourceNotFoundException("Organization", "id", 99L);

        when(service.register(input)).thenThrow(ex);

        ResponseEntity<?> response = controller.register(input);

        assertEquals(ex.getStatus().value(), response.getStatusCodeValue());
        assertEquals(ex.getMessage(), response.getBody());
    }

    @Test
    void testRegisterUser_GenericException() {
        Users input = mockUser(null, "mahesh");

        when(service.register(input)).thenThrow(new RuntimeException("Unexpected error"));

        ResponseEntity<?> response = controller.register(input);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Unexpected error", response.getBody());
    }


    @Test
    void testUpdateUserStatus_Success() {
        Users user = mockUser(1001, "mahesh");
        user.setStatus("Active");

        when(service.updateUserStatus(1001, "Active")).thenReturn(user);

        ResponseEntity<Users> response = controller.updateUserStatus(1001, "Active");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Active", response.getBody().getStatus());
        assertEquals("mahesh", response.getBody().getUsername());
    }

    @Test
    void testUpdateUserStatus_NotFound() {
        when(service.updateUserStatus(9999, "Inactive"))
                .thenThrow(new RuntimeException("User not found"));

        assertThrows(RuntimeException.class, () -> {
            controller.updateUserStatus(9999, "Inactive");
        });

        verify(service).updateUserStatus(9999, "Inactive");
    }




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
    @Test
    void testDeleteUser_Success() {
        doNothing().when(service).deleteById(1001);

        ResponseEntity<Users> response = controller.deleteByUser(1001);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(service).deleteById(1001);
    }

    @Test
    void testDeleteUser_NotFound() {
        doThrow(new RuntimeException("User not found"))
                .when(service).deleteById(9999);

        assertThrows(RuntimeException.class, () -> controller.deleteByUser(9999));

        verify(service).deleteById(9999);
    }



}