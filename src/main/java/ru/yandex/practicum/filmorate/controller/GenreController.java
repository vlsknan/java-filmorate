package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.sql.SQLException;
import java.util.Collection;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {
    private final FilmService filmService;

    @Autowired
    public GenreController(FilmService filmService) {
        this.filmService = filmService;
    }

    //получить все жанры
    @GetMapping
    public Collection<Genre> getAllGenre() throws SQLException {
        log.info("GET list genre all");
        return filmService.getAllGenres();
    }

    //получить жанр по id
    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable long id) throws SQLException {
        log.info("GET genre by id");
        return filmService.getGenreById(id);
    }
}
