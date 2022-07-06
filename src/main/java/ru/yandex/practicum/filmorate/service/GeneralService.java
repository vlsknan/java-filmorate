package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Collection;

public interface GeneralService<T> {
    Collection<T> getAll();
    T getById(long id);
    T create(T t) throws ValidationException;
    T update(T t) throws ValidationException;
    void validate(T t) throws ValidationException;
    void check(long id);
}
