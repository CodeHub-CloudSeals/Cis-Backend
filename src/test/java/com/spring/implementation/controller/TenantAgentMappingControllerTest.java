package com.spring.implementation.controller;

import com.spring.implementation.model.AgentCatalog;
import com.spring.implementation.model.Organizations;
import com.spring.implementation.model.TenantAgentMapping;
import com.spring.implementation.service.TenantAgentMappingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TenantAgentMappingControllerTest {

    @Mock
    private TenantAgentMappingService service;

    @InjectMocks
    private TenantAgentMappingController controller;

    private Organizations mockOrg() {
        return Organizations.builder()
                .id(1)
            .name("CloudCorp")
            .createdAt(LocalDateTime.now())
            .build();
    }

    private AgentCatalog mockCatalog() {
        return AgentCatalog.builder()
            .catalogId(100)
            .agentName("GopiMetrics")
            .agentType(AgentCatalog.AgentType.DevOps)
            .version("v1.2.3")
            .status(AgentCatalog.AgentStatus.active)
            .createdAt(LocalDateTime.now())
            .lastUpdatedAt(LocalDateTime.now())
            .configJson("{\"logLevel\":\"INFO\"}")
            .build();
    }

    private TenantAgentMapping buildMapping(Integer id, String version, TenantAgentMapping.DeploymentStatus status) {
        return TenantAgentMapping.builder()
            .mappingId(id)
            .organizations(mockOrg())
            .agentCatalog(mockCatalog())
            .tenantSpecificConfigJson("{\"threshold\":80}")
            .deployedVersion(version)
            .deploymentStatus(status)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    void testCreateTenantAgentMapping() {
        TenantAgentMapping request = buildMapping(null, "v2.0.0", TenantAgentMapping.DeploymentStatus.pending);
        TenantAgentMapping saved = buildMapping(1, "v2.0.0", TenantAgentMapping.DeploymentStatus.pending);

        when(service.save(request)).thenReturn(saved);

        ResponseEntity<TenantAgentMapping> response = controller.create(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getMappingId());
        assertEquals("v2.0.0", response.getBody().getDeployedVersion());
        assertEquals(TenantAgentMapping.DeploymentStatus.pending, response.getBody().getDeploymentStatus());
    }

    @Test
    void testGetAllTenantAgentMappings() {
        TenantAgentMapping map1 = buildMapping(1, "v1.1.1", TenantAgentMapping.DeploymentStatus.deployed);
        TenantAgentMapping map2 = buildMapping(2, "v1.2.0", TenantAgentMapping.DeploymentStatus.failed);

        when(service.findAll()).thenReturn(List.of(map1, map2));

        List<TenantAgentMapping> result = controller.getAll();

        assertEquals(2, result.size());
        assertEquals("v1.1.1", result.get(0).getDeployedVersion());
        assertEquals(TenantAgentMapping.DeploymentStatus.deployed, result.get(0).getDeploymentStatus());
        assertEquals(TenantAgentMapping.DeploymentStatus.failed, result.get(1).getDeploymentStatus());
    }
}