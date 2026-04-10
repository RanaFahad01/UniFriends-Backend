CREATE TABLE profiles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    type VARCHAR(20) NOT NULL,
    bio VARCHAR(300),
    tags TEXT,
    hobbies TEXT,
    UNIQUE (user_id, type)
);

CREATE INDEX idx_profiles_user_id ON profiles (user_id);
