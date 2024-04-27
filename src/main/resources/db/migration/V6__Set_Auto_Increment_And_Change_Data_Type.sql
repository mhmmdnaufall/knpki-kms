ALTER TABLE article_image_gallery
    DROP FOREIGN KEY fk_gallery_articles;

ALTER TABLE articles_authors
    DROP FOREIGN KEY fk_articles_to_articles_authors,
    DROP FOREIGN KEY fk_authors_to_articles_authors;

ALTER TABLE articles_tags
    DROP FOREIGN KEY fk_articles_to_articles_tags;

ALTER TABLE articles
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE quotes
    DROP FOREIGN KEY fk_quotes_authors;

ALTER TABLE authors
    MODIFY id INT NOT NULL AUTO_INCREMENT;

ALTER TABLE article_image_gallery
    MODIFY article_id INT NOT NULL,
    ADD CONSTRAINT fk_gallery_articles FOREIGN KEY (article_id) REFERENCES articles (id);

ALTER TABLE articles_authors
    MODIFY author_id INT NOT NULL,
    ADD CONSTRAINT fk_authors_to_articles_authors FOREIGN KEY (author_id) REFERENCES authors (id),
    MODIFY article_id INT NOT NULL,
    ADD CONSTRAINT fk_articles_to_articles_authors FOREIGN KEY (article_id) REFERENCES articles (id);

ALTER TABLE articles_tags
    MODIFY article_id INT NOT NULL,
    ADD CONSTRAINT fk_articles_to_articles_tags FOREIGN KEY (article_id) REFERENCES articles (id);

ALTER TABLE quotes
    MODIFY id INT NOT NULL AUTO_INCREMENT,
    MODIFY author_id INT NOT NULL,
    ADD CONSTRAINT fk_quotes_authors FOREIGN KEY (author_id) REFERENCES authors (id);