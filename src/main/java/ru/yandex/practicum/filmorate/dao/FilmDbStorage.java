package ru.yandex.practicum.filmorate.dao;

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
    private final GenreDbStorage genreDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public Collection<Film> getAll() {
        final String sqlQuery = "select F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, F.RELEASE_DATE, " +
                "F.DURATION, M.MPA_ID, M.MPA_NAME, FG.GENRE_ID " +
                "from FILMS F " +
                "join FILMS_GENRES FG on F.FILM_ID = FG.FILM_ID " +
                "join MPA M on M.MPA_ID = F.MPA_ID";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    @Override
    public Film create(Film film) throws ValidationException {
        try {
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
            film.setId((keyHolder.getKey()).longValue());
            return film;
        } catch (Exception ex) {
            throw new ValidationException("Получены некорректные данные");
        }
    }

    @Override
    public Optional<Film> update(Film film) throws ValidationException {
        try {
            final String sqlQuery = "update FILMS set FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, " +
                    " DURATION = ?, MPA_ID = ? " +
                    "where FILM_ID = ?";

            return  jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpa().getId(), film.getId()) == 0 ?
                    Optional.empty() :
                    Optional.of(film);
//            KeyHolder keyHolder = new GeneratedKeyHolder();
//            jdbcTemplate.update(connection -> {
//                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
//                stmt.setString(1, film.getName());
//                stmt.setString(2, film.getDescription());
//                final LocalDate releaseDate = film.getReleaseDate();
//                if (releaseDate == null) {
//                    stmt.setNull(3, Types.DATE);
//                } else {
//                    stmt.setDate(3, Date.valueOf(releaseDate));
//                }
//                stmt.setInt(4, film.getDuration());
//                stmt.setInt(5, film.getMpa().getId());
//                stmt.setLong(6, film.getId());
//                return stmt;
//            }, keyHolder);
//            film.setId(keyHolder.getKey().longValue());
//            return Optional.of(film);
        } catch (Exception ex) {
            throw new NotFoundException("Получены некорректные данные.");
        }
    }

    @Override
    public Optional<Film> getById(long id) throws SQLException {
        final String sqlQuery = "select F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, F.DURATION, " +
                "F.RELEASE_DATE, M.MPA_ID, M.MPA_NAME, FG.GENRE_ID from FILMS F " +
                "join MPA M on M.MPA_ID = F.MPA_ID " +
                "join FILMS_GENRES FG on F.FILM_ID = FG.FILM_ID " +
                "where F.FILM_ID = ?";
        List<Film> res = jdbcTemplate.query(sqlQuery, this::makeFilm, id);
        return res.size() == 0 ?
                Optional.empty() :
                Optional.of(res.get(0));
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

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getLong("FILM_ID"),
                rs.getString("FILM_NAME"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")),
                new ArrayList<>(rs.getInt("GENRE_ID"))
        );
//        return Film.builder()
//                .id(rs.getInt("film_id"))
//                .name(rs.getString("film_name"))
//                .description(rs.getString("description"))
//                .releaseDate(LocalDate.parse(rs.getString("release_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
//                .duration(rs.getInt("duration"))
//                .mpa(Mpa.builder()
//                        .id(rs.getInt("MPA_ID"))
//                        .name(rs.getString("MPA_NAME")).build())
//                .genres(genreDbStorage.getGenreById(rs.getInt("genre_id"))).build();
    }
}
