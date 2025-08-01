package com.spring.implementation.repository;


import com.spring.implementation.model.TenantCostSavings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantCostSavingRepository extends JpaRepository<TenantCostSavings, String> {
}