package ru.yandex.practicum.filmorate.storage.dao.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    //получить все жанры
    public Collection<Genre> getAllGenres() {
        final String sqlQuery = "select * from GENRES";
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    //получить жанр по id
    public Optional<Genre> getGenreById(long id) throws SQLException {
        final String sqlQuery = "select * from GENRES where GENRE_ID = ?";
        List<Genre> res = jdbcTemplate.query(sqlQuery, this::makeGenre, id);
        return res.size() == 0 ?
                Optional.empty() :
                Optional.of(res.get(0));
    }

        public void setFilmGenre(Film film) {
        if (film.getGenres() == null) {
            return;
        }
        String sqlQuery = "delete from FILMS_GENRES where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        for (Genre genre : film.getGenres()) {
            String sql = "insert into FILMS_GENRES(GENRE_ID, FILM_ID) " +
                                "values (?, ?)";
            jdbcTemplate.update(sql, genre.getId(), film.getId());
        }
    }

    public Set<Genre> loadFilmGenre(Film film) {
        final String sqlQuery = "select G.GENRE_ID, G.GENRE_NAME from FILMS_GENRES FG " +
                "join GENRES G on FG.GENRE_ID = G.GENRE_ID " +
                "where FG.FILM_ID = ?";
        List<Genre> res = jdbcTemplate.query(sqlQuery, this::makeGenre, film.getId());
        return new HashSet<>(res);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"),
                rs.getString("GENRE_NAME"));
    }
}
