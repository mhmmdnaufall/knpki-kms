package org.knpkid.kms.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.RegisterAdminRequest;
import org.knpkid.kms.repository.AdminRepository;
import org.knpkid.kms.service.ValidationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private ValidationService validationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void loadByUsername() {
        // admin exist
        when(adminRepository.findById("username")).thenReturn(Optional.of(new Admin()));
        final var admin = adminService.loadUserByUsername("username");
        assertNotNull(admin);

        // admin not exist
        when(adminRepository.findById("username")).thenReturn(Optional.empty());
         final var exception = assertThrows(
                 UsernameNotFoundException.class,
                 () -> adminService.loadUserByUsername("username")
         );
         assertEquals("admin not found with username = 'username'", exception.getMessage());
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


    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void register(boolean isAccountExist) throws IOException {

        final var mockImage = mock(MultipartFile.class);

        final var request = new RegisterAdminRequest(
                "username", "password", "name", mockImage
        );

        {
            when(adminRepository.existsById(request.username())).thenReturn(isAccountExist);
        }

        if (isAccountExist) {
            final var exception = assertThrows(ResponseStatusException.class, () -> adminService.register(request));
            assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
            assertEquals("username already registered", exception.getReason());
            return;
        }

        {
            when(mockImage.getBytes()).thenReturn("image".getBytes());
            when(adminRepository.save(any())).thenReturn(new Admin());
        }

        assertDoesNotThrow(() -> adminService.register(request));
        verify(adminRepository).save(any());

    }

    @Test
    void register_getBytesError() throws IOException {

        final var mockImage = mock(MultipartFile.class);

        final var request = new RegisterAdminRequest(
                "username", "password", "name", mockImage
        );

        {
            when(adminRepository.existsById(request.username())).thenReturn(false);
            when(mockImage.getBytes()).thenThrow(new IOException());
        }

        final var exception = assertThrows(ErrorResponseException.class, () -> adminService.register(request));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(adminRepository, times(0)).save(any());

    }
}