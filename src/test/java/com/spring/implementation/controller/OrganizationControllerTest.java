package com.spring.implementation.controller;

import com.spring.implementation.model.Organizations;
import com.spring.implementation.service.OrganizationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationControllerTest {


    @Mock
    private  OrganizationService organizationService;

    @InjectMocks
    private OrganizationController organizationController;

    @Test
    void testGetAllOrganizations() {
        List<Organizations> mockList = List.of(new Organizations(1, "AlphaCorp","service", LocalDateTime.now()),
                new Organizations(2, "BetaInc","service", LocalDateTime.now()));
        when(organizationService.getAllOrganizations()).thenReturn(mockList);

        ResponseEntity<List<Organizations>> response = organizationController.getAllOrganizations();

        assertEquals(2, response.getBody().size());
        assertEquals("AlphaCorp", response.getBody().get(0).getName());
    }

    @Test
    void testGetOrganizationById_Found() {
        Organizations org = new Organizations(2, "AlphaCorp","service", LocalDateTime.now());
        when(organizationService.getOrganizationById(1L)).thenReturn(Optional.of(org));

        ResponseEntity<Organizations> response = organizationController.getOrganizationById(1L);

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals("AlphaCorp", response.getBody().getName());
    }

    @Test
    void testGetOrganizationById_NotFound() {
        when(organizationService.getOrganizationById(99l)).thenReturn(Optional.empty());

        ResponseEntity<Organizations> response = organizationController.getOrganizationById(99L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testCreateOrganization() {
        Organizations org = new Organizations(null, "BetaInc","service", LocalDateTime.now());
        Organizations savedOrg = new Organizations(2, "BetaInc","service", LocalDateTime.now());

        when(organizationService.createOrganization(org)).thenReturn(ResponseEntity.ok(savedOrg));

        ResponseEntity<Organizations> response = organizationController.createOrganization(org);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().getId());
    }

    @Test
    void testUpdateOrganization() {
        Organizations updatedOrg = new Organizations(2, "UpdatedCorp","service", LocalDateTime.now());

        when(organizationService.updateOrganization(eq(2l), any(Organizations.class)))
            .thenReturn(ResponseEntity.ok(updatedOrg));

        ResponseEntity<Organizations> response = organizationController.updateOrganization(2L, updatedOrg);

        assertEquals("UpdatedCorp", response.getBody().getName());
    }

    @Test
    void testDeleteOrganization() {
        when(organizationService.deleteOrganization(1L)).thenReturn(ResponseEntity.ok("Deleted"));

        ResponseEntity<String> response = organizationController.deleteOrganization(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Deleted", response.getBody());
    }
}