CREATE TABLE Languages (
       id INTEGER PRIMARY KEY AUTOINCREMENT,
       name TEXT NOT NULL,
       abbreviation TEXT CHECK (length(abbreviation)<=4) NOT NULL,
       code TEXT CHECK(length(code) = 5)NOT NULL UNIQUE,
       flag BLOB NULL
);
CREATE TABLE Categories(
       id INTEGER PRIMARY KEY AUTOINCREMENT,
       name  TEXT NOT NULL,
       language_fk INTEGER NOT NULL,
       FOREIGN KEY (language_fk) REFERENCES Languages(id),
       UNIQUE (name, language_fk)
);
CREATE TABLE Translations (
       id INTEGER PRIMARY KEY AUTOINCREMENT,
       content TEXT NOT NULL UNIQUE
);
CREATE TABLE Sentences(
       id INTEGER PRIMARY KEY AUTOINCREMENT,
       content TEXT NOT NULL UNIQUE,
       translation TEXT
);
CREATE TABLE Hints(
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        content TEXT NOT NULL UNIQUE
);
CREATE TABLE Exercises (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL UNIQUE
);
CREATE TABLE Sets (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        language_l2_fk INTEGER,
        language_l1_fk INTEGER,
        FOREIGN KEY (language_l2_fk) REFERENCES Languages(id),
        FOREIGN KEY (language_l1_fk) REFERENCES Languages(id)
);
CREATE TABLE Lessons (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        number INTEGER NOT NULL CHECK(number >= 0) DEFAULT 1,
        set_fk INTEGER NOT NULL,
        FOREIGN KEY (set_fk) REFERENCES Sets(id)
);
CREATE TABLE PartsOfSpeech (
        id INTEGER PRIMARY KEY,
        name TEXT NOT NULL UNIQUE
);
CREATE TABLE Definitions (
        id INTEGER PRIMARY KEY,
        content TEXT NOT NULL,
        translation NULL
);
CREATE TABLE Words (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        content TEXT NOT NULL,
        transcription TEXT NULL,
        definition_fk INTEGER NULL,
        lesson_fk INTEGER NOT NULL,
        part_of_speech_fk INTEGER NULL,
        category_fk INTEGER NULL,
        difficult INTEGER NOT NULL CHECK(difficult >=0 AND difficult <=5) DEFAULT 0,
        master_level INTEGER NOT NULL CHECK(master_level <= 100) DEFAULT 0,
        selected INTEGER NOT NULL CHECK(selected = 0 OR selected = 1) DEFAULT 0,
        own INTEGER NOT NULL CHECK(own = 0 OR own = 1) DEFAULT 1,
        learning_date INTEGER NULL,
        FOREIGN KEY (definition_fk) REFERENCES Definitions(id),
        FOREIGN KEY (lesson_fk) REFERENCES Lessons(id),
        FOREIGN KEY (part_of_speech_fk) REFERENCES PartsOfSpeech(id),
        FOREIGN KEY (category_fk) REFERENCES Categories(id)
);
CREATE TABLE WordsTranslations (
       word_fk INTEGER NOT NULL,
       translation_fk INTEGER NOT NULL,
       PRIMARY KEY(word_fk, translation_fk),
       FOREIGN KEY(word_fk) REFERENCES Words(id),
       FOREIGN KEY (translation_fk) REFERENCES Translations(id)
);
CREATE TABLE ExampleSentences (
        word_fk INTEGER NOT NULL,
        sentence_fk INTEGER NOT NULL,
        FOREIGN KEY (word_fk) REFERENCES Words(id),
        FOREIGN KEY (sentence_fk) REFERENCES Sentences(id)
);

CREATE TABLE WordsHints(
        word_fk INTEGER NOT NULL,
        hint_fk INTEGER NOT NULL,
        FOREIGN KEY (word_fk) REFERENCES Words(id),
        FOREIGN KEY (hint_fk) REFERENCES Hints(id)
);

CREATE TABLE Repetitions (
        word_fk INTEGER PRIMARY KEY,
        date INTEGER NOT NULL,
        FOREIGN KEY(word_fk) REFERENCES Words(id)
);