package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.sql.SQLException;
import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    //получить все жанры
    @GetMapping
    public Collection<Mpa> getMpa() throws SQLException {
        log.info("GET list mpa all");
        return mpaService.getMpa();
    }

    //получить жанр по id
    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable long id) throws SQLException {
        log.info("GET mpa by id");
        return mpaService.getMpaById(id);
    }
}
