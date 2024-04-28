package org.knpkid.kms.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateQuoteRequest(

        @NotBlank
        @Size(max = 200)
        String body,

        @NotBlank
        @Size(max = 100)
        String author

) { }
