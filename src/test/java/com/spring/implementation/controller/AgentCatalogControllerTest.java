package com.spring.implementation.controller;

import com.spring.implementation.model.AgentCatalog;
import com.spring.implementation.service.AgentCatalogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgentCatalogControllerTest {

    @Mock
    private AgentCatalogService service;

    @InjectMocks
    private AgentCatalogController controller;

    @Test
    void testCreateAgentCatalog() {
        AgentCatalog request = new AgentCatalog();
        request.setAgentName("GopiOps");
        request.setAgentType(AgentCatalog.AgentType.DevOps);
        request.setVersion("v1.0.0");
        request.setConfigJson("{\"logLevel\":\"DEBUG\"}");
        request.setCreatedAt(LocalDateTime.now());
        request.setLastUpdatedAt(LocalDateTime.now());
        request.setStatus(AgentCatalog.AgentStatus.active);

        AgentCatalog saved = new AgentCatalog();
        saved.setCatalogId(1);
        saved.setAgentName("GopiOps");
        saved.setAgentType(request.getAgentType());
        saved.setVersion(request.getVersion());
        saved.setConfigJson(request.getConfigJson());
        saved.setCreatedAt(request.getCreatedAt());
        saved.setLastUpdatedAt(request.getLastUpdatedAt());
        saved.setStatus(request.getStatus());

        when(service.save(request)).thenReturn(saved);

        var response = controller.create(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("GopiOps", response.getBody().getAgentName());
        assertEquals(AgentCatalog.AgentStatus.active, response.getBody().getStatus());
    }

    @Test
    void testGetAllAgentCatalogs() {
        AgentCatalog catalog1 = new AgentCatalog(1, "GopiOps", AgentCatalog.AgentType.DevOps,
                "v1.0", "{\"logLevel\":\"INFO\"}", LocalDateTime.now(), LocalDateTime.now(),
                AgentCatalog.AgentStatus.active);

        AgentCatalog catalog2 = new AgentCatalog(2, "SecureBot", AgentCatalog.AgentType.SecOps,
                "v2.1", "{\"logLevel\":\"WARN\"}", LocalDateTime.now(), LocalDateTime.now(),
                AgentCatalog.AgentStatus.inactive);

        when(service.findAll()).thenReturn(List.of(catalog1, catalog2));

        List<AgentCatalog> result = controller.getAll();

        assertEquals(2, result.size());
        assertEquals("GopiOps", result.get(0).getAgentName());
        assertEquals("SecureBot", result.get(1).getAgentName());
        assertEquals(AgentCatalog.AgentStatus.inactive, result.get(1).getStatus());
    }
}