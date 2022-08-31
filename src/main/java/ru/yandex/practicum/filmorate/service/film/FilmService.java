package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.dao.EventDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.director.DirectorDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.film.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.LikeDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.GeneralService;
import ru.yandex.practicum.filmorate.storage.dao.user.UserDbStorage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService implements GeneralService<Film> {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final LikeDbStorage likeDbStorage;
    private final DirectorDbStorage directorDbStorage;
    private final EventDbStorage eventDbStorage;
    private static final LocalDate REFERENCE_DATE = LocalDate.of(1895,12,28);

    //получить список всех фильмов
    @Override
    public Collection<Film> getAll() {
        List<Film> films = filmDbStorage.getAll();
        return addFilmsGenresAndDirectorsForList(films);
    }

    //получить фильм по id
    @Override
    public Film getById(long filmId) throws SQLException {
        Film film = filmDbStorage.getById(filmId).
                orElseThrow(() -> new NotFoundException(String.format("Фильм с id = %s не найден", filmId)));
        film.setGenres(genreDbStorage.loadFilmGenre(film));
        film.setDirectors(directorDbStorage.loadFilmDirector(film));
        return film;
    }

    //создать фильм
    @Override
    public Film create(Film film) {
        validate(film);
        filmDbStorage.create(film);

        genreDbStorage.setFilmGenre(film);
        film.setGenres(genreDbStorage.loadFilmGenre(film));
        directorDbStorage.setFilmDirector(film);

        return film;
    }

    //обновить данные о фильме
    @Override
    public Film update(Film film) throws SQLException {
        validate(film);
        Film res = filmDbStorage.update(film)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с id = %s не найден.", film.getId())));
        genreDbStorage.setFilmGenre(res);
        directorDbStorage.setFilmDirector(res);
        return res;
    }

    //удалить фильм по id
    @Override
    public void delete(long id) {
        filmDbStorage.delete(id);
    }

    //добавить фильму лайк
    public void addLike(long filmId, long userId) {
        if (!likeDbStorage.addLike(filmId, userId)) {
            throw new NotFoundException("Ошибка при добавлении лайка.");
        }
        eventDbStorage.addLikeEvent(filmId, userId);
    }

    //удалить у фильма лайк
    public void deleteLike(long filmId, long userId) throws SQLException {
        if (!likeDbStorage.deleteLike(filmId, userId)) {
            throw new NotFoundException("Ошибка при удалении лайка.");
        }
        eventDbStorage.deleteLikeEvent(filmId, userId);
    }

    //получить список популярных фильмов (из первых count фильмов по количеству лайков)
    public List<Film> getListPopularFilm(int count, long genreId, int year) {
        List<Film> films;
        if (genreId != 0 && year != 0) {
            films = filmDbStorage.findPopularFilmSortedByGenreAndYear(count, genreId, year);
        } else if (genreId != 0 && year == 0) {
            films = filmDbStorage.getListPopularFilmSortedByGenre(count, genreId);
        } else if (genreId == 0 && year != 0) {
            films = filmDbStorage.getListPopularFilmSortedByYear(count, year);
        } else {
            films = filmDbStorage.getListPopularFilm(count);
        }
        return addFilmsGenresAndDirectorsForList(films);
    }

    public List<Film> getListFilmsDirector(long id, String sort) throws SQLException {
        if (directorDbStorage.getById(id).isEmpty()) {
            throw new NotFoundException(String.format("Режиссер с id = %s не найден", id));
        }
        List<Film> films = null;
        switch (sort) {
            case "year":
                films = filmDbStorage.getListFilmsDirectorByYear(id);
                break;
            case "likes":
                films = filmDbStorage.getListFilmsDirectorByLikes(id);
                break;
        }
        return addFilmsGenresAndDirectorsForList(films);
    }

    //получить список фильмов по поиску по тексту и по режиссеру/названию фильма
    public List<Film> getListFilmsByRequest(String query, String by) {
        List<Film> films;
        switch (by) {
            case "title":
                films = filmDbStorage.getListFilmsByRequestByTitle(query);
                break;
            case "director":
                films = filmDbStorage.getListFilmsByRequestByDirector(query);
                break;
            default:
                films = filmDbStorage.getListFilmsByRequestByTitleAndDirector(query);
                break;
        }
        return addFilmsGenresAndDirectorsForList(films);
    }

    public List<Film> getCommonFilms(long userId, long friendId) throws SQLException {
        userDbStorage.getById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %s не найден", userId)));
        userDbStorage.getById(friendId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %s не найден", friendId)));
        List<Film> films = filmDbStorage.getCommonFilms(userId, friendId);
        return addFilmsGenresAndDirectorsForList(films);
    }

    private List<Film> addFilmsGenresAndDirectorsForList(List<Film> films) {
        return films.stream()
                .peek(f -> f.setGenres(genreDbStorage.loadFilmGenre(f)))
                .peek(f -> f.setDirectors(directorDbStorage.loadFilmDirector(f)))
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
}
