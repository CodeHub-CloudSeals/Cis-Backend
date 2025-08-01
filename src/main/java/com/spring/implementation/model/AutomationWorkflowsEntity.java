package com.spring.implementation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "automation_workflows")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutomationWorkflowsEntity {

    @Id
    @Column(name = "workflow_id")
    private String workflow_id;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "workflow_name")
    private String workflowName;

    @Column(columnDefinition = "json", nullable = false)
    private String workflow_definition;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


}