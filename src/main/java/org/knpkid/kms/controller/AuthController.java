package org.knpkid.kms.controller;

import lombok.RequiredArgsConstructor;
import org.knpkid.kms.model.LoginAdminRequest;
import org.knpkid.kms.model.TokenResponse;
import org.knpkid.kms.model.WebResponse;
import org.knpkid.kms.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<TokenResponse> login(@RequestBody LoginAdminRequest request) {
        final var jwtToken = authService.getLoginToken(request);
        return new WebResponse<>(new TokenResponse(jwtToken), null, null);
    }

    @DeleteMapping("/api/auth/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {

        SecurityContextHolder.clearContext();

    }

}
