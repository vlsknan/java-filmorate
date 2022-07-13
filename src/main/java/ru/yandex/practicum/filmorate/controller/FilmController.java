package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //получить список всех фильмов
    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.getFilms();
    }

    //получить фильм по id
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) throws IncorrectParameterException {
        return filmService.getFilmById(id);
    }

    //создать фильм
    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        return filmService.createFilm(film);
    }

    //обновить данные о фильме
    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException, IncorrectParameterException {
        return filmService.updateFilm(film);
    }

    //поставить лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") long filmId,
                        @PathVariable("userId") long userId) throws IncorrectParameterException {
        filmService.addLike(filmId, userId);
    }

    //удалить лайк у фильма
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") long filmId,
                        @PathVariable("userId") long userId) throws IncorrectParameterException {
        filmService.deleteLike(filmId, userId);
    }

    //получить список из первых count фильмов по количеству лайков
    @GetMapping("/popular")
    public Collection<Film> getListPopularFilm(@RequestParam(value = "count", defaultValue = "10") long count) {
        return filmService.getListPopularFilm(count);
    }
 }
