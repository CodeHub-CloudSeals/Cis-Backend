package com.spring.implementation.controller;

import com.spring.implementation.exception.DuplicateResourceException;
import com.spring.implementation.exception.ResourceNotFoundException;
import com.spring.implementation.model.Users;
import com.spring.implementation.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;


@Slf4j
@RestController
@RequestMapping("cloudseal/v1/api")
@Tag(name = "User Controller", description = "Operations related to user registration, login, and management")
public class UserController {

    @Autowired
    private UserService service;

    @Operation(summary = "Register a new user", description = "Adds a user with organization details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate user")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {
        try {
            log.info("register user: {}", user);
            return ResponseEntity.ok(service.register(user));
        } catch (ResourceNotFoundException | DuplicateResourceException ex) {
            return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(summary = "Login user", description = "Verifies user credentials and returns token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized or invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user) {
        log.info("login user: {}", user);
        String resp = service.verify(user);
        if (resp != null && !resp.isEmpty()) {
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found or invalid credentials");
        }
    }

    @Operation(summary = "Get user by ID", description = "Fetches user details by user ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<Users> getUser(@PathVariable Integer id) {
        log.info("get user: {}", id);
        return service.loadUserById(id);
    }

    @Operation(summary = "Update user status", description = "Updates the status of a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User status updated"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters")
    })
    @PutMapping("/user/status")
    public ResponseEntity<Users> updateUserStatus(
            @RequestParam("userId") Integer userId,
            @RequestParam("status") String status) {
        log.info("Received request to update status of user {} to {}", userId, status);
        Users updatedUser = service.updateUserStatus(userId, status);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete user by ID", description = "Deletes a user from the system")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Users> deleteByUser(@PathVariable Integer id) {
        log.info("delete user: {}", id);
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
