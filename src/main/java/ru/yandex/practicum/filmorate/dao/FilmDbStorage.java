package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
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
    public Collection<Film> getAll() throws SQLException {
        final String sqlQuery = "select * " +
                "from FILMS";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery);
        final Collection<Film> films = new ArrayList<>();
        while (rs.next()) {
            Film film = makeFilm(rs);
            films.add(film);
        }
        return films;
    }

    @Override
    public Film create(Film film) throws ValidationException {
        final String sqlQuery = "insert into FILMS (FILM_NAME, DESCRIPTION, DURATION, RELEASE_DATE) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getDuration());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(releaseDate));
            }
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        return film;
    }

    @Override
    public Film update(Film film) throws ValidationException {
        final String sqlQuery = "update FILMS set FILM_NAME = ?, DESCRIPTION = ?, DURATION = ?, " +
                "RELEASE_DATE = ?";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getDuration());
            final LocalDate releaseDate = film.getReleaseDate();
            if (releaseDate == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(releaseDate));
            }
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        return film;
    }

    @Override
    public Film getById(long id) throws SQLException {
        final String sqlQuery = "select * from FILMS where FILM_ID = ?";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery);
        return makeFilm(rs);
    }

    @Override
    public boolean contains(long id) throws SQLException {
        final String sqlQuery = "select FILM_ID from FILMS where FILM_ID = ?";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery);
        if (makeFilm(rs) != null) {
            return true;
        }
        return false;
    }

//    void setFilmGenre(Film film) {
//        String sql = "DELETE * FROM genres where film_id = ?";
//        if (film.getGenres() == null || film.getGenres().isEmpty()) {
//            return;
//        }
//        for (Genre genre : film.getGenres()) {
//
//            // SQL INSERT INTO film_genres(film_id, genre_id) values (?,?)
//            // INSERT INTO film_genres(film_id, genre_id) values (?,?);
//            // INSERT INTO film_genres(film_id, genre_id) values (?,?);
//        }
//        // Или сделать через batch
//
//    }
//
//    void loadFilmGenre(List<Film> films) {
//        final List<Long> ids = films.stream().map(Film::getId).collect(Collectors.toList());
//		final Map<Long, Film> filmMap = new HashMap<>();
//        for (Film film : films) {
//            filmMap.put(film.getId(), film);
//        }
//
//		// SELECT film_id , genres.* FROM genres WHERE film_id in (:ids)
//		filmMap.get(film_id).addGenre(new Genre());
//	}

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
