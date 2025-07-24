package com.spring.implementation.service;

import com.spring.implementation.events.EventPublisherService;
import com.spring.implementation.events.UserRegisteredEvent;
import com.spring.implementation.exception.DuplicateResourceException;
import com.spring.implementation.exception.ResourceNotFoundException;

import com.spring.implementation.model.Organizations;

import com.spring.implementation.model.Users;
import com.spring.implementation.repository.OrganizationRepository;
import com.spring.implementation.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Random;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final JWTService jwtService;
    private final AuthenticationManager authManager;
    private final UserRepo userRepo;
    private final OrganizationRepository organizationRepository;
    private final Random random = new Random();
    private final EventPublisherService publisher;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Transactional
    public Users register(Users user) {

        Organizations organization = null;
        Users usr=null;
        /*Organizations org = organizationRepository.findById(
                        user.getOrganizations().getId().longValue())
                .orElseThrow(() -> new ResourceNotFoundException("OrganizationId", "id", user.getOrganizations().getId().longValue()));*/

        if (userRepo.findByUsername(user.getUsername()) != null) {
            throw new DuplicateResourceException("User", "Duplicate username", user.getUsername());
        }

        int randomId;
        user.setPassword(encoder.encode(user.getPassword()));
        do {
            randomId = random.nextInt(999999); // example: random int between 0 and 999999
        } while (userRepo.existsById(randomId)); // ensure uniqueness

        //user.setOrganizations(org);

            if(user.getOrganizations().getId()==null){
                organization=organizationRepository.save(user.getOrganizations());
                user.getOrganizations().setId(organization.getId());
            }

        usr =userRepo.save(user);

            publisher.publish(new UserRegisteredEvent(user.getEmail(), user.getUsername(),usr.getId()));
            log.info("service register user:{}", user);


        return usr;
    }

    public String verify(Users user) {
        log.info("service verify user:{}", user);
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            log.info("verify user isAuthenticated:{}", authentication.isAuthenticated());
            return jwtService.generateToken(user.getUsername());
        } else {
            return "fail";
        }
    }

    public Users updateUserStatus(Integer userId, String status) {
        // Fetch user by ID
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Update status
        user.setStatus(status);

        // Save updated user
        Users updatedUser = userRepo.save(user);
        log.info("Updated status for user {}: {}", userId, status);

        return updatedUser;
    }


    public ResponseEntity<Users> loadUserById(Integer id) throws UsernameNotFoundException {
        return ResponseEntity.ok(userRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id)));
    }

    public void deleteById(Integer id) throws UsernameNotFoundException {
        userRepo.deleteById(id);
    }


}