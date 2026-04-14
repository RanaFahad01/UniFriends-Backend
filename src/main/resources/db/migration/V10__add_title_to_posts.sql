ALTER TABLE posts ADD COLUMN title VARCHAR(150) NOT NULL DEFAULT '';

-- Remove the default after backfilling so future inserts must supply a title
ALTER TABLE posts ALTER COLUMN title DROP DEFAULT;
