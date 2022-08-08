package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
public class LikeDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLike(long filmId, long userId) {
        final String sqlQuery = "insert into LIKES (USER_ID) " +
                "values (?) where FILM_ID = ?";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery, userId, filmId);
    }

    public void deleteLike(long filmId) {
        final String sqlQuery = "delete from LIKES" +
                " where FILM_ID = ?";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery, filmId);
    }
}
