package org.knpkid.kms.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginAdminRequest(
        @NotBlank
        @Size(max = 20)
        String username,

        @NotBlank
        @Size(max = 30, min = 5)
        String password
) {}
