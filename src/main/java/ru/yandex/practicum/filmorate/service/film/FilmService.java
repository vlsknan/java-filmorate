package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.GeneralService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService implements GeneralService<Film> {
    private final FilmDbStorage filmDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final LikeDbStorage likeDbStorage;
    private static final LocalDate REFERENCE_DATE = LocalDate.of(1895,12,28);

    //получить список всех фильмов
    public Collection<Film> getAll() {
        return filmDbStorage.getAll();
    }

    //получить фильм по id
    public Film getById(long filmId) throws SQLException {
        Optional<Film> film = filmDbStorage.getById(filmId);
        if (film.isPresent()) {
            genreDbStorage.loadFilmGenre(film.get());
            return film.get();
        }
        throw new NotFoundException(String.format("Фильм с id = %s не найден.", filmId));

    }

    //создать фильм
    public Film create(Film film) throws ValidationException {
        validate(film);
        Film newFilm = filmDbStorage.create(film);
        genreDbStorage.setFilmGenre(newFilm);
        return newFilm;
    }

    //обновить данные о фильме
    public Film update(Film film) throws ValidationException, SQLException {
        validate(film);
        Optional<Film> res = filmDbStorage.update(film);
        if (res.isPresent()) {
            genreDbStorage.setFilmGenre(res.get());
            return res.get();
        }
        throw new NotFoundException(String.format("Фильм с id = %s не найден.", film.getId()));

    }

    //добавить фильму лайк
    public void addLike(long filmId, long userId) throws SQLException {
        likeDbStorage.addLike(filmId, userId);
    }

    //удалить у фильма лайк
    public void deleteLike(long filmId, long userId) throws SQLException {
        likeDbStorage.deleteLike(filmId, userId);
    }

    //получить список популярных фильмов (из первых count фильмов по количеству лайков)
    public List<Film> getListPopularFilm(long count) {
        List<Film> films =  filmDbStorage.getListPopularFilm(count);
        for (Film film : films) {
            genreDbStorage.loadFilmGenre(film);
        }
        return films;
    }

    public void validate(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            log.debug("Название фильма не указано");
            throw new ValidationException("Название фильма не указано.");
        }
        if (film.getDescription().length() > 200) {
            log.debug("Описание фильма больше 200 символов");
            throw new ValidationException("Описание фильма не должно превышать 200 символов.");
        }
        if (film.getReleaseDate().isBefore(REFERENCE_DATE)) {
            log.debug("Дата релиза раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            log.debug("Продолжительность фильма отрицательная");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной.");
        }
    }

}
