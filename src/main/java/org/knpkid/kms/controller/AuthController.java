package org.knpkid.kms.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.knpkid.kms.model.LoginAdminRequest;
import org.knpkid.kms.model.WebResponse;
import org.knpkid.kms.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(
            path = "/api/auth/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> login(@RequestBody LoginAdminRequest request, HttpServletResponse response) {
        final var jwtToken = authService.getLoginToken(request);
        final var cookie = new Cookie("token", jwtToken);
        response.addCookie(cookie);

        return new WebResponse<>("OK", null);
    }

    @DeleteMapping(
            path = "/api/auth/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public WebResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        final var cookie = WebUtils.getCookie(request, "token");

        if (cookie != null) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        return new WebResponse<>("OK", null);
    }


}
