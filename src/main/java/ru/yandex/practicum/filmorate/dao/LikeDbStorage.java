package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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

    public List<Film> getListPopularFilm(long count) throws SQLException {
        final String sqlQuery = "select * from FILMS " +
                "order by COUNT(LIKE_ID) desc" +
                "limit(?)";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery, count);
        final List<Film> films = new ArrayList<>();
        while (rs.next()) {
            Film film = makeFilm(rs);
            films.add(film);
        }
        return films;
    }

    private static Film makeFilm(ResultSet rs) throws SQLException {
        return new Film(rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("description"),
                rs.getDate("Release_Date").toLocalDate(),
                rs.getInt("DURATION"),
                (Set<User>) rs.getObject("LIKE_ID"),
                (Mpa) rs.getObject("MPA_ID"),
                (Set<Genre>) rs.getObject("GENRE_ID")
        );
    }
}
