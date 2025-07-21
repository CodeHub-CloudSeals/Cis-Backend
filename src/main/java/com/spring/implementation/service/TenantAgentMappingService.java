package com.spring.implementation.service;

import com.spring.implementation.model.AgentCatalog;
import com.spring.implementation.model.TenantAgentMapping;
import com.spring.implementation.repository.AgentCatalogRepository;
import com.spring.implementation.repository.TenantAgentMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantAgentMappingService {
    private final TenantAgentMappingRepository repository;
    private final AgentCatalogRepository agentCatalogRepository;

    public TenantAgentMapping save(TenantAgentMapping mapping) {
        AgentCatalog catalog = agentCatalogRepository.findById(mapping.getAgentCatalog().getCatalogId())
                .orElseThrow(() -> new RuntimeException("Catalog ID not found"));
        mapping.setAgentCatalog(catalog);
        return repository.save(mapping);
    }

    public List<TenantAgentMapping> findAll() {
        return repository.findAll();
    }
}