DROP TABLE IF EXISTS urls CASCADE;

CREATE TABLE urls (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL
);

DROP TABLE IF EXISTS url_checks;

CREATE TABLE url_checks (
    id SERIAL PRIMARY KEY,
    status_code INTEGER,
    title VARCHAR(255),
    h1 VARCHAR(255),
    description TEXT,
    url_id BIGINT REFERENCES urls (id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL
);
