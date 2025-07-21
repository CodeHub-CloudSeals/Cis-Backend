package com.spring.implementation.service;

import com.spring.implementation.model.AgentCatalog;
import com.spring.implementation.repository.AgentCatalogRepository;
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
class AgentCatalogServiceTest {

    @Mock
    private AgentCatalogRepository repository;

    @InjectMocks
    private AgentCatalogService service;

    private AgentCatalog buildCatalog(Integer id, String name, String version) {
        return AgentCatalog.builder()
            .catalogId(id)
            .agentName(name)
            .agentType(AgentCatalog.AgentType.DevOps)
            .version(version)
            .configJson("{\"logLevel\":\"INFO\"}")
            .status(AgentCatalog.AgentStatus.active)
            .createdAt(LocalDateTime.now())
            .lastUpdatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    void testSaveAgentCatalog() {
        AgentCatalog catalog = buildCatalog(null, "GopiOps", "v1.0.0");
        AgentCatalog saved = buildCatalog(1, "GopiOps", "v1.0.0");

        when(repository.save(catalog)).thenReturn(saved);

        AgentCatalog result = service.save(catalog);

        assertNotNull(result);
        assertEquals("GopiOps", result.getAgentName());
        assertEquals(1, result.getCatalogId());
    }

    @Test
    void testFindAllAgentCatalogs() {
        AgentCatalog c1 = buildCatalog(1, "OpsBot", "v1.2");
        AgentCatalog c2 = buildCatalog(2, "SecBot", "v1.3");

        when(repository.findAll()).thenReturn(List.of(c1, c2));

        List<AgentCatalog> result = service.findAll();

        assertEquals(2, result.size());
        assertEquals("OpsBot", result.get(0).getAgentName());
        assertEquals("SecBot", result.get(1).getAgentName());
    }
}