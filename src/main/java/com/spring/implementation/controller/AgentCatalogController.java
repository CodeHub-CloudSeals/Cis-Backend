package com.spring.implementation.controller;

import com.spring.implementation.model.AgentCatalog;
import com.spring.implementation.service.AgentCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cloudseal/v1/api/agentcatalog")
@RequiredArgsConstructor
public class AgentCatalogController {

    private final AgentCatalogService service;

    @PostMapping
    public ResponseEntity<AgentCatalog> create(@RequestBody AgentCatalog catalog) {
        return ResponseEntity.ok(service.save(catalog));
    }

    @GetMapping
    public List<AgentCatalog> getAll() {
        return service.findAll();
    }
}