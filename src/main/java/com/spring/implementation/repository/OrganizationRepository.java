package com.spring.implementation.repository;

import com.spring.implementation.model.Organizations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organizations, Long> {

}