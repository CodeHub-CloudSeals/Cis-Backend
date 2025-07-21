package com.spring.implementation.service;

import com.spring.implementation.model.Organizations;
import com.spring.implementation.repository.OrganizationRepository;
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
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private OrganizationService service;

    private Organizations buildOrg(Integer id, String name) {
        return Organizations.builder()
                .id(id)
                .name(name)
                .domain("cloudseal.ai")
               // .type("service")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testGetAllOrganizations() {
        Organizations o1 = buildOrg(1, "AlphaCorp");
        Organizations o2 = buildOrg(2, "BetaInc");

        when(organizationRepository.findAll()).thenReturn(List.of(o1, o2));

        List<Organizations> result = service.getAllOrganizations();

        assertEquals(2, result.size());
        assertEquals("AlphaCorp", result.get(0).getName());
    }

    @Test
    void testGetOrganizationById_Found() {
        Organizations org = buildOrg(1, "SecureCorp");

        when(organizationRepository.findById(1L)).thenReturn(Optional.of(org));

        Optional<Organizations> result = service.getOrganizationById(1L);

        assertTrue(result.isPresent());
        assertEquals("SecureCorp", result.get().getName());
    }

    @Test
    void testGetOrganizationById_NotFound() {
        when(organizationRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Organizations> result = service.getOrganizationById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void testCreateOrganization() {
        Organizations org = buildOrg(null, "CloudOps");
        Organizations saved = buildOrg(5, "CloudOps");

        when(organizationRepository.save(org)).thenReturn(saved);

        ResponseEntity<Organizations> response = service.createOrganization(org);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(5, response.getBody().getId());
    }

    @Test
    void testUpdateOrganization_Found() {
        Organizations existing = buildOrg(7, "LegacyCorp");
        Organizations updated = buildOrg(null, "NextGenCorp");

        when(organizationRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(organizationRepository.save(any(Organizations.class))).thenReturn(existing);

        ResponseEntity<Organizations> response = service.updateOrganization(7L, updated);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("NextGenCorp", response.getBody().getName());
    }

    @Test
    void testUpdateOrganization_NotFound() {
        Organizations updated = buildOrg(null, "GhostCorp");

        when(organizationRepository.findById(100L)).thenReturn(Optional.empty());

        ResponseEntity<Organizations> response = service.updateOrganization(100L, updated);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteOrganization_Found() {
        when(organizationRepository.existsById(3L)).thenReturn(true);
        doNothing().when(organizationRepository).deleteById(3L);

        ResponseEntity<String> response = service.deleteOrganization(3L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Organization deleted successfully.", response.getBody());
    }

    @Test
    void testDeleteOrganization_NotFound() {
        when(organizationRepository.existsById(42L)).thenReturn(false);

        ResponseEntity<String> response = service.deleteOrganization(42L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Organization not found.", response.getBody());
    }
}