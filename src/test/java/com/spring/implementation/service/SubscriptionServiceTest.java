package com.spring.implementation.service;

import com.spring.implementation.model.Subscription;
import com.spring.implementation.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSubscriptions() {
        Subscription s1 = Subscription.builder().id(1).agentId("AG-01").subscriptionId(1).agentName("Agent One").build();
        Subscription s2 = Subscription.builder().id(2).agentId("AG-02").subscriptionId(1).agentName("Agent Two").build();

        when(subscriptionRepository.findAll()).thenReturn(List.of(s1, s2));

        List<Subscription> result = subscriptionService.getAllSubscriptions();
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getSubscriptionId());
        verify(subscriptionRepository).findAll();
    }

    @Test
    void testGetSubscriptionById_Found() {
        Subscription sub = Subscription.builder().id(1).agentId("AG-01").subscriptionId(1).agentName("Agent One").build();
        when(subscriptionRepository.findById(1)).thenReturn(Optional.of(sub));

        Optional<Subscription> result = subscriptionService.getSubscriptionById(1);
        assertTrue(result.isPresent());
        assertEquals("Agent One", result.get().getAgentName());
        verify(subscriptionRepository).findById(1);
    }

    @Test
    void testGetSubscriptionById_NotFound() {
        when(subscriptionRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Subscription> result = subscriptionService.getSubscriptionById(999);
        assertFalse(result.isPresent());
        verify(subscriptionRepository).findById(999);
    }

    @Test
    void testSaveSubscription() {
        Subscription sub = Subscription.builder().agentId("AG-03").subscriptionId(1).agentName("Agent Three").build();
        Subscription saved = Subscription.builder().id(3).agentId("AG-03").subscriptionId(1).agentName("Agent Three").build();

        when(subscriptionRepository.save(sub)).thenReturn(saved);

        Subscription result = subscriptionService.saveSubscription(sub);
        assertEquals(3, result.getId());
        verify(subscriptionRepository).save(sub);
    }

    @Test
    void testDeleteSubscription() {
        doNothing().when(subscriptionRepository).deleteById(2);

        subscriptionService.deleteSubscription(2);
        verify(subscriptionRepository).deleteById(2);
    }
}