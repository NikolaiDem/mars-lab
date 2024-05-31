create table users
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(100) UNIQUE,
    password VARCHAR(100),
    role     VARCHAR(100)
);

create table reports
(
    id           SERIAL PRIMARY KEY,
    title        VARCHAR(100),
    state        VARCHAR(100),
    author_id    INTEGER,
    last_updated TIMESTAMP,
    file_name    VARCHAR(100),
    file_uuid    VARCHAR(100),
    comment      VARCHAR(100),
    CONSTRAINT fk_author_id
        FOREIGN KEY (author_id) REFERENCES users (id)
);

create table delivery_periods
(
    from_date  TIMESTAMP,
    to_date    TIMESTAMP,
    executed   BOOLEAN,
    PRIMARY KEY (from_date, to_date)
);

