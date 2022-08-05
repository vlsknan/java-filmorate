package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.sql.SQLException;
import java.util.Collection;

public interface Storage<T> {

    Collection<T> getAll() throws SQLException;
    T create(T t) throws ValidationException;
    T update(T t) throws ValidationException;
    T getById(long id) throws SQLException;
    boolean contains(long id) throws SQLException;
}
