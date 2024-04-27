ALTER TABLE quotes
    ADD username VARCHAR(20) NOT NULL,
    ADD CONSTRAINT fk_quotes_admin FOREIGN KEY (username) REFERENCES admin (username);
