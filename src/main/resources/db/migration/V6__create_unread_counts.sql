CREATE TABLE unread_counts (
    user_id BIGINT NOT NULL REFERENCES users(id),
    league_id BIGINT NOT NULL REFERENCES leagues(id),
    count INT NOT NULL DEFAULT 0,
    PRIMARY KEY (user_id, league_id)
);

CREATE INDEX idx_unread_counts_user_id ON unread_counts (user_id);
