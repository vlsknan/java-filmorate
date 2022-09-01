package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.dao.director.DirectorDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.film.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.user.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LikeDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmDbStorage filmDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final DirectorDbStorage directorDbStorage;
    private final UserDbStorage userDbStorage;

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

    public List<Film> getFilmRecommendations(long id) throws SQLException {
        List<Film> recommendations = new ArrayList<>();
        String sqlQuery = "SELECT L2.USER_ID " +
                "FROM LIKES AS L1 " +
                "JOIN LIKES AS L2 ON L1.FILM_ID = L2.FILM_ID " +
                "WHERE L1.USER_ID<>L2.USER_ID AND L1.USER_ID = ? " +
                "GROUP BY L2.USER_ID ORDER BY COUNT(L2.USER_ID) DESC " +
                "LIMIT 1";
        List<Long> sameLikesUser = jdbcTemplate.queryForList(sqlQuery, Long.class, id);
        if (sameLikesUser.size() != 1) {
            return recommendations;
        }
        Long sameLikesUserId = sameLikesUser.get(0);

        String sqlQuery2 = "SELECT FILM_ID FROM LIKES WHERE USER_ID = ? " +
                "EXCEPT (SELECT FILM_ID FROM LIKES WHERE USER_ID = ?)";
        List<Long> filmDifferences = jdbcTemplate.queryForList(sqlQuery2, Long.class, sameLikesUserId, id);
        for(Long filmId : filmDifferences) {
            Film film = filmDbStorage.getById(filmId).get();
            film.setGenres(genreDbStorage.loadFilmGenre(film));
            film.setDirectors(directorDbStorage.loadFilmDirector(film));
            recommendations.add(film);
        }
        return recommendations;
    }


    public List<Like> getLikes(long userId, long filmId) {
        String sqlQuery = "SELECT L.USER_ID, L.FILM_ID " +
                "FROM LIKES L " +
                "WHERE L.USER_ID = ? AND L.FILM_ID = ? ";
        return jdbcTemplate.query(sqlQuery, this::makeLike, userId, filmId);
    }

    private Like makeLike(ResultSet rs, int num) throws SQLException {
        return new Like(
                userDbStorage.getById(rs.getLong("USER_ID")).get(),
                filmDbStorage.getById(rs.getLong("FILM_ID")).get());
    }
}
