create table IF NOT EXISTS GENRES
(
    GENRE_ID   LONG primary key,
    GENRE_NAME CHARACTER VARYING(50),
    constraint GENRES_PK
        primary key (GENRE_ID)
);

create table IF NOT EXISTS MPA
(
    MPA_ID      LONG primary key,
    MPA_NAME    CHARACTER VARYING(50),
    constraint MPA_PK
        primary key (MPA_ID)
);

create table IF NOT EXISTS FRIENDS
(
    FRIEND_ID LONG primary key,
    USER_ID   LONG,
    STATUS    BOOLEAN
);

create table IF NOT EXISTS USERS
(
    USER_ID   LONG primary key auto_increment,
    USER_NAME CHARACTER VARYING(100) not null,
    LOGIN     CHARACTER VARYING(100),
    EMAIL     CHARACTER VARYING(100) not null,
    BIRTHDAY  DATE                   not null,
    FRIEND_ID LONG REFERENCES FRIENDS(FRIEND_ID) ,
    constraint user_id
    primary key (user_id)
    );

create unique index USERS_EMAIL_UINDEX
    on USERS (EMAIL);

create unique index USERS_LOGIN_UINDEX
    on USERS (LOGIN);

create table IF NOT EXISTS LIKES
(
    LIKE_ID LONG primary key auto_increment,
    USER_ID LONG REFERENCES USERS(USER_ID),
    constraint LIKES_PK
        primary key (LIKE_ID)
);

create table IF NOT EXISTS FILMS
(
    FILM_ID      LONG primary key auto_increment,
    FILM_NAME    CHARACTER VARYING(100) not null,
    DESCRIPTION  CHARACTER VARYING(200) DEFAULT 'unknown',
    RELEASE_DATE DATE                   not null,
    DURATION     INTEGER                DEFAULT 'unknown',
    LIKE_ID      INTEGER REFERENCES LIKES(LIKE_ID),
    GENRE_ID     INTEGER REFERENCES GENRES(GENRE_ID),
    MPA_ID       INTEGER REFERENCES MPA(MPA_ID),
    constraint FILMS_PK
    primary key (FILM_ID)
    );

