package com.spring.implementation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tenant_agent_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantAgentMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mappingId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organizations organizations;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id", nullable = false)
    private AgentCatalog agentCatalog;


    @Column(name = "tenant_specific_config_json",columnDefinition = "json")
    private String tenantSpecificConfigJson;

    @Column(length = 50)
    private String deployedVersion;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('pending','deployed','failed')", nullable = false)
    private DeploymentStatus deploymentStatus = DeploymentStatus.pending;


    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum DeploymentStatus { pending, deployed, failed }
}