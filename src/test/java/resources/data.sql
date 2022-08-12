merge into GENRES(GENRE_ID, GENRE_NAME)
    values(1, 'Комедия'), (2, 'Драма'), (3, 'Мультфильм'),
    (4, 'Трилер'), (5, 'Документальный'), (6, 'Боевик');

merge into mpa(mpa_id, mpa_name)
    values(1, 'G'), (2, 'PG'), (3, 'PG-13'), (4, 'R'), (5, 'NC-17');

-- insert into USERS(USER_NAME, LOGIN, EMAIL, BIRTHDAY)
-- values ('user_test', 'login', 'email@email', '2000-05-02');
--
-- insert into FILMS(FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
-- values ('film_test', 'bkufuyd', '2000-05-02', 35, 1);