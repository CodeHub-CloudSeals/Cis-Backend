package com.spring.implementation.repository;


import com.spring.implementation.model.AutomationWorkflowsEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkFlowRepository extends JpaRepository<AutomationWorkflowsEntity, String> {
}