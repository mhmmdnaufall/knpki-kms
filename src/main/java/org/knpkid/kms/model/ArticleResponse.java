package org.knpkid.kms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.knpkid.kms.entity.Admin;
import org.knpkid.kms.entity.Image;
import org.knpkid.kms.entity.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record ArticleResponse (

        String id,

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

        Image coverImage,

        @JsonIgnoreProperties("article")
        List<Image> images


) { }
