package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.sql.SQLException;
import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaController {
    private final FilmService filmService;

    @Autowired
    public MpaController(FilmService filmService) {
        this.filmService = filmService;
    }

    //получить все жанры
    @GetMapping
    public Collection<Mpa> getMpa() throws SQLException {
        log.info("GET list mpa all");
        return filmService.getMpa();
    }

    //получить жанр по id
    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable long id) throws SQLException {
        log.info("GET mpa by id");
        return filmService.getMpaById(id);
    }
}
