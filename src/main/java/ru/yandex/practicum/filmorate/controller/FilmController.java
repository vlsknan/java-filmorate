package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //получить список всех фильмов
    @GetMapping
    public Collection<Film> getFilms() {
        log.info("GET list films all");
        return filmService.getFilms();
    }

    //получить фильм по id
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        log.info("GET film by id");
        return filmService.getFilmById(id);
    }

    //создать фильм
    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        log.info("POST create film");
        return filmService.createFilm(film);
    }

    //обновить данные о фильме
    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        log.info("PUT update film");
        return filmService.updateFilm(film);
    }

    //поставить лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") long filmId,
                        @PathVariable long userId) {
        log.info("PUT user set liked the film");
        filmService.addLike(filmId, userId);
    }

    //удалить лайк у фильма
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") long filmId,
                           @PathVariable long userId) {
        log.info("DELETE user deleted like from the film");
        filmService.deleteLike(filmId, userId);
    }

    //получить список из первых count фильмов по количеству лайков
    @GetMapping("/popular")
    public Collection<Film> getListPopularFilm(@RequestParam(defaultValue = "10") int count) {
        log.info("GET list popular film(size = count)");
        return filmService.getListPopularFilm(count);
    }
 }
