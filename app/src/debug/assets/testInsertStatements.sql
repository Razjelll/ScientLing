INSERT INTO Languages (name, abbreviation, code)
VALUES ('Angielski', 'ENG','en_EN');
INSERT INTO Languages (name, abbreviation, code)
VALUES ('Hiszpański', 'ESP', 'sp_SP');
INSERT INTO Languages (name, abbreviation, code)
VALUES ('Polski','POL','pl_PL');


INSERT INTO Categories(name, language_fk)
VALUES ('Zwierzęta', 3);
INSERT INTO Categories(name, language_fk)
VALUES ('Animmals',1);
INSERT INTO Categories(name, language_fk)
VALUES ("Czynności", 3);
INSERT INTO Categories(name, language_fk)
VALUES ('Work', 1);
INSERT INTO Categories(name, language_fk)
VALUES  ('War',1);



INSERT INTO Sentences(content, translation)
VALUES ('This is very polite dog', 'To jest bardzo grzeczny piesek');
INSERT INTO Sentences(content, translation)
VALUES ('This is devil Tomek', 'To jest diabeł Tomek');

INSERT INTO Sets(name, language_fk)
VALUES ('Angielski 1', 1);
INSERT INTO Sets(name, language_fk)
VALUES ('English for cgildren', 1);
INSERT INTO Sets(name, language_fk)
VALUES ('Espanol', 2);
INSERT INTO Sets(name, language_fk)
VALUES ('Polski dla obcych', 3);

INSERT INTO Lessons(name, number, set_fk)
VALUES ('1', 1, 1);
INSERT INTO Lessons(name, number, set_fk)
VALUES ('2', 2, 1);
INSERT INTO Lessons(name, number, set_fk)
VALUES ('3', 3, 1);

INSERT INTO PartsOfSpeech(name)
VALUES ('noun');
INSERT INTO PartsOfSpeech(name)
VALUES ('verb');
INSERT INTO PartsOfSpeech(name)
VALUES ('adjective');

INSERT INTO Definitions(content)
VALUES ('the most powerful spirit of evil in Christianity, Judaism, and Islam who is often represented as the ruler of hell');
INSERT INTO Definitions(content, translation)
VALUES ('domestic mammal closely related with gray wold','udomowiony ssak blisko powiązany z szarym wilkiem');


INSERT INTO Words(content, transcription, definition_fk, lesson_fk, part_of_speech_fk, category_fk, difficult, master_level, selected)
VALUES ('dog', 'dog', 2,1,1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('great', 'grejt',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('cause', 'kous',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('old', 'old',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('time', 'tajm',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('time', 'men',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('need', 'nid',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('father', 'fater',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('head', 'hed',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('point', 'pojnt',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('world', 'łerld',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('run', 'ran',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('left', 'left',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('car', 'kar',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('book', 'buk',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('room', 'rum',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('dark', 'dark',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('short', 'szort',1,1,5,-1,0); --1
INSERT INTO Words(content, transcription, lesson_fk,  category_fk, difficult, master_level, selected)
VALUES ('blue', 'blu',1,1,5,-1,0); --1

INSERT INTO Translations(content)
VALUES ('pies'); --1
INSERT INTO Translations(content)
VALUES ('świetnie'); --1
INSERT INTO Translations(content)
VALUES ('przyczyna'); --1
INSERT INTO Translations(content)
VALUES ('powód'); --1
INSERT INTO Translations(content)
VALUES ('stary'); --1
INSERT INTO Translations(content)
VALUES ('czas'); --1
INSERT INTO Translations(content)
VALUES ('mężczyzna'); --1
INSERT INTO Translations(content)
VALUES ('człowiek'); --1
INSERT INTO Translations(content)
VALUES ('potrzebować'); --1
INSERT INTO Translations(content)
VALUES ('potrzeba'); --1
INSERT INTO Translations(content)
VALUES ('ojciec'); --1
INSERT INTO Translations(content)
VALUES ('głowa'); --1
INSERT INTO Translations(content)
VALUES ('główny'); --1
INSERT INTO Translations(content)
VALUES ('wskazywać'); --1
INSERT INTO Translations(content)
VALUES ('punkt'); --1
INSERT INTO Translations(content)
VALUES ('świat'); --1
INSERT INTO Translations(content)
VALUES ('biegać'); --1
INSERT INTO Translations(content)
VALUES ('lewy'); --1
INSERT INTO Translations(content)
VALUES ('pozostawiony'); --1
INSERT INTO Translations(content)
VALUES ('samochód'); --1
INSERT INTO Translations(content)
VALUES ('książka'); --1
INSERT INTO Translations(content)
VALUES ('pokój'); --1
INSERT INTO Translations(content)
VALUES ('ciemny'); --1
INSERT INTO Translations(content)
VALUES ('mroczny'); --1
INSERT INTO Translations(content)
VALUES ('krótki'); --1
INSERT INTO Translations(content)
VALUES ('niebieski'); --1




INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (1,1);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (2,2);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (3,3);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (3,4);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (4,5);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (5,6);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (6,7);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (6,8);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (7,9);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (7,10);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (8,11);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (9,12);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (9,13);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (10,14);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (10,15);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (11,16);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (12,17);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (13,18);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (13,19);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (14,20);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (15,21);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (16,22);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (17,23);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (18,24);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (19,25);
INSERT INTO WordsTranslations (word_fk, translation_fk)
VALUES (20,26);



