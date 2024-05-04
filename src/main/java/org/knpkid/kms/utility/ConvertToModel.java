package org.knpkid.kms.utility;

import org.knpkid.kms.entity.Article;
import org.knpkid.kms.entity.Author;
import org.knpkid.kms.entity.Quote;
import org.knpkid.kms.model.ArticleResponse;
import org.knpkid.kms.model.AuthorResponse;
import org.knpkid.kms.model.OnlyArticleResponse;
import org.knpkid.kms.model.QuoteResponse;

public class ConvertToModel {

    private ConvertToModel() {}

    public static ArticleResponse articleResponse(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getBody(),
                article.getTeaser(),
                article.getTags(),
                article.getAdmin(),
                article.getAuthors(),
                article.getCoverImage(),
                article.getImageGallery(),
                article.getArchive()
        );
    }

    public static OnlyArticleResponse onlyArticleResponse(Article article) {
        return new OnlyArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getBody(),
                article.getTeaser(),
                article.getCoverImage()
        );
    }

    public static QuoteResponse quoteResponse(Quote quote) {
        return new QuoteResponse(
                quote.getId(),
                quote.getBody(),
                quote.getAuthor(),
                quote.getAdmin()
        );
    }

    public static AuthorResponse authorResponse(Author author) {
        return new AuthorResponse(
                author.getId(),
                author.getName(),
                author.getArticles().stream().map(ConvertToModel::articleResponse).toList(),
                author.getQuotes()
        );
    }

}
