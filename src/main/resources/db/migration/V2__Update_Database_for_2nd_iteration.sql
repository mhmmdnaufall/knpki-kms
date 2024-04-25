# Create authors table
CREATE TABLE authors
(
    id   BIGINT       NOT NULL,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

# For author id purposes
CREATE TABLE authors_seq
(
    next_val BIGINT
) ENGINE = InnoDB;

INSERT INTO authors_seq (next_val)
VALUES (1);

# Create quotes table
CREATE TABLE quotes
(
    id        BIGINT       NOT NULL,
    quote     VARCHAR(200) NOT NULL,
    author_id BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_quotes_authors FOREIGN KEY (author_id) REFERENCES authors (id)
) ENGINE = InnoDB;

# For quotes id purposes
CREATE TABLE quotes_seq
(
    next_val BIGINT
) ENGINE = InnoDB;

INSERT INTO quotes_seq (next_val)
VALUES (1);

# Create archives table, for persist some journal things
CREATE TABLE archives
(
    id     VARCHAR(100) NOT NULL,
    format ENUM ('PDF') NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

# Modifying articles id, FROM: VARCHAR(100) TO: BIGINT. Using sequence
ALTER TABLE articles_tags
    DROP FOREIGN KEY fk_articles_to_articles_tags,
    MODIFY article_id BIGINT NOT NULL;


ALTER TABLE article_image_gallery
    DROP FOREIGN KEY fk_gallery_articles,
    MODIFY article_id BIGINT NOT NULL;

ALTER TABLE articles
    DROP PRIMARY KEY,
    MODIFY id BIGINT NOT NULL,
    ADD PRIMARY KEY (id);

ALTER TABLE articles_tags
    ADD CONSTRAINT fk_articles_to_articles_tags FOREIGN KEY (article_id) REFERENCES articles (id);

ALTER TABLE article_image_gallery
    ADD CONSTRAINT fk_gallery_articles FOREIGN KEY (article_id) REFERENCES articles (id);

# For articles id purposes
CREATE TABLE articles_seq
(
    next_val BIGINT
) ENGINE = InnoDB;

INSERT INTO articles_seq (next_val)
VALUES (1);

ALTER TABLE articles
    ADD archive_id VARCHAR(100),
    ADD CONSTRAINT fk_articles_archives FOREIGN KEY (archive_id) REFERENCES archives (id);


# Create many-to-many relations table for articles and authors
CREATE TABLE articles_authors
(
    article_id BIGINT NOT NULL,
    author_id  BIGINT NOT NULL,
    PRIMARY KEY (article_id, author_id),
    CONSTRAINT fk_articles_to_articles_authors FOREIGN KEY (article_id) REFERENCES articles (id),
    CONSTRAINT fk_authors_to_articles_authors FOREIGN KEY (author_id) REFERENCES authors (id)
) ENGINE = InnoDB;




