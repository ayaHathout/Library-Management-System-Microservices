CREATE TABLE authors
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(255)          NOT NULL,
    last_name  VARCHAR(255)          NULL,
    bio        LONGTEXT              NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE books
(
    id               BIGINT AUTO_INCREMENT   NOT NULL,
    title            VARCHAR(255)            NOT NULL,
    isbn             VARCHAR(255)            NULL,
    edition          VARCHAR(255)            NULL,
    publication_year INT                     NULL,
    language         ENUM   DEFAULT 'ARABIC' NULL,
    summary          VARCHAR(500)            NULL,
    cover_image      VARCHAR(2048)           NULL,
    total_copies     BIGINT DEFAULT 1        NULL,
    available_copies BIGINT                  NOT NULL,
    publisher_id     BIGINT                  NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE books_authors
(
    book_id   BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (book_id, author_id)
);

CREATE TABLE books_categories
(
    book_id     BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (book_id, category_id)
);

CREATE TABLE categories
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    name      VARCHAR(255)          NOT NULL,
    parent_id BIGINT                NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE publishers
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    name    VARCHAR(255)          NOT NULL,
    phone   VARCHAR(255)          NOT NULL,
    email   VARCHAR(255)          NULL,
    address VARCHAR(255)          NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

ALTER TABLE publishers
    ADD CONSTRAINT email UNIQUE (email);

ALTER TABLE books
    ADD CONSTRAINT isbn UNIQUE (isbn);

ALTER TABLE categories
    ADD CONSTRAINT name UNIQUE (name);

ALTER TABLE publishers
    ADD CONSTRAINT name UNIQUE (name);

ALTER TABLE publishers
    ADD CONSTRAINT phone UNIQUE (phone);

ALTER TABLE books_authors
    ADD CONSTRAINT books_authors_ibfk_1 FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE;

ALTER TABLE books_authors
    ADD CONSTRAINT books_authors_ibfk_2 FOREIGN KEY (author_id) REFERENCES authors (id) ON DELETE CASCADE;

CREATE INDEX author_id ON books_authors (author_id);

ALTER TABLE books_categories
    ADD CONSTRAINT books_categories_ibfk_1 FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE;

ALTER TABLE books_categories
    ADD CONSTRAINT books_categories_ibfk_2 FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE;

CREATE INDEX category_id ON books_categories (category_id);

ALTER TABLE books
    ADD CONSTRAINT books_ibfk_1 FOREIGN KEY (publisher_id) REFERENCES publishers (id) ON DELETE SET NULL;

CREATE INDEX publisher_id ON books (publisher_id);

ALTER TABLE categories
    ADD CONSTRAINT categories_ibfk_1 FOREIGN KEY (parent_id) REFERENCES categories (id) ON DELETE SET NULL;

CREATE INDEX parent_id ON categories (parent_id);