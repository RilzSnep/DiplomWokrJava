--liquibase formatted sql

--changeset student:5
CREATE TABLE images (
    id VARCHAR(255) PRIMARY KEY,
    image BYTEA NOT NULL,
    media_type VARCHAR(50)
);