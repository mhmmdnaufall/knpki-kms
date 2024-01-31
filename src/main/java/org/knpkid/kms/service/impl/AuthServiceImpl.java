package org.knpkid.kms.service.impl;

import lombok.RequiredArgsConstructor;
import org.knpkid.kms.model.LoginAdminRequest;
import org.knpkid.kms.repository.AdminRepository;
import org.knpkid.kms.security.JwtTokenUtils;
import org.knpkid.kms.service.AuthService;
import org.knpkid.kms.service.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AdminRepository adminRepository;

    private final ValidationService validationService;

    private final JwtTokenUtils jwtTokenUtils;

    private final PasswordEncoder passwordEncoder;

    @Override
    public String getLoginToken(LoginAdminRequest request) {
        validationService.validate(request);

        final var admin = adminRepository.findById(request.username())
                .orElseThrow(this::throwRequestLoginFailed);

        if (!passwordEncoder.matches(request.password(), admin.getPassword())) {
            throw throwRequestLoginFailed();
        }

        return jwtTokenUtils.generateToken(admin);

    }

    private ResponseStatusException throwRequestLoginFailed() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password wrong");
    }


}
