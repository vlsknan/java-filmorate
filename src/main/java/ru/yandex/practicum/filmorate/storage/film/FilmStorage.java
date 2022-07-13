package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getFilms(); //получить список всех фильмов
    Film createFilm(Film film) throws ValidationException; //создать фильм
    Film updateFilm(Film film) throws ValidationException; //обновить данные о фильме
    Film getFilmById(long id);
}
