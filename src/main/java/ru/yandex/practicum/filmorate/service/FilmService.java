package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private static final LocalDate REFERENCE_DATE = LocalDate.of(1895,12,28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    //получить список всех фильмов
    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    //получить фильм по id
    public Film getFilmById(long filmId) throws IncorrectParameterException {
        if (filmId < 0) {
            throw new IncorrectParameterException("id фильма не может быть отрицательным.");
        }
        return filmStorage.getFilmById(filmId);
    }

    //создать фильм
    public Film createFilm(Film film) throws ValidationException {
        validateFilm(film);
        return filmStorage.createFilm(film);
    }

    //обновить данные о фильме
    public Film updateFilm(Film film) throws ValidationException {
        validateFilm(film);
        return filmStorage.updateFilm(film);
    }

    //добавить фильму лайк
    public void addLike(long filmId, long userId) throws IncorrectParameterException {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        film.getLike().add(user.getId());
    }

    //удалить у фильма лайк
    public void deleteLike(long filmId, long userId) throws IncorrectParameterException {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        film.getLike().remove(user.getId());
    }

    //получить список популярных фильмов (из первых count фильмов по количеству лайков)
    public Collection<Film> getListPopularFilm(long count) {
        return filmStorage.getFilms().stream()
                .sorted(Collections.reverseOrder())
                // .map(Film::getLike)
                //.sorted(Comparator.comparing(Film::getLike).reverseOrder())
                //.sorted(Comparator.reverseOrder())
                .limit(count)
                .collect(Collectors.toList());
    }

    public void validateFilm(Film film) throws ValidationException {
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
        if (film.getId() < 0) {
            log.debug("id отрицателен");
            throw new ValidationException("Id не может быть отрицательным.");
        }
    }
}
