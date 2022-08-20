package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public interface Storage<T> {

    Collection<T> getAll() throws SQLException;
    T create(T t) throws ValidationException;
    Optional<T> update(T t) throws ValidationException;
    Optional<T> getById(long id) throws SQLException;
    void delete(long id);
}
