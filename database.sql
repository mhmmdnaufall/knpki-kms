CREATE DATABASE knpki_kms;

USE knpki_kms;

CREATE TABLE articles
(
    id          VARCHAR(100) NOT NULL,
    title       VARCHAR(100) NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    cover_image BLOB,
    body        TEXT         NOT NULL,
    teaser      VARCHAR(200),
    PRIMARY KEY (id)
) ENGINE = InnoDb;

DESC articles;

CREATE TABLE admin
(
    id       VARCHAR(100) NOT NULL,
    username VARCHAR(20)  NOT NULL,
    password VARCHAR(30)  NOT NULL,
    image    BLOB,
    name     VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

DESC admin;

ALTER TABLE articles
    ADD COLUMN admin_id VARCHAR(100) NOT NULL,
    ADD CONSTRAINT fk_articles_admin
        FOREIGN KEY (admin_id) REFERENCES admin (id);

DESC articles;

CREATE TABLE tags
(
    id   VARCHAR(100) NOT NULL,
    name VARCHAR(50)  NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

DESC tags;

CREATE TABLE articles_tags
(
    article_id VARCHAR(100) NOT NULL,
    tag_id     VARCHAR(100) NOT NULL,
    PRIMARY KEY (article_id, tag_id),
    CONSTRAINT fk_articles_to_articles_tags
        FOREIGN KEY (article_id) REFERENCES articles (id),
    CONSTRAINT fk_tags_to_articles_tags
        FOREIGN KEY (tag_id) REFERENCES tags (id)
) ENGINE = InnoDB;

DESC articles_tags;

CREATE TABLE article_images
(
    id         VARCHAR(100) NOT NULL,
    image      BLOB         NOT NULL,
    article_id VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_article_images
        FOREIGN KEY (article_id) REFERENCES articles (id)
) ENGINE = InnoDB;

DESC article_images;

ALTER TABLE articles_tags
    DROP CONSTRAINT fk_tags_to_articles_tags;

ALTER TABLE tags
    MODIFY COLUMN id VARCHAR(50) NOT NULL;

ALTER TABLE articles_tags
    MODIFY tag_id VARCHAR(50) NOT NULL,
    ADD CONSTRAINT fk_tags_to_articles_tags FOREIGN KEY (tag_id) REFERENCES tags (id);

DESC tags;

DESC articles_tags;

ALTER TABLE articles
    DROP CONSTRAINT fk_articles_admin;

DESC articles;

ALTER TABLE articles
    DROP COLUMN admin_id,
    ADD COLUMN username VARCHAR(20) NOT NULL;


ALTER TABLE admin
    DROP PRIMARY KEY,
    DROP COLUMN id,
    ADD PRIMARY KEY (username);

ALTER TABLE articles
    ADD CONSTRAINT fk_articles_admin FOREIGN KEY (username) REFERENCES admin (username);

DESC admin;

DESC articles;

ALTER TABLE admin
    MODIFY COLUMN password VARCHAR(100) NOT NULL;

ALTER TABLE admin
    MODIFY COLUMN image MEDIUMBLOB;

DESC admin;