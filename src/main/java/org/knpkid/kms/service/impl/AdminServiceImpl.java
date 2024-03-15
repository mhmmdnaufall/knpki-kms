package org.knpkid.kms.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.AdminResponse;
import org.knpkid.kms.model.RegisterAdminRequest;
import org.knpkid.kms.repository.AdminRepository;
import org.knpkid.kms.service.AdminService;
import org.knpkid.kms.service.ImageService;
import org.knpkid.kms.service.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@AllArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    private final ValidationService validationService;

    private final ImageService imageService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("admin not found with username = '" + username + "'"));
    }

    @Override
    public AdminResponse get(Admin admin) {
        return new AdminResponse(admin.getUsername(), admin.getName(), admin.getImage());
    }

    @Override
    public void register(RegisterAdminRequest request) {
        validationService.validate(request);

        if (adminRepository.existsById(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already registered");
        }

        final var admin = new Admin();
        admin.setUsername(request.username());
        admin.setPassword(passwordEncoder.encode(request.password()));
        admin.setName(request.name());

        if (request.image() != null)
            admin.setImage(imageService.save(request.image()));


        adminRepository.save(admin);

        log.info("new admin created with username = '{}'", admin.getUsername());
    }
}
