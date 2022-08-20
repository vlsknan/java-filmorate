package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;

@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAll() {
        final String sqlQuery = "select F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, F.RELEASE_DATE, " +
                "F.DURATION, M.MPA_ID, M.MPA_NAME " +
                "from FILMS F " +
                "join MPA M on M.MPA_ID = F.MPA_ID";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    @Override
    public Film create(Film film) throws ValidationException {
        final String sqlQuery = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, " +
                "DURATION, MPA_ID) values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(3, Types.DATE);
            } else {
                stmt.setDate(3, Date.valueOf(releaseDate));
            }
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
            }, keyHolder);
            film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
            return film;
    }

    @Override
    public Optional<Film> update(Film film) {
        final String sqlDelete = "DELETE FROM FILMS_DIRECTORS WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlDelete, film.getId());

        final String sqlQuery = "update FILMS set FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, " +
                " DURATION = ?, MPA_ID = ? " +
                "where FILM_ID = ?";
        return  jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId()) == 0 ?
                Optional.empty() :
                Optional.of(film);
    }

    @Override
    public Optional<Film> getById(long id) throws SQLException {
        final String sqlQuery = "select F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, F.DURATION, " +
                "F.RELEASE_DATE, M.MPA_ID, M.MPA_NAME from FILMS F " +
                "join MPA M on M.MPA_ID = F.MPA_ID " +
                "where F.FILM_ID = ?";
        List<Film> res = jdbcTemplate.query(sqlQuery, this::makeFilm, id);
        return res.size() == 0 ?
                Optional.empty() :
                Optional.of(res.get(0));
    }

    @Override
    public void delete(long id) {
        final String sql = "delete from films where film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Film> getListPopularFilm(long count) {
        final String sqlQuery = "select F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, " +
                "F.RELEASE_DATE, F.DURATION, M.MPA_ID, M.MPA_NAME, G.GENRE_ID " +
                "from FILMS F " +
                "join MPA M on M.MPA_ID = F.MPA_ID " +
                "left join FILMS_GENRES G on F.FILM_ID = G.FILM_ID " +
                "left join LIKES L on F.FILM_ID = L.FILM_ID " +
                "group by F.FILM_ID, G.GENRE_ID order by count(L.USER_ID) desc " +
                "limit ?";
        return jdbcTemplate.query(sqlQuery, this::makeFilm, count);
    }

    public List<Film> getListFilmsDirector(long id, String sort) {
        List<Film> films = null;
        switch (sort) {
            case "year":
                final String sql1 = "SELECT F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, F.RELEASE_DATE, " +
                        "F.DURATION, M.MPA_ID, M.MPA_NAME, FD.DIRECTOR_ID " +
                        "FROM FILMS F " +
                        "JOIN MPA M ON F.MPA_ID = M.MPA_ID " +
                        "JOIN FILMS_DIRECTORS FD ON F.FILM_ID = FD.FILM_ID " +
                        "WHERE FD.DIRECTOR_ID = ? " +
                        "ORDER BY F.RELEASE_DATE";
                films = jdbcTemplate.query(sql1, this::makeFilm, id);
                break;
            case "likes":
                final String sql2 = "SELECT F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, F.RELEASE_DATE, " +
                        "F.DURATION, M.MPA_ID, M.MPA_NAME, FD.DIRECTOR_ID " +
                        "FROM FILMS F " +
                        "JOIN MPA M ON F.MPA_ID = M.MPA_ID " +
                        "LEFT JOIN LIKES L ON F.FILM_ID = L.FILM_ID " +
                        "JOIN FILMS_DIRECTORS FD ON F.FILM_ID = FD.FILM_ID " +
                        "WHERE FD.DIRECTOR_ID = ? " +
                        "GROUP BY F.FILM_ID " +
                        "ORDER BY COUNT(L.USER_ID)";
                films = jdbcTemplate.query(sql2, this::makeFilm, id);
                break;
        }
        return films;
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")),
                new HashSet<>(),
                new ArrayList<>()
        );
    }
}
