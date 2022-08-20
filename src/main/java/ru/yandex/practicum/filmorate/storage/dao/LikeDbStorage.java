package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

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
