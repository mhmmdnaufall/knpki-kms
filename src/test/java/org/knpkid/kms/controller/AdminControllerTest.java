package org.knpkid.kms.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.AdminResponse;
import org.knpkid.kms.model.RegisterAdminRequest;
import org.knpkid.kms.model.WebResponse;
import org.knpkid.kms.service.AdminService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

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
        assertEquals(admin.getName(), webResponse.data().getName());
        assertEquals(admin.getUsername(), webResponse.data().getUsername());
        assertEquals(admin.getImage(), webResponse.data().getImage());
        assertNull(webResponse.errors());
        assertNull(webResponse.paging());
    }

    @Test
    void register() {
        final var request = new RegisterAdminRequest(
                "username", "password", "name", mock(MultipartFile.class)
        );

        doNothing().when(adminService).register(request);

        final var webResponse = adminController.register(request);

        verify(adminService).register(request);

        assertEquals("OK", webResponse.data());
        assertNull(webResponse.errors());
        assertNull(webResponse.paging());
    }
}