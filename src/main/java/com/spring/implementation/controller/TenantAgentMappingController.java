package com.spring.implementation.controller;

import com.spring.implementation.model.TenantAgentMapping;
import com.spring.implementation.service.TenantAgentMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cloudseal/v1/api/tenantagent")
@RequiredArgsConstructor
public class TenantAgentMappingController {

    private final TenantAgentMappingService service;

    @PostMapping
    public ResponseEntity<TenantAgentMapping> create(@RequestBody TenantAgentMapping mapping) {
        return ResponseEntity.ok(service.save(mapping));
    }

    @GetMapping
    public List<TenantAgentMapping> getAll() {
        return service.findAll();
    }
}