package com.spring.implementation.controller;

import com.spring.implementation.model.Subscription;
import com.spring.implementation.service.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionControllerTest {

    @InjectMocks
    private SubscriptionController subscriptionController;

    @Mock
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSubscriptions() {
        Subscription s1 = Subscription.builder().id(1).agentId("A1").agentName("Alpha").subscriptionId(1).build();
        Subscription s2 = Subscription.builder().id(2).agentId("A2").agentName("Beta").subscriptionId(1).build();

        when(subscriptionService.getAllSubscriptions()).thenReturn(List.of(s1, s2));

        ResponseEntity<List<Subscription>> response = subscriptionController.getAll();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(subscriptionService).getAllSubscriptions();
    }

    @Test
    void testGetSubscriptionById_Found() {
        Subscription sub = Subscription.builder().id(1).agentId("A1").agentName("Alpha").subscriptionId(1).build();
        when(subscriptionService.getSubscriptionById(1)).thenReturn(Optional.of(sub));

        ResponseEntity<Subscription> response = subscriptionController.getById(1);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sub, response.getBody());
        verify(subscriptionService).getSubscriptionById(1);
    }

    @Test
    void testGetSubscriptionById_NotFound() {
        when(subscriptionService.getSubscriptionById(999)).thenReturn(Optional.empty());

        ResponseEntity<Subscription> response = subscriptionController.getById(999);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(subscriptionService).getSubscriptionById(999);
    }

    @Test
    void testCreateSubscription() {
        Subscription newSub = Subscription.builder().agentId("A3").agentName("Gamma").subscriptionId(1).build();
        Subscription savedSub = Subscription.builder().id(3).agentId("A3").agentName("Gamma").subscriptionId(1).build();

        when(subscriptionService.saveSubscription(newSub)).thenReturn(savedSub);

        ResponseEntity<Subscription> response = subscriptionController.create(newSub);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(savedSub, response.getBody());
        verify(subscriptionService).saveSubscription(newSub);
    }

    @Test
    void testDeleteSubscription() {
        doNothing().when(subscriptionService).deleteSubscription(1);

        ResponseEntity<Void> response = subscriptionController.delete(1);
        assertEquals(204, response.getStatusCodeValue());
        verify(subscriptionService).deleteSubscription(1);
    }
}