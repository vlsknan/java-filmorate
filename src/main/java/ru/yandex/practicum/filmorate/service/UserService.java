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
        //validateUserExists(userId); //непройденные тесты увеличиваются
        return userStorage.getUserById(userId);
    }

    //добавить в друзья
    public void addInFriend(long userId, long friendId) throws IncorrectParameterException {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
    }

    //получить список друзей пользователя user
    public Set<Long> getListFriends(long userId) throws IncorrectParameterException {
        validateUserExists(userId);
        User user = userStorage.getUserById(userId);
        Set<Long> friends = user.getFriends();
        return friends;
    }

    //удалить из друзей
    public void deleteFromFriends(long userId, long friendId) throws IncorrectParameterException {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
    }

    //получить список общих друзей
    public Set<Long> getListCommonFriends(long user1, long user2) throws IncorrectParameterException {
//        User user = getUserById(user1);
//        User other = getUserById(user2);
//        Set<Long> commonFriends = new HashSet<>();
//        for (Long userList : user.getFriends()) {
//            for (Long otherUserList : other.getFriends()) {
//                if (userList == otherUserList) {
//                 commonFriends.add(userList);
//                }
//            }
//        }
//        return commonFriends;
        Set<Long> user1Friends = getUserById(user1).getFriends();
        Set<Long> user2Friends = getUserById(user2).getFriends();
        user1Friends.retainAll(user2Friends);
        return user2Friends;
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
