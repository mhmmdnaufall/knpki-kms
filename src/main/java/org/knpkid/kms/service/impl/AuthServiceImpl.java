package org.knpkid.kms.service.impl;

import lombok.RequiredArgsConstructor;
import org.knpkid.kms.model.LoginAdminRequest;
import org.knpkid.kms.security.JwtTokenUtils;
import org.knpkid.kms.service.AdminService;
import org.knpkid.kms.service.AuthService;
import org.knpkid.kms.service.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AdminService adminService;

    private final ValidationService validationService;

    private final JwtTokenUtils jwtTokenUtils;

    private final AuthenticationManager authenticationManager;

    @Override
    public String getLoginToken(LoginAdminRequest request) {
        validationService.validate(request);

        authenticate(request.username(), request.password());

        var admin = adminService.loadUserByUsername(request.username());
        return jwtTokenUtils.generateToken(admin);
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password wrong");
        }
    }


}
