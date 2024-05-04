package org.knpkid.kms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.knpkid.kms.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record ArticleResponse (

        Integer id,

        String title,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime updatedAt,

        String body,

        String teaser,

        @JsonIgnoreProperties("articles")
        Set<Tag> tags,

        @JsonIncludeProperties({"username", "name", "image"})
        Admin admin,

        @JsonIncludeProperties({"id", "name"})
        Set<Author> authors,

        Image coverImage,

        @JsonIgnoreProperties("article")
        List<Image> images,

        Archive archive

) { }
