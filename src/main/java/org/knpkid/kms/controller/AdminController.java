package org.knpkid.kms.controller;

import lombok.RequiredArgsConstructor;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.model.AdminResponse;
import org.knpkid.kms.model.RegisterAdminRequest;
import org.knpkid.kms.model.WebResponse;
import org.knpkid.kms.service.AdminService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping(
            path = "/api/admin/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AdminResponse> get(Admin admin) {
        var adminResponse = adminService.get(admin);
        return new WebResponse<>(adminResponse, null, null);
    }

    @PostMapping(
            path = "/api/admin",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> register(@ModelAttribute RegisterAdminRequest request) {
        adminService.register(request);
        return new WebResponse<>("OK", null, null);
    }

}
