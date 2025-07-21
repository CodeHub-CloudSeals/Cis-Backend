package com.spring.implementation.service;


import com.spring.implementation.model.AiAgents;
import com.spring.implementation.repository.AIAgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AIAgentService {

    private final AIAgentRepository repository;


    public AiAgents createAgent(AiAgents agent) {
        return repository.save(agent);
    }

    public List<AiAgents> getAllAgents() {
        return repository.findAll();
    }

    public Optional<AiAgents> getAgentById(Integer id) {
        return repository.findById(id);
    }

    public void deleteAgent(Integer id) {
        repository.deleteById(id);
    }

    public AiAgents updateAgent(AiAgents aiAgent) {
        AiAgents existing = repository.findById(aiAgent.getId())
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        existing.setName(aiAgent.getName());
        existing.setAgentType(aiAgent.getAgentType());
        existing.setStatus(aiAgent.getStatus());
        existing.setConfigJson(aiAgent.getConfigJson());
        return repository.save(existing);
    }
}