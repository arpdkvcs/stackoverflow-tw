CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_sessions
(
    user_id BIGINT NOT NULL,
    token   VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS questions
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT                              NOT NULL,
    title      VARCHAR(100)                        NOT NULL,
    content    TEXT                                NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) references users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS answers
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT                              NOT NULL,
    question_id BIGINT                              NOT NULL,
    content     TEXT                                NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) references users (id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) references questions (id) ON DELETE CASCADE
);
