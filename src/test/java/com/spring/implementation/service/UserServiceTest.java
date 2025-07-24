package com.spring.implementation.service;

import com.spring.implementation.events.EventPublisherService;
import com.spring.implementation.exception.DuplicateResourceException;
import com.spring.implementation.exception.ResourceNotFoundException;
import com.spring.implementation.model.Organizations;
import com.spring.implementation.model.Users;
import com.spring.implementation.repository.OrganizationRepository;
import com.spring.implementation.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.spring.implementation.events.UserRegisteredEvent;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private UserRepo userRepo;


    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private EventPublisherService publisher;


    @InjectMocks
    private UserService service;

    private Organizations mockOrg() {
        return Organizations.builder()
                .id(101)
                .name("CloudCorp")
               // .type("service")
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Users mockUser() {
        return Users.builder()
                .username("gopi.dev")
                .password("securePass")
                .role("admin")
                .organizations(mockOrg())
                .build();
    }

    private Users buildUser() {
        Organizations org = Organizations.builder()
                .name("Cloud Seals HQ")
                .domain("cloudseals.com")
                .build();

        return Users.builder()
                .username("mahesh")
                .email("mahesh@cloudseals.com")
                .password("mahesh@123")
                .role("admin")
                .organizations(org)
                .build();
    }


    @BeforeEach
    void clearRandomIds() {
        //   when(repo.existsById(anyInt())).thenReturn(false); // simplify unique ID check
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        Users inputUser = buildUser();
        inputUser.setId(null);
        Organizations savedOrg = Organizations.builder().id(1).name("Cloud Seals HQ").domain("cloudseals.com").build();

        when(userRepo.findByUsername("mahesh")).thenReturn(null);
        when(userRepo.existsById(anyInt())).thenReturn(false);
        when(organizationRepository.save(any())).thenReturn(savedOrg);
        when(userRepo.save(any())).thenAnswer(invocation -> {
            Users user = invocation.getArgument(0);
            user.setId(1001); // simulate DB-assigned ID
            return user;
        });

        Users result = service.register(inputUser);

        assertNotNull(result);
        assertEquals("mahesh", result.getUsername());
        assertEquals(1001, result.getId());
        verify(publisher).publish(any(UserRegisteredEvent.class));
    }

    @Test
    void testLoadUserById_Found() {
        Users user = mockUser();
        user.setId(2);
        lenient().when(userRepo.existsById(anyInt())).thenReturn(false);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.loadUserById(2));
        assertEquals("User not found with id: 2", ex.getMessage());
    }

    @Test
    void verify_validCredentials_shouldReturnToken() {
        Users user = Users.builder()
                .username("gopi.dev")
                .password("securePass")
                .build();

        Authentication authMock = mock(Authentication.class);

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authMock);

        when(authMock.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken("gopi.dev")).thenReturn("jwt-token");

        String result = service.verify(user);

        assertEquals("jwt-token", result);
        verify(authManager).authenticate(any());
        verify(jwtService).generateToken("gopi.dev");
    }

    @Test
    void shouldThrowExceptionForDuplicateUsername() {
        Users inputUser = buildUser();
        when(userRepo.findByUsername("mahesh")).thenReturn(inputUser);

        assertThrows(DuplicateResourceException.class, () -> service.register(inputUser));
        verify(userRepo, never()).save(any());
    }


    @Test
    void verify_invalidCredentials_shouldReturnFail() {
        Users user = Users.builder()
                .username("invalidUser")
                .password("badPass")
                .build();

        Authentication authMock = mock(Authentication.class);

        when(authManager.authenticate(any())).thenReturn(authMock);
        when(authMock.isAuthenticated()).thenReturn(false);

        String result = service.verify(user);

        assertEquals("fail", result);
        verify(authManager).authenticate(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void loadUserById_found_shouldReturnUser() {
        Users mockUser = Users.builder()
                .id(10)
                .username("gopi.dev")
                .role("admin")
                .build();

        when(userRepo.findById(10)).thenReturn(Optional.of(mockUser));

        ResponseEntity<Users> response = service.loadUserById(10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("gopi.dev", response.getBody().getUsername());
    }

    @Test
    void shouldUpdateUserStatusSuccessfully() {
        Users existingUser = buildUser();

        when(userRepo.findById(1001)).thenReturn(Optional.of(existingUser));
        when(userRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Users result = service.updateUserStatus(1001, "Inactive");

        assertEquals("Inactive", result.getStatus());
        verify(userRepo).findById(1001);
        verify(userRepo).save(existingUser);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepo.findById(9999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateUserStatus(9999, "Inactive"));

        verify(userRepo).findById(9999);
        verify(userRepo, never()).save(any());
    }


    @Test
    void loadUserById_notFound_shouldThrowException() {
        when(userRepo.findById(99)).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserById(99);
        });

        assertEquals("User not found with id: 99", ex.getMessage());
    }


}