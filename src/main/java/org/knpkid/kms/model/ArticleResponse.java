package org.knpkid.kms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Value;
import org.knpkid.kms.entity.ArticleImage;
import org.knpkid.kms.entity.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Value
public class ArticleResponse {

        String id;

        String title;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime updatedAt;

        String body;

        String teaser;

        @JsonIgnoreProperties("articles")
        Set<Tag> tags;

        byte[] coverImage;

        @JsonIgnoreProperties("article")
        List<ArticleImage> images;


}
