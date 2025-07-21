package com.spring.implementation.service;

import com.spring.implementation.model.AuditLog;
import com.spring.implementation.model.AiAgents;
import com.spring.implementation.repository.AIAgentRepository;
import com.spring.implementation.repository.AuditLogRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private AIAgentRepository aiAgentRepository;

    @InjectMocks
    private AuditLogService auditLogService;

    private AiAgents mockAgent;
    private AuditLog mockLog;

    @BeforeEach
    void setup() {
        mockAgent = AiAgents.builder()
                .id(101)
                .name("InsightBot")
                .status("active")
                .build();

        mockLog = AuditLog.builder()
                .id(1)
                .eventType("LOGIN")
                .eventTime(LocalDateTime.now())
                .detailsJson("{\"ip\":\"127.0.0.1\"}")
                .agentId(1)
              //  .aiAgents(mockAgent)
                .build();
    }

  /*  @Test
    void save_shouldPersistAuditLog() {
        when(aiAgentRepository.findById(101)).thenReturn(Optional.of(mockAgent));
        when(auditLogRepository.save(mockLog)).thenReturn(mockLog);

        AuditLog saved = auditLogService.save(mockLog);

        assertNotNull(saved);
        assertEquals("LOGIN", saved.getEventType());
        verify(aiAgentRepository).findById(101);
        verify(auditLogRepository).save(mockLog);
    }*/

    /*@Test
    void save_shouldThrowExceptionIfAgentNotFound() {
        when(aiAgentRepository.findById(101)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> auditLogService.save(mockLog));
        assertTrue(ex.getMessage().contains("AIAgent not found"));
        verify(aiAgentRepository).findById(101);
        verifyNoInteractions(auditLogRepository);
    }*/

    @Test
    void getAllLogs_shouldReturnList() {
        when(auditLogRepository.findAll()).thenReturn(List.of(mockLog));

        List<AuditLog> result = auditLogService.getAllLogs();

        assertEquals(1, result.size());
        assertEquals("LOGIN", result.get(0).getEventType());
        verify(auditLogRepository).findAll();
    }

    @Test
    void getLogById_found_shouldReturnLog() {
        when(auditLogRepository.findById(1)).thenReturn(Optional.of(mockLog));

        Optional<AuditLog> result = auditLogService.getLogById(1);

        assertTrue(result.isPresent());
        assertEquals("LOGIN", result.get().getEventType());
    }

    @Test
    void getLogById_notFound_shouldReturnEmpty() {
        when(auditLogRepository.findById(999)).thenReturn(Optional.empty());

        Optional<AuditLog> result = auditLogService.getLogById(999);

        assertFalse(result.isPresent());
    }

    @Test
    void deleteById_shouldInvokeRepository() {
        auditLogService.deleteById(1);

        verify(auditLogRepository).deleteById(1);
    }
}