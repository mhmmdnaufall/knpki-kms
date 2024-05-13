package org.knpkid.kms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Author;

import java.time.LocalDateTime;

public record QuoteResponse(

        Integer id,

        String body,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime updatedAt,

        @JsonIgnoreProperties({"articles", "quotes"})
        Author author,

        @JsonIncludeProperties({"username", "name", "image"})
        Admin admin
) { }
