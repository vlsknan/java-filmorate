package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Collection;

public interface Storage<T> {

    Collection<T> getAll();
    T create(T t) throws ValidationException;
    T update(T t) throws ValidationException;
    T getById(long id);
    boolean contains(long id);
}
