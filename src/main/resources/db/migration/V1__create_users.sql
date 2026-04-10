CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(20) UNIQUE,
    avatar_url VARCHAR(1024),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    is_new_user BOOLEAN NOT NULL DEFAULT TRUE,
    banned_at TIMESTAMP,
    ban_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    last_login_at TIMESTAMP
);

CREATE INDEX idx_users_banned_at ON users (banned_at);
