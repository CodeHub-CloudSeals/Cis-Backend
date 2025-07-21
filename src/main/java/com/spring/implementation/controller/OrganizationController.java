package com.spring.implementation.controller;

import com.spring.implementation.model.Organizations;
import com.spring.implementation.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("cloudseal/v1/api/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<List<Organizations>> getAllOrganizations() {
        log.info(" getAllOrganizations");

        List<Organizations> organizations = organizationService.getAllOrganizations();
        log.info("getAllOrganizations response: {}", organizations);
        return ResponseEntity.ok(organizations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organizations> getOrganizationById(@PathVariable Long id) {
        log.info("getOrganizationById : {}", id);
        return organizationService.getOrganizationById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Organizations> createOrganization(@RequestBody Organizations organization) {

        log.info("createOrganization : {}", organization);

        return organizationService.createOrganization(organization);
    }


    @PostMapping("/{userId}")
    public ResponseEntity<Organizations> createOrganization(@PathVariable Long userId,@RequestBody Organizations organization) {

        log.info("createOrganization : {}", organization);

        return organizationService.createOrganization(organization);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organizations> updateOrganization(@PathVariable Long id, @RequestBody Organizations organization) {
        log.info("updateOrganization : {}", organization);
        return organizationService.updateOrganization(id, organization);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrganization(@PathVariable Long id) {
        log.info("deleteOrganization : {}", id);
        return organizationService.deleteOrganization(id);
    }
}