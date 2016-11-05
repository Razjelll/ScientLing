CREATE TABLE Languages (
       id INTEGER PRIMARY KEY AUTOINCREMENT,
       --Komentarz
       name TEXT NOT NULL,
       abbreviation TEXT CHECK (length(abreviation)<=4),
       code TEXT CHECK(length(code) = 5) UNIQUE
);

ALTER TABLE Languages;
-- Komentarz
DROP TABLE /*Komentarz */Languages;
--Komentarz