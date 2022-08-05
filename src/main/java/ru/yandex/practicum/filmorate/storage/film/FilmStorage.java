package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.Map;


public interface FilmStorage extends Storage<Film> {

    List<Genre> getAllGenres();
    Map<Long, Genre> getGenreById(long id);
}
