package org.knpkid.kms.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.AdminResponse;
import org.knpkid.kms.service.AdminService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private AdminService adminService;

    @Test
    void get() {
        final var admin = new Admin();
        admin.setName("Admin Test");
        admin.setUsername("admin");

        when(adminService.get(admin))
                .thenReturn(new AdminResponse(admin.getUsername(), admin.getName(), admin.getImage()));

        final var webResponse = adminController.get(admin);
        assertEquals(admin.getName(), webResponse.data().name());
        assertEquals(admin.getUsername(), webResponse.data().username());
        assertEquals(admin.getImage(), webResponse.data().image());
    }
}