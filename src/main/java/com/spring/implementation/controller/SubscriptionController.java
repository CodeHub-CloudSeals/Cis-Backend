package com.spring.implementation.controller;

import com.spring.implementation.model.Subscription;
import com.spring.implementation.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cloudseal/v1/api/subscription")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<List<Subscription>> getAll() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getById(@PathVariable Integer id) {
        return subscriptionService.getSubscriptionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Subscription> create(@RequestBody Subscription subscription) {
        return ResponseEntity.ok(subscriptionService.saveSubscription(subscription));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}