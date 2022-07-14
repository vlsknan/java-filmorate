package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getUsers(); //получить список пользователей
    User createUser(User user) throws ValidationException; //создать пользователя
    User updateUser(User user) throws ValidationException; //обновить данные о пользователе
    User getUserById(long userId); //получить пользователя по id
    boolean contains(long id);
}
