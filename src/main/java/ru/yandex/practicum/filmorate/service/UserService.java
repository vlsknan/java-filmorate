package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //создать пользователя
    public User createUser(User user) throws ValidationException {
        validateUser(user);
        return userStorage.createUser(user);
    }

    //обновить данные пользователя
    public User updateUser(User user) throws ValidationException, IncorrectParameterException {
        validateUserExists(user.getId());
        validateUser(user);
        return userStorage.updateUser(user);
    }

    //получить список пользователей
    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    //получить пользователя по id
    public User getUserById(long userId) throws IncorrectParameterException {
        if (userId < 0) {
            throw new IncorrectParameterException("id пользователя не может быть отрицательным.");
        }
        return userStorage.getUserById(userId);
    }

    //добавить в друзья
    public void addInFriend(long userId, long friendId) throws IncorrectParameterException {
        validateUserExists(userId);
        validateUserExists(friendId);

        User user = getUserById(userId);
        User friend = getUserById(friendId);

//        user.getFriends().add(getUserById(friendId));
//        friend.getFriends().add(getUserById(userId));
        user.addInFriends(friend);
    }

    //получить список друзей пользователя user
    public Set<User> getListFriends(long userId) throws IncorrectParameterException {
        validateUserExists(userId);
        User user = userStorage.getUserById(userId);
        return user.getFriends();
    }

    //удалить из друзей
    public void deleteFromFriends(long userId, long friendId) throws IncorrectParameterException {
        validateUserExists(userId);
        User user = getUserById(userId);
        validateUserExists(friendId);
        User friend = getUserById(friendId);

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
    }

    //получить список общих друзей
    public List<User> getListCommonFriends(long user1, long user2) throws IncorrectParameterException {
        validateUserExists(user1);
        validateUserExists(user2);

        List<User> commonFriends = new ArrayList<>(getUserById(user1).getFriends());
        List<User> fr = new ArrayList<>(getUserById(user2).getFriends());
        commonFriends.retainAll(fr);

        return commonFriends;
    }

    public void validateUser(User user) throws ValidationException {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            log.debug("Адрес электронной почты пуст/не содержит @");
            throw new ValidationException("Проверьте адрес электронной почты.");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            log.debug("Логин содержит пробелы/пустой");
            throw new ValidationException("Логин не может содержать пробелы или быть пустым");
        }
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Дата рождения в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        if (user.getId() < 0) {
            log.debug("id отрицателен");
            throw new ValidationException("Id не может быть отрицательным.");
        }
    }

    protected void validateUserExists(long user) throws IncorrectParameterException {
        if (getUserById(user) == null) {
            throw new IncorrectParameterException(String.format("Пользователь c id %s не найден.", user));
        }
    }
}
