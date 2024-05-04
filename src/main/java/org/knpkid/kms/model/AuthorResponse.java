package org.knpkid.kms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.knpkid.kms.entity.Article;
import org.knpkid.kms.entity.Quote;

import java.util.List;

public record AuthorResponse(

        int id,

        String name,

        List<ArticleResponse> articles,

        @JsonIgnoreProperties({"author", "admin"})
        List<Quote> quotes

) { }
