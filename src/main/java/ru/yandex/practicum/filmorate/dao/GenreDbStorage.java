package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    //получить все жанры
    public Collection<Genre> getAllGenres() throws SQLException {
        final String sqlQuery = "select * from GENRES";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery);
        final Collection<Genre> genres = new ArrayList<>();
        while (rs.next()) {
            Genre genre = new Genre(rs.getLong("GENRE_ID"),
                    rs.getString("GENRE_NAME"));
            genres.add(genre);
        }
        return genres;
    }

    //получить жанр по id
    public Genre getGenreById(long id) throws SQLException {
        final String sqlQuery = "select * from GENRES where GENRE_ID = ?";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery);
        return new Genre(rs.getLong("GENRE_ID"),
                rs.getString("GENRE_NAME"));
    }
}
