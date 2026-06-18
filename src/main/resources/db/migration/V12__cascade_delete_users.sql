-- Re-add all FKs referencing users(id) with ON DELETE CASCADE
-- (or SET NULL for nullable reviewer column in reports)

ALTER TABLE profiles
    DROP CONSTRAINT profiles_user_id_fkey,
    ADD CONSTRAINT profiles_user_id_fkey
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE league_members
    DROP CONSTRAINT league_members_user_id_fkey,
    ADD CONSTRAINT league_members_user_id_fkey
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE chat_messages
    DROP CONSTRAINT chat_messages_sender_id_fkey,
    ADD CONSTRAINT chat_messages_sender_id_fkey
        FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE posts
    DROP CONSTRAINT posts_author_id_fkey,
    ADD CONSTRAINT posts_author_id_fkey
        FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE unread_counts
    DROP CONSTRAINT unread_counts_user_id_fkey,
    ADD CONSTRAINT unread_counts_user_id_fkey
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE reports
    DROP CONSTRAINT reports_reporter_id_fkey,
    ADD CONSTRAINT reports_reporter_id_fkey
        FOREIGN KEY (reporter_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE reports
    DROP CONSTRAINT reports_reported_user_id_fkey,
    ADD CONSTRAINT reports_reported_user_id_fkey
        FOREIGN KEY (reported_user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE reports
    DROP CONSTRAINT reports_reviewed_by_id_fkey,
    ADD CONSTRAINT reports_reviewed_by_id_fkey
        FOREIGN KEY (reviewed_by_id) REFERENCES users(id) ON DELETE SET NULL;
