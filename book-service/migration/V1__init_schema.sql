CREATE TABLE authors
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(255)          NOT NULL,
    last_name  VARCHAR(255)          NULL,
    bio        TEXT                  NULL,
    CONSTRAINT pk_authors PRIMARY KEY (id)
);

CREATE TABLE books
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    title            VARCHAR(255)          NOT NULL,
    isbn             VARCHAR(255)          NOT NULL,
    edition          VARCHAR(255)          NOT NULL,
    publication_year INT                   NOT NULL,
    language         VARCHAR(255)          NULL,
    summary          VARCHAR(500)          NULL,
    cover_image      VARCHAR(2048)         NULL,
    total_copies     BIGINT                NULL,
    available_copies BIGINT                NOT NULL,
    publisher_id     BIGINT                NULL,
    CONSTRAINT pk_books PRIMARY KEY (id)
);

CREATE TABLE books_authors
(
    author_id BIGINT NOT NULL,
    book_id   BIGINT NOT NULL,
    CONSTRAINT pk_books_authors PRIMARY KEY (author_id, book_id)
);

CREATE TABLE books_categories
(
    book_id     BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT pk_books_categories PRIMARY KEY (book_id, category_id)
);

CREATE TABLE categories
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    name      VARCHAR(255)          NOT NULL,
    parent_id BIGINT                NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE publishers
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    name    VARCHAR(255)          NOT NULL,
    phone   VARCHAR(255)          NOT NULL,
    email   VARCHAR(255)          NULL,
    address VARCHAR(255)          NULL,
    CONSTRAINT pk_publishers PRIMARY KEY (id)
);

ALTER TABLE books
    ADD CONSTRAINT uc_books_isbn UNIQUE (isbn);

ALTER TABLE categories
    ADD CONSTRAINT uc_categories_name UNIQUE (name);

ALTER TABLE publishers
    ADD CONSTRAINT uc_publishers_email UNIQUE (email);

ALTER TABLE publishers
    ADD CONSTRAINT uc_publishers_name UNIQUE (name);

ALTER TABLE publishers
    ADD CONSTRAINT uc_publishers_phone UNIQUE (phone);

ALTER TABLE books
    ADD CONSTRAINT FK_BOOKS_ON_PUBLISHER FOREIGN KEY (publisher_id) REFERENCES publishers (id);

ALTER TABLE categories
    ADD CONSTRAINT FK_CATEGORIES_ON_PARENT FOREIGN KEY (parent_id) REFERENCES categories (id);

ALTER TABLE books_authors
    ADD CONSTRAINT fk_booaut_on_author FOREIGN KEY (author_id) REFERENCES authors (id);

ALTER TABLE books_authors
    ADD CONSTRAINT fk_booaut_on_book FOREIGN KEY (book_id) REFERENCES books (id);

ALTER TABLE books_categories
    ADD CONSTRAINT fk_boocat_on_book FOREIGN KEY (book_id) REFERENCES books (id);

ALTER TABLE books_categories
    ADD CONSTRAINT fk_boocat_on_category FOREIGN KEY (category_id) REFERENCES categories (id);