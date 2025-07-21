package com.spring.implementation.controller;

import com.spring.implementation.exception.DuplicateResourceException;
import com.spring.implementation.exception.ResourceNotFoundException;
import com.spring.implementation.model.ErrorResponse;
import com.spring.implementation.model.Users;
import com.spring.implementation.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("cloudseal/v1/api")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {
        try {
            log.info("register user: {}", user);
            return ResponseEntity.ok(service.register(user));

        } catch (ResourceNotFoundException  | DuplicateResourceException ex) {
            return ResponseEntity
                    .status(ex.getStatus())
                    .body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Users user) {
        log.info("login user: {}", user);
        String resp = service.verify(user);
        if (resp != null && !resp.isEmpty()) {
            return ResponseEntity.ok(resp); // Token or success message
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found or invalid credentials");
        }

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Users> getUser(@PathVariable Integer id) {
        log.info("get user: {}", id);
        return service.loadUserById(id);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Users> deleteByUser(@PathVariable Integer id) {
        log.info("get user: {}", id);
        service.deleteById(id);
        return  ResponseEntity.noContent().build();
    }


}