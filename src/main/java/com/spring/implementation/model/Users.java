package com.spring.implementation.model;
import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;



import java.time.LocalDateTime;


@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Builder
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Organizations organizations;

    @Column(length = 128, nullable = false)
    private String username;

    @Column(length = 128, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String role;

    @Column(name = "password", length = 256)
    private String password;

    @Column(length = 20)
    private String status ;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "active";
    }


}