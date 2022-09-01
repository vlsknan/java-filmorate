package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.sql.SQLException;
import java.util.Collection;

public interface GeneralService<T> {
    Collection<T> getAll() throws SQLException;
    T getById(long id) throws SQLException;
    T create(T t) throws ValidationException, SQLException;
    T update(T t) throws ValidationException, SQLException;
    void delete(long id) throws SQLException;
    void validate(T t) throws ValidationException, SQLException;
}
