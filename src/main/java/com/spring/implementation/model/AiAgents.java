package com.spring.implementation.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_agents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiAgents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organizations organizations;

    @Column(length = 128, nullable = false)
    private String name;

    @Column(name = "agent_type", length = 64, nullable = false)
    private String agentType;

    @Column(length = 20, nullable = false)
    private String status = "active";

    @Column(name = "config_json", columnDefinition = "json")
    private String configJson;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Users createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}