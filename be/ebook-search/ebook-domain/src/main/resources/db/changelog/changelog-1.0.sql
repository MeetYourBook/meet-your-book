-- liquibase formatted sql

-- changeset jayden:20240903_create_init_schema
-- comment: 초기 스키마 생성 - book, library, book_library 테이블

CREATE TABLE book
(
    id           BINARY(16) PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    author       VARCHAR(255) NOT NULL,
    publisher    VARCHAR(255),
    publish_date DATE,
    image_url    VARCHAR(255),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT unique_book UNIQUE (title, author, publisher, publish_date)
);

CREATE TABLE library
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    name             VARCHAR(255) NOT NULL,
    type             VARCHAR(255),
    url              VARCHAR(255) NOT NULL,
    ebook_platform   VARCHAR(255) NOT NULL,
    total_book_count INT       DEFAULT 0,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE book_library
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    book_id    BINARY(16) NOT NULL,
    library_id BIGINT     NOT NULL,
    url        VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES book (id),
    FOREIGN KEY (library_id) REFERENCES library (id),
    CONSTRAINT unique_book_library UNIQUE (book_id, library_id)
);

-- changeset jayden:20240903_add_unique_key_library
-- comment: library 테이블의 url에 unique key 추가

ALTER TABLE library
    ADD CONSTRAINT unique_library_url UNIQUE (url);
