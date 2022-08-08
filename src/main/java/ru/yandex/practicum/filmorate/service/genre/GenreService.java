package ru.yandex.practicum.filmorate.service.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.GenreController;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.SQLException;
import java.util.Collection;

@Service
@Slf4j
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    //получить все жанрры
    public Collection<Genre> getAllGenres() throws SQLException {
        return genreDbStorage.getAllGenres();
    }

    //получить жанр по id
    public Genre getGenreById(long id) throws SQLException {
        return genreDbStorage.getGenreById(id);
    }
}
