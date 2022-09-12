DROP TABLE IF EXISTS comments, bookings, items, item_requests, users;

CREATE TABLE IF NOT EXISTS users (
    id    BIGINT       GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS item_requests (
    id           BIGINT        GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    description  VARCHAR(2000) NOT NULL,
    requester_id BIGINT        NOT NULL REFERENCES users ON DELETE CASCADE,
    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS items (
    id           BIGINT        GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name         VARCHAR(255)  NOT NULL,
    description  VARCHAR(2000) NOT NULL,
    is_available BOOLEAN,
    owner_id     BIGINT        NOT NULL REFERENCES users ON DELETE CASCADE,
    request_id   BIGINT        REFERENCES item_requests,
    CONSTRAINT pk_item PRIMARY KEY (id),
    CONSTRAINT UQ_OWNER_ITEM_NAME UNIQUE(name, owner_id)
);

CREATE TABLE IF NOT EXISTS bookings (
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id    BIGINT                      NOT NULL REFERENCES items ON DELETE CASCADE,
    booker_id  BIGINT                      NOT NULL REFERENCES users ON DELETE CASCADE,
    status     VARCHAR(50)                 NOT NULL
);

CREATE TABLE IF NOT EXISTS comments (
    id        BIGINT        GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    text      VARCHAR(2000) NOT NULL,
    item_id   BIGINT        NOT NULL REFERENCES items ON DELETE CASCADE,
    author_id BIGINT        NOT NULL REFERENCES users ON DELETE CASCADE,
    created   TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
