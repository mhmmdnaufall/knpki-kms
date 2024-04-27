package org.knpkid.kms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Author;

public record QuoteResponse(

        Integer id,

        String body,

        @JsonIgnoreProperties({"articles", "quotes"})
        Author author,

        @JsonIncludeProperties({"username", "name", "image"})
        Admin admin
) { }
