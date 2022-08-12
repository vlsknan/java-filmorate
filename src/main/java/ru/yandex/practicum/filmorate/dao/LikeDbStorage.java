package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class LikeDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean addLike(long filmId, long userId) {
        final String sqlQuery = "insert into LIKES (USER_ID, FILM_ID) " +
                "values (?, ?)";
        return jdbcTemplate.update(sqlQuery, userId, filmId) != 0;
    }

    public boolean deleteLike(long filmId, long userId) {
        final String sqlQuery = "delete from LIKES " +
                "where FILM_ID = ? and USER_ID = ?";
       return jdbcTemplate.update(sqlQuery, filmId, userId) != 0;
    }
}
