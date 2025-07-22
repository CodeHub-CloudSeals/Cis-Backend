package com.spring.implementation.service;

import com.spring.implementation.events.EventPublisherService;
import com.spring.implementation.events.UserRegisteredEvent;
import com.spring.implementation.exception.DuplicateResourceException;
import com.spring.implementation.exception.ResourceNotFoundException;
import com.spring.implementation.model.ErrorResponse;
import com.spring.implementation.model.Organizations;
import com.spring.implementation.model.UserPrincipal;
import com.spring.implementation.model.Users;
import com.spring.implementation.repository.OrganizationRepository;
import com.spring.implementation.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final JWTService jwtService;
    private final AuthenticationManager authManager;
    private final UserRepo repo;
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

        if (repo.findByUsername(user.getUsername()) != null) {
            throw new DuplicateResourceException("User", "Duplicate username", user.getUsername());
        }

        int randomId;
        user.setPassword(encoder.encode(user.getPassword()));
        do {
            randomId = random.nextInt(999999); // example: random int between 0 and 999999
        } while (repo.existsById(randomId)); // ensure uniqueness

        //user.setOrganizations(org);

        try {

            if(user.getOrganizations().getId()!=null){
                usr =repo.save(user);
            }else{
                organization=organizationRepository.save(user.getOrganizations());
            }


            publisher.publish(new UserRegisteredEvent(user.getEmail(), user.getUsername(),usr.getId()));
            log.info("service register user:{}", user);

        }catch ( ResourceNotFoundException  | DuplicateResourceException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return user;
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

    public ResponseEntity<Users> loadUserById(Integer id) throws UsernameNotFoundException {
        return ResponseEntity.ok(repo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id)));
    }

    public void deleteById(Integer id) throws UsernameNotFoundException {
          repo.deleteById(id);
    }


}