INSERT INTO roles (name)
VALUES ('ADMIN'),
       ('USER')
    ON CONFLICT (name) DO NOTHING;