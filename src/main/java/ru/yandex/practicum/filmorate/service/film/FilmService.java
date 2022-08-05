package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GeneralService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService implements GeneralService<Film> {
    private final FilmStorage filmDbStorage;
    private final UserStorage userDbStorage;
    private static final LocalDate REFERENCE_DATE = LocalDate.of(1895,12,28);

    @Autowired
    public FilmService(FilmStorage filmDbStorage, UserStorage userDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
    }

    //получить список всех фильмов
    public Collection<Film> getAll() throws SQLException {
        return filmDbStorage.getAll();
    }

    //получить фильм по id
    public Film getById(long filmId) throws SQLException {
        check(filmId);
        return filmDbStorage.getById(filmId);
    }

    //создать фильм
    public Film create(Film film) throws ValidationException {
        validate(film);
        return filmDbStorage.create(film);
    }

    //обновить данные о фильме
    public Film update(Film film) throws ValidationException, SQLException {
        check(film.getId());
        validate(film);
        return filmDbStorage.update(film);
    }

    //добавить фильму лайк
    public void addLike(long filmId, long userId) throws SQLException {
        Film film = getById(filmId);
        User user = userDbStorage.getById(userId);
        film.getLike().add(user);
    }

    //удалить у фильма лайк
    public void deleteLike(long filmId, long userId) throws SQLException {
        check(filmId);
        Film film = getById(filmId);
        User user = userDbStorage.getById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        film.getLike().remove(user);
    }

    //получить список популярных фильмов (из первых count фильмов по количеству лайков)
    public List<Film> getListPopularFilm(long count) throws SQLException {
        return filmDbStorage.getAll().stream()
                .sorted(Comparator.comparing(film -> film.getLike().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
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

    public void check(long filmId) throws SQLException {
        if (!filmDbStorage.contains(filmId)) {
            throw new NotFoundException(String.format("Фильм с id=%s не найден", filmId));
        }
    }

    public List<Genre> getAllGenres() {
        return filmDbStorage.getAllGenres();
    }

    public Map<Long, Genre> getGenreById(long id) {
        return filmDbStorage.getGenreById(id);
    }
}
