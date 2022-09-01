package ru.yandex.practicum.filmorate.controller.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.film.director.DirectorService;

import java.sql.SQLException;
import java.util.Collection;

@RestController
@RequestMapping("/directors")
@Slf4j
public class DirectorController {
    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    //Получить список всех режиссёров
    @GetMapping
    public Collection<Director> getAll() throws SQLException {
        log.info("GET list directors all");
        return directorService.getAll();
    }

    //Получение режиссёра по id
    @GetMapping("/{id}")
    public Director getById(@PathVariable long id) throws SQLException {
        log.info("GET director by id");
        return directorService.getById(id);
    }

    //Создание режиссёра
    @PostMapping
    public Director create(@RequestBody Director director) throws ValidationException {
        log.info("POST create director");
        return directorService.create(director);
    }

    //Изменение режиссёра
    @PutMapping
    public Director update(@RequestBody Director director) throws ValidationException, SQLException {
        log.info("PUT update director");
        return directorService.update(director);
    }

    //Удаление режиссёра
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable long id) throws SQLException {
        directorService.delete(id);
        log.info("DELETE delete director");
        return ResponseEntity.ok().build();
    }
}
