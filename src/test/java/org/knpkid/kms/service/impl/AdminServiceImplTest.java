package org.knpkid.kms.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.repository.AdminRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminService;

    @Mock
    private AdminRepository adminRepository;

    @Test
    void loadByUsername() {
        // admin exist
        when(adminRepository.findById("username")).thenReturn(Optional.of(new Admin()));
        final var admin = adminService.loadUserByUsername("username");
        assertNotNull(admin);

        // admin not exist
        when(adminRepository.findById("username")).thenReturn(Optional.empty());
         final var exception = assertThrows(
                 ResponseStatusException.class,
                 () -> adminService.loadUserByUsername("username")
         );
         assertEquals("Unauthorized", exception.getReason());
         assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    void get() {
        final var admin = new Admin();
        admin.setName("Admin Test");
        admin.setUsername("admin");

        final var adminResponse = adminService.get(admin);
        assertEquals(admin.getName(), adminResponse.getName());
        assertEquals(admin.getUsername(), adminResponse.getUsername());
        assertEquals(admin.getImage(), adminResponse.getImage());
    }
}