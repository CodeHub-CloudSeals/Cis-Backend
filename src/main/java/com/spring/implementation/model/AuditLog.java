package com.spring.implementation.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /*@ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organizations organizations;*/
    @Column(name = "organization_id", nullable = false)
    private Integer organizationId;

    /*@ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private AiAgents aiAgents;*/

    @Column(name = "agent_id", nullable = false)
    private Integer agentId;

   /* @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;*/
   @Column(name = "user_id", nullable = false)
   private Integer userId;


    @Column(name = "event_type", nullable = false, length = 64)
    private String eventType;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @Column(name = "details_json", columnDefinition = "json")
    private String detailsJson;

    @PrePersist
    protected void onCreate() {
        this.eventTime = LocalDateTime.now();

    }

}