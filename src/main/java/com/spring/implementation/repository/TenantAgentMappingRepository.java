package com.spring.implementation.repository;

import com.spring.implementation.model.TenantAgentMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantAgentMappingRepository extends JpaRepository<TenantAgentMapping, Integer> {}
