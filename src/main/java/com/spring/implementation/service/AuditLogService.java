package com.spring.implementation.service;

import com.spring.implementation.model.AiAgents;
import com.spring.implementation.model.AuditLog;
import com.spring.implementation.repository.AIAgentRepository;
import com.spring.implementation.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditLogService {


    private final AuditLogRepository auditLogRepository;
    private final AIAgentRepository aiAgentRepository;

    public AuditLog save(AuditLog auditLog) {

        return    auditLogRepository.save(auditLog);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    public Optional<AuditLog> getLogById(Integer id) {
        return auditLogRepository.findById(id);
    }

    public void deleteById(Integer id) {
        auditLogRepository.deleteById(id);
    }
}