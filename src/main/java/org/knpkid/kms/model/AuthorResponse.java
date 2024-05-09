package org.knpkid.kms.model;

import java.util.List;

public record AuthorResponse(

        int id,

        String name,

        List<ArticleResponse> articles,

        List<QuoteResponse> quotes

) { }
