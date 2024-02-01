package org.knpkid.kms.controller;

import lombok.RequiredArgsConstructor;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.AdminResponse;
import org.knpkid.kms.model.WebResponse;
import org.knpkid.kms.service.AdminService;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping(
            path = "/api/admin/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AdminResponse> get() {
        final var admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final var adminResponse = adminService.get(admin);
        return new WebResponse<>(adminResponse, null);
    }

}
