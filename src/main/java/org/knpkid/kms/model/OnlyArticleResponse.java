package org.knpkid.kms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class OnlyArticleResponse implements Serializable {

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

    byte[] coverImage;

}
