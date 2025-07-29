package com.spring.implementation.controller;


import com.spring.implementation.model.AiAgents;
import com.spring.implementation.service.AIAgentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cloudseal/v1/api/aiagent")
@RequiredArgsConstructor
@Tag(name = "AI Agent Controller", description = "CRUD operations for AI Agents")
public class AIAgentController {

    private final AIAgentService service;

    @Operation(summary = "Create a new AI Agent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "AI Agent created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<AiAgents> create(@RequestBody AiAgents agent) {
        return ResponseEntity.ok(service.createAgent(agent));
    }

    @Operation(summary = "Get all AI Agents")
    @ApiResponse(responseCode = "200", description = "Fetched all AI Agents")
    @GetMapping
    public List<AiAgents> getAll() {
        return service.getAllAgents();
    }

    @Operation(summary = "Get AI Agent by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "AI Agent found"),
            @ApiResponse(responseCode = "404", description = "AI Agent not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AiAgents> getById(@PathVariable Integer id) {
        return service.getAgentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update existing AI Agent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "AI Agent updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AiAgents> update(@RequestBody AiAgents agent) {
        return ResponseEntity.ok(service.updateAgent(agent));
    }

    @Operation(summary = "Delete AI Agent by ID")
    @ApiResponse(responseCode = "204", description = "AI Agent deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteAgent(id);
        return ResponseEntity.noContent().build();
    }
}