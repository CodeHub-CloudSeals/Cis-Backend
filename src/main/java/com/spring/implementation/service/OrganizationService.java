package com.spring.implementation.service;

import com.spring.implementation.model.Organizations;
import com.spring.implementation.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public List<Organizations> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Optional<Organizations> getOrganizationById(Long id) {
        return organizationRepository.findById(id);
    }

    public ResponseEntity<Organizations> createOrganization(Organizations organization) {
        Organizations savedOrg = organizationRepository.save(organization);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrg);
    }

    public ResponseEntity<Organizations> updateOrganization(Long id, Organizations newOrgData) {
        Optional<Organizations> existingOrg = organizationRepository.findById(id);
        if (existingOrg.isPresent()) {
            Organizations org = existingOrg.get();
            org.setName(newOrgData.getName());
            org.setDomain(newOrgData.getDomain());

           /* org.setType(newOrgData.getType());
            org.setEmail(newOrgData.getEmail());
            org.setPhone(newOrgData.getPhone());
            org.setAddress(newOrgData.getAddress());*/

            return ResponseEntity.ok(organizationRepository.save(org));
        }
        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<String> deleteOrganization(Long id) {
        if (organizationRepository.existsById(id)) {
            organizationRepository.deleteById(id);
            return ResponseEntity.ok("Organization deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Organization not found.");
    }
}