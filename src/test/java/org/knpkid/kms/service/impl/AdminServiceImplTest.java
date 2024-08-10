package org.knpkid.kms.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.RegisterAdminRequest;
import org.knpkid.kms.repository.AdminRepository;
import org.knpkid.kms.service.ImageService;
import org.knpkid.kms.service.ValidationService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
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

    @Mock
    private ValidationService validationService;

    @Mock
    private ImageService imageService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void loadByUsername() {
        // admin exist
        when(adminRepository.findById("username")).thenReturn(Optional.of(new Admin()));
        var admin = adminService.loadUserByUsername("username");
        assertNotNull(admin);

        // admin not exist
        when(adminRepository.findById("username")).thenReturn(Optional.empty());
         var exception = assertThrows(
                 UsernameNotFoundException.class,
                 () -> adminService.loadUserByUsername("username")
         );
         assertEquals("admin not found with username = 'username'", exception.getMessage());
    }

    @Test
    void get() {
        var admin = new Admin();
        admin.setName("Admin Test");
        admin.setUsername("admin");

        var adminResponse = adminService.get(admin);
        assertEquals(admin.getName(), adminResponse.name());
        assertEquals(admin.getUsername(), adminResponse.username());
        assertEquals(admin.getImage(), adminResponse.image());
    }


    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void register(boolean isAccountExist) {

        var mockImage = mock(MultipartFile.class);

        var request = new RegisterAdminRequest(
                "username", "password", "name", mockImage
        );

        {
            when(adminRepository.existsById(request.username())).thenReturn(isAccountExist);
            doNothing().when(validationService).validate(request);
        }

        if (isAccountExist) {
            var exception = assertThrows(ResponseStatusException.class, () -> adminService.register(request));
            assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
            assertEquals("username already registered", exception.getReason());
            return;
        }

        {
            when(adminRepository.save(any())).thenReturn(new Admin());
        }

        assertDoesNotThrow(() -> adminService.register(request));
        verify(validationService).validate(request);
        verify(adminRepository).save(any());
        verify(passwordEncoder).encode(any());
        verify(imageService).save(any());

        // null image
        reset(adminRepository, imageService, passwordEncoder);
        var nullImageRequest = new RegisterAdminRequest(
                "username", "password", "name", null
        );

        assertDoesNotThrow(() -> adminService.register(nullImageRequest));
        verify(adminRepository).save(any());
        verify(imageService, times(0)).save(any());
        verify(passwordEncoder).encode(any());

    }
}