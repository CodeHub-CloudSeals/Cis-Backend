package com.spring.implementation.model;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organizations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false)
    private String name;

    @Column(length = 128, nullable = false)
    private String domain;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "company_name")
    private String companyName;


    @Column(name = "company_address")
    private String companyAddress;


    @Column(name = "country")
    private String country;


    @Column(name = "state")
    private String state;


    @Column(name = "zip_code")
    private String zipCode;




    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}