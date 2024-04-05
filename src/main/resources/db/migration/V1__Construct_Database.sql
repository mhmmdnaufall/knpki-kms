CREATE TABLE images
(
    id     VARCHAR(100)              NOT NULL,
    format ENUM ('JPG','JPEG','PNG') NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE admin
(
    username VARCHAR(20)  NOT NULL,
    password VARCHAR(100) NOT NULL,
    image    VARCHAR(100),
    name     VARCHAR(100) NOT NULL,
    PRIMARY KEY (username),
    UNIQUE KEY image_unique (image),
    CONSTRAINT fk_admin_images FOREIGN KEY (image) REFERENCES images (id)
) ENGINE = InnoDB;

CREATE TABLE articles
(
    id          VARCHAR(100) NOT NULL,
    title       VARCHAR(100) NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    cover_image VARCHAR(100),
    body        TEXT         NOT NULL,
    teaser      VARCHAR(200),
    username    VARCHAR(20)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE cover_image_unique (cover_image),
    CONSTRAINT fk_articles_admin FOREIGN KEY (username) REFERENCES admin (username),
    CONSTRAINT fk_articles_images FOREIGN KEY (cover_image) REFERENCES images (id)
) ENGINE = InnoDB;

CREATE TABLE article_image_gallery
(
    article_id VARCHAR(100) NOT NULL,
    image_id   VARCHAR(100) NOT NULL,
    PRIMARY KEY (article_id, image_id),
    UNIQUE KEY image_unique (image_id),
    CONSTRAINT fk_gallery_articles FOREIGN KEY (article_id) REFERENCES articles (id),
    CONSTRAINT fk_gallery_images FOREIGN KEY (image_id) REFERENCES images (id)
) ENGINE = InnoDB;

CREATE TABLE tags
(
    id   VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE articles_tags
(
    article_id VARCHAR(100) NOT NULL,
    tag_id     VARCHAR(50)  NOT NULL,
    PRIMARY KEY (article_id, tag_id),
    CONSTRAINT fk_articles_to_articles_tags FOREIGN KEY (article_id) REFERENCES articles (id),
    CONSTRAINT fk_tags_to_articles_tags FOREIGN KEY (tag_id) REFERENCES tags (id)
) ENGINE = InnoDB;
