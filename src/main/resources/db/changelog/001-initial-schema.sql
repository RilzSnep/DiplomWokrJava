--liquibase formatted sql

--changeset student:1
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL,
    image VARCHAR(255),
    password VARCHAR(255) NOT NULL
);

--changeset student:2
CREATE TABLE ads (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price INTEGER NOT NULL,
    description TEXT,
    image VARCHAR(255),
    author_id INTEGER NOT NULL,
    CONSTRAINT fk_ads_author FOREIGN KEY (author_id) REFERENCES users(id)
);

--changeset student:3
CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    created_at TIMESTAMP,
    author_id INTEGER NOT NULL,
    ad_id INTEGER NOT NULL,
    CONSTRAINT fk_comments_author FOREIGN KEY (author_id) REFERENCES users(id),
    CONSTRAINT fk_comments_ad FOREIGN KEY (ad_id) REFERENCES ads(id) ON DELETE CASCADE
);

--changeset student:4
CREATE INDEX idx_ads_author_id ON ads(author_id);
CREATE INDEX idx_comments_ad_id ON comments(ad_id);
CREATE INDEX idx_comments_author_id ON comments(author_id);