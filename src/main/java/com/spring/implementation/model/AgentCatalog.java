package com.spring.implementation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "agent_catalog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer catalogId;

    @Column(nullable = false, length = 100)
    private String agentName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AgentType agentType;

    @Column(nullable = false, length = 50)
    private String version;

    @Column(columnDefinition = "json", nullable = false)
    private String configJson;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('active','inactive')", nullable = false)
    private AgentStatus status;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum AgentType { DevOps, FinOps, SecOps }
    public enum AgentStatus { active, inactive }
}