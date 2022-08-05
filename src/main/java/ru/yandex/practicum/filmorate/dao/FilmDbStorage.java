package ru.yandex.practicum.filmorate.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
        return null;
    }

    @Override
    public Film update(Film film) throws ValidationException {
        return null;
    }

    @Override
    public Film getById(long id) {
        return null;
    }

    @Override
    public boolean contains(long id) {
        return false;
    }

    void setFilmGenre(Film film) {
        String sql = "DELETE * FROM genres where film_id = ?";
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        for (Genre genre : film.getGenres()) {

            // SQL INSERT INTO film_genres(film_id, genre_id) values (?,?)
            // INSERT INTO film_genres(film_id, genre_id) values (?,?);
            // INSERT INTO film_genres(film_id, genre_id) values (?,?);
        }
        // Или сделать через batch

    }

    void loadFilmGenre(List<Film> films) {
        final List<Long> ids = films.stream().map(Film::getId).collect(Collectors.toList());
		final Map<Long, Film> filmMap = new HashMap<>();
        for (Film film : films) {
            filmMap.put(film.getId(), film);
        }

		// SELECT film_id , genres.* FROM genres WHERE film_id in (:ids)
		filmMap.get(film_id).addGenre(new Genre());
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

    @Override
    public List<Genre> getAllGenres() {
        return ;
    }

    @Override
    public Map<Long, Genre> getGenreById(long id) {
        return ;
    }
}
