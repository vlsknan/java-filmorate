package ru.yandex.practicum.filmorate.controller.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

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
        return filmService.getAll();
    }

    //получить фильм по id
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) throws SQLException {
        log.info("GET film by id");
        return filmService.getById(id);
    }

    //создать фильм
    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        log.info("POST create film");
        return filmService.create(film);
    }

    //обновить данные о фильме
    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws SQLException {
        log.info("PUT update film");
        return filmService.update(film);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteFilmByID(@PathVariable long id) {
        log.info("DELETE film by id");
        filmService.delete(id);
        return ResponseEntity.ok().build();
    }

    //поставить лайк фильму
    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<HttpStatus> addLike(@PathVariable("id") long filmId,
                        @PathVariable long userId) throws SQLException {
        log.info("PUT user set liked the film");
        filmService.addLike(filmId, userId);
        return ResponseEntity.ok().build();
    }

    //удалить лайк у фильма
    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<HttpStatus> deleteLike(@PathVariable("id") long filmId,
                                                 @PathVariable long userId) throws SQLException {
        log.info("DELETE user deleted like from the film");
        filmService.deleteLike(filmId, userId);
        return ResponseEntity.ok().build();
    }

    //получить список из первых count фильмов по количеству лайков
    @GetMapping("/popular")
    public Collection<Film> getListPopularFilm(@RequestParam(defaultValue = "10") int count,
                                               @RequestParam(defaultValue = "0") long genreId,
                                               @RequestParam(defaultValue = "0") int year) {
        log.info("GET list popular film(size = count)");
        return filmService.getListPopularFilm(count, genreId, year);
    }

    //список фильмов режиссера
    @GetMapping("/director/{directorId}")
    public List<Film> getListFilmsDirector(@PathVariable long directorId,
                                           @RequestParam String sortBy) throws SQLException {
        log.info("GET list films director sortBy (year/likes)");
        return filmService.getListFilmsDirector(directorId, sortBy);
    }

    //получить список фильмов по определенному запросу (query - текст поиска, by - где текст поиска)
    @GetMapping("/search")
    public List<Film> getListFilmsByRequest(@RequestParam(required = false) String query,
                                            @RequestParam(required = false) String by) {
        log.info("GET list film by request by query-by");
        return filmService.getListFilmsByRequest(query, by);
    }

    //получить общие фильмы пользователей
    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam long userId, @RequestParam long friendId) throws SQLException {
        List<Film> films = filmService.getCommonFilms(userId, friendId);
        log.info("GET list common users {} and {}", userId, friendId);
        return films;
    }
 }
