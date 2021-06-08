CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(20) NOT NULL
);

CREATE TABLE posts (
    post_id SERIAL PRIMARY KEY,
    post_user int4 REFERENCES users(id) ON DELETE CASCADE,
    text VARCHAR(2000) NOT NULL
);