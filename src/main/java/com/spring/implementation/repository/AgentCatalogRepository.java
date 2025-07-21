package com.spring.implementation.repository;

import com.spring.implementation.model.AgentCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentCatalogRepository extends JpaRepository<AgentCatalog, Integer> {}
