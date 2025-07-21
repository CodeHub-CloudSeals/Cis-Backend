package com.spring.implementation.service;

import com.spring.implementation.model.AgentCatalog;
import com.spring.implementation.model.TenantAgentMapping;
import com.spring.implementation.repository.AgentCatalogRepository;
import com.spring.implementation.repository.TenantAgentMappingRepository;
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
class TenantAgentMappingServiceTest {

    @Mock
    private TenantAgentMappingRepository mappingRepo;

    @Mock
    private AgentCatalogRepository catalogRepo;

    @InjectMocks
    private TenantAgentMappingService service;

    private AgentCatalog mockCatalog(Integer id) {
        return AgentCatalog.builder()
                .catalogId(id)
                .agentName("GopiOps")
                .agentType(AgentCatalog.AgentType.DevOps)
                .version("v1.0.0")
                .configJson("{\"logLevel\":\"INFO\"}")
                .status(AgentCatalog.AgentStatus.active)
                .createdAt(LocalDateTime.now())
                .lastUpdatedAt(LocalDateTime.now())
                .build();
    }

    private TenantAgentMapping buildMapping(Integer id, Integer catalogId) {
        return TenantAgentMapping.builder()
                .mappingId(id)
                .agentCatalog(AgentCatalog.builder().catalogId(catalogId).build())
                .deployedVersion("v1.2.3")
                .deploymentStatus(TenantAgentMapping.DeploymentStatus.pending)
                .tenantSpecificConfigJson("{\"autoSync\":true}")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testSaveTenantAgentMapping_CatalogFound() {
        TenantAgentMapping input = buildMapping(null, 100);
        AgentCatalog catalog = mockCatalog(100);
        TenantAgentMapping saved = buildMapping(1, 100);
        saved.setAgentCatalog(catalog);

        when(catalogRepo.findById(100)).thenReturn(Optional.of(catalog));
        when(mappingRepo.save(any(TenantAgentMapping.class))).thenReturn(saved);

        TenantAgentMapping result = service.save(input);

        assertNotNull(result);
        assertEquals(100, result.getAgentCatalog().getCatalogId());
        assertEquals("v1.2.3", result.getDeployedVersion());
    }

    @Test
    void testSaveTenantAgentMapping_CatalogNotFound() {
        TenantAgentMapping input = buildMapping(null, 404);

        when(catalogRepo.findById(404)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.save(input));
        assertEquals("Catalog ID not found", exception.getMessage());
    }

    @Test
    void testFindAllTenantAgentMappings() {
        TenantAgentMapping m1 = buildMapping(1, 100);
        TenantAgentMapping m2 = buildMapping(2, 101);

        when(mappingRepo.findAll()).thenReturn(List.of(m1, m2));

        List<TenantAgentMapping> result = service.findAll();

        assertEquals(2, result.size());
        assertEquals("v1.2.3", result.get(0).getDeployedVersion());
        assertEquals(TenantAgentMapping.DeploymentStatus.pending, result.get(1).getDeploymentStatus());
    }
}