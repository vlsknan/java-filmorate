package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getUsers(); //получить список пользователей

    User createUser(User user) throws ValidationException; //создать пользователя

    User updateUser(User user) throws ValidationException, IncorrectParameterException; //обновить данные о пользователе

    User getUserById(long userId);

    //void void addInFriends(long userId, long friendId);
    boolean contains(long id);
}
