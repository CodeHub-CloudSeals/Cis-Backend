package com.spring.implementation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Tenant_Cost_Savings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantCostSavings {

    @Id
    @Column(name = "tcs_id")
    private String tcsId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column(name = "month")
    private LocalDate month;

    @Column(name = "savings_amount")
    private Double savingsAmount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


}