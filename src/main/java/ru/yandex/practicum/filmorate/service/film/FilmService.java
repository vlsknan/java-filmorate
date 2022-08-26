package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.director.DirectorDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.film.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.LikeDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
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
    private final DirectorDbStorage directorDbStorage;
    private static final LocalDate REFERENCE_DATE = LocalDate.of(1895,12,28);

    //получить список всех фильмов
    @Override
    public Collection<Film> getAll() {
        List<Film> films = filmDbStorage.getAll();
        for (Film film : films) {
            Set<Genre> genre = genreDbStorage.loadFilmGenre(film);
            film.setGenres(genre);

            List<Director> directors = directorDbStorage.loadFilmDirector(film);
            film.setDirectors(directors);
        }
        return films;
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
    public Film create(Film film) throws ValidationException {
        validate(film);
        filmDbStorage.create(film);
        genreDbStorage.setFilmGenre(film);
        film.setGenres(genreDbStorage.loadFilmGenre(film));
        directorDbStorage.setFilmDirector(film);
        return film;
    }

    //обновить данные о фильме
    @Override
    public Film update(Film film) throws ValidationException, SQLException {
        validate(film);
        Optional<Film> res = filmDbStorage.update(film);
        if (res.isPresent()) {
            genreDbStorage.setFilmGenre(res.get());
            directorDbStorage.setFilmDirector(res.get());
            return res.get();
        }
        throw new NotFoundException(String.format("Фильм с id = %s не найден.", film.getId()));
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
    }

    //удалить у фильма лайк
    public void deleteLike(long filmId, long userId) throws SQLException {
        if (!likeDbStorage.deleteLike(filmId, userId)) {
            throw new NotFoundException("Ошибка при удалении лайка.");
        }
    }

    //получить список популярных фильмов (из первых count фильмов по количеству лайков)
    public List<Film> getListPopularFilm(int count, long genreId, int year) {
        List<Film> films = null;
        if (genreId == 0 && year == 0) {
            films = filmDbStorage.getListPopularFilm(count);
        } else if (genreId != 0 && year == 0) {
            films = filmDbStorage.getListPopularFilmSortGenre(count, genreId);
        } else if (year != 0 && genreId == 0) {
            films = filmDbStorage.getListPopularFilmSortYear(count, year);
        } else if (genreId != 0 && year != 0) {
            films = filmDbStorage.getListPopularFilmSortGenreAndYear(count, genreId, year);
        }

        for (Film film : films) {
            Set<Genre> genre = genreDbStorage.loadFilmGenre(film);
            film.setGenres(genre);
        }
        return films;
    }

    public List<Film> getListFilmsDirector(long id, String sort) throws SQLException {
        if (directorDbStorage.getById(id).isEmpty()) {
            throw new NotFoundException(String.format("Режиссер с id = %s не найден", id));
        }
        List<Film> films = filmDbStorage.getListFilmsDirector(id, sort);
        for (Film film : films) {
            film.setGenres(genreDbStorage.loadFilmGenre(film));
            film.setDirectors(directorDbStorage.loadFilmDirector(film));
        }
        return films;
    }

    public List<Film> findPopularFilmsByTitleOrDirector(String query, String by) {
        List<Film> films = filmDbStorage.findPopularFilmsByTitleOrDirector(query.toLowerCase(), by);
        if (films.size() == 0) {
            throw new NotFoundException(String.format("Фильмов по запросу %s не найдено", query));
        }
        for (Film film : films) {
            film.setGenres(genreDbStorage.loadFilmGenre(film));
            film.setDirectors(directorDbStorage.loadFilmDirector(film));
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
