package com.spring.implementation.repository;


import com.spring.implementation.model.AiAgents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIAgentRepository extends JpaRepository<AiAgents, Integer> {
}