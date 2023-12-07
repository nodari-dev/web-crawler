CREATE TABLE IF NOT EXISTS seo (
    title text,
    description text,
    url text NOT NULL PRIMARY KEY,
    keywords text NOT NULL
);

CREATE TABLE IF NOT EXISTS hosts (
   host text NOT NULL PRIMARY KEY,
   banned text,
   delay int
);