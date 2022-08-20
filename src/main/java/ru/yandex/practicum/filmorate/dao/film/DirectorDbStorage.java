package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.sql.*;
import java.util.*;

@Component
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Collection<Director> getAll() throws SQLException {
        final String sql = "select director_id, name from directors";
        return jdbcTemplate.query(sql, this::makeDirector);
    }

    @Override
    public Director create(Director director) throws ValidationException {
        final String sqlQuery = "insert into directors(name) values (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        director.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return director;
    }

    @Override
    public Optional<Director> update(Director director) throws ValidationException {
        final String sql = "update directors set name = ? where director_id = ?";
        return jdbcTemplate.update(sql, director.getName(), director.getId()) == 0 ?
                Optional.empty() :
                Optional.of(director);
    }

    @Override
    public Optional<Director> getById(long id) throws SQLException {
            final String sql = "select director_id, name from directors where director_id = ?";
            List<Director> res = jdbcTemplate.query(sql, this::makeDirector, id);
            return res.size() == 0 ?
                    Optional.empty() :
                    Optional.of(res.get(0));
    }

    @Override
    public void delete(long id) {
        final String sql = "delete from directors where director_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void setFilmDirector(Film film) {
        if (film.getDirectors() == null) {
            return;
        }
        for (Director director : film.getDirectors()) {
            final String sql = "INSERT INTO FILMS_DIRECTORS (FILM_ID, DIRECTOR_ID) " +
                    "VALUES (?, ?)";
            jdbcTemplate.update(sql, film.getId(), director.getId());
        }
    }

    public List<Director> loadFilmDirector(Film film) {
        final String sql = "select d.director_id, d.name from FILMS_directors fd " +
                "join directors d on fd.director_id = d.director_id " +
                "where fd.film_id = ?";
        List<Director> res = jdbcTemplate.query(sql, this::makeDirector, film.getId());
        return new ArrayList<>(res);
    }

    private Director makeDirector(ResultSet rs, int rowNum) throws SQLException {
        return new Director(rs.getLong("director_id"), rs.getString("name"));
    }
}
