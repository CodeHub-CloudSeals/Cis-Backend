package com.spring.implementation.service;

import com.spring.implementation.model.AgentCatalog;
import com.spring.implementation.repository.AgentCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgentCatalogService {
    private final AgentCatalogRepository repository;

    public AgentCatalog save(AgentCatalog catalog) {
        return repository.save(catalog);
    }

    public List<AgentCatalog> findAll() {
        return repository.findAll();
    }
}