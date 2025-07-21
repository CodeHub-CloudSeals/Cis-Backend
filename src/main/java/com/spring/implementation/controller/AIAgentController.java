package com.spring.implementation.controller;


import com.spring.implementation.model.AiAgents;
import com.spring.implementation.service.AIAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cloudseal/v1/api/aiagent")
@RequiredArgsConstructor
public class AIAgentController {

    private final AIAgentService service;

    @PostMapping
    public ResponseEntity<AiAgents> create(@RequestBody AiAgents agent) {
        return ResponseEntity.ok(service.createAgent(agent));
    }

    @GetMapping
    public List<AiAgents> getAll() {
        return service.getAllAgents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AiAgents> getById(@PathVariable Integer id) {
        return service.getAgentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AiAgents> update(@RequestBody AiAgents agent) {
        return ResponseEntity.ok(service.updateAgent(agent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteAgent(id);
        return ResponseEntity.noContent().build();
    }
}