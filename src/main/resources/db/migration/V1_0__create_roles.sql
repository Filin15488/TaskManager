CREATE TABLE IF NOT EXISTS roles (
                                     id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                     name VARCHAR(255) NOT NULL UNIQUE
    );

INSERT INTO roles (name)
VALUES ('ADMIN'),
       ('USER')
    ON CONFLICT (name) DO NOTHING;
