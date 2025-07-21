package com.spring.implementation.controller;

import com.spring.implementation.model.AuditLog;
import com.spring.implementation.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cloudseal/v1/api/auditlogs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @PostMapping
    public ResponseEntity<AuditLog> createLog(@RequestBody AuditLog auditLog) {
        return ResponseEntity.ok(auditLogService.save(auditLog));
    }

    @GetMapping
    public ResponseEntity<List<AuditLog>> fetchAllLogs() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLog> getLogById(@PathVariable Integer id) {
        return auditLogService.getLogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Integer id) {
        auditLogService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}