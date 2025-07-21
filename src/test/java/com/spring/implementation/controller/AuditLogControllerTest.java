package com.spring.implementation.controller;

import com.spring.implementation.model.AuditLog;
import com.spring.implementation.model.Organizations;
import com.spring.implementation.model.AiAgents;
import com.spring.implementation.model.Users;
import com.spring.implementation.service.AuditLogService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditLogControllerTest {

    @Mock
    private AuditLogService service;

    @InjectMocks
    private AuditLogController controller;

    private Organizations mockOrg() {
        return Organizations.builder()
                .id(1)
                .name("CloudCorp")
                .createdAt(LocalDateTime.now())
                .build();
    }

    private AiAgents mockAgent() {
        return AiAgents.builder()
                .id(101)
                .name("InsightBot")
                .status("active")
                .build();
    }

    private Users mockUser() {
        return Users.builder()
                .id(1)
                .username("gopi.dev")
                .role("admin")
                .build();
    }

    private AuditLog buildLog(Integer id, String type) {
        return AuditLog.builder()
                .id(id)
                .eventType(type)
                .eventTime(LocalDateTime.now())
                .detailsJson("{\"ip\":\"127.0.0.1\"}")
              //  .organizations(mockOrg())
              //  .aiAgents(mockAgent())
                //.users(mockUser())
                .build();
    }

    @Test
    void testCreateLog() {
        AuditLog request = buildLog(null, "USER_LOGIN");
        AuditLog saved = buildLog(1, "USER_LOGIN");

        when(service.save(request)).thenReturn(saved);

        ResponseEntity<AuditLog> response = controller.createLog(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("USER_LOGIN", response.getBody().getEventType());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void testFetchAllLogs() {
        AuditLog log1 = buildLog(1, "CREATE");
        AuditLog log2 = buildLog(2, "DELETE");

        when(service.getAllLogs()).thenReturn(List.of(log1, log2));

        ResponseEntity<List<AuditLog>> response = controller.fetchAllLogs();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("CREATE", response.getBody().get(0).getEventType());
    }

    @Test
    void testGetLogById_Found() {
        AuditLog log = buildLog(3, "VIEW");

        when(service.getLogById(3)).thenReturn(Optional.of(log));

        ResponseEntity<AuditLog> response = controller.getLogById(3);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("VIEW", response.getBody().getEventType());
    }

    @Test
    void testGetLogById_NotFound() {
        when(service.getLogById(999)).thenReturn(Optional.empty());

        ResponseEntity<AuditLog> response = controller.getLogById(999);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteLog() {
        doNothing().when(service).deleteById(4);

        ResponseEntity<Void> response = controller.deleteLog(4);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(service).deleteById(4);
    }
}