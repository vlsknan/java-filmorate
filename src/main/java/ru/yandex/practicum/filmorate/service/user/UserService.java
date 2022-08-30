package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.user.FriendDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GeneralService;
import ru.yandex.practicum.filmorate.storage.interf.UserStorage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements GeneralService<User> {
    private final UserStorage userDbStorage;
    private final FriendDbStorage friendDbStorage;

    //создать пользователя
    @Override
    public User create(User user) {
            validate(user);
            return userDbStorage.create(user);
    }

    //обновить данные пользователя
    @Override
    public User update(User user) throws SQLException {
        validate(user);
        Optional<User> res = userDbStorage.update(user);
        if (res.isPresent()) {
            return res.get();
        }
        throw new NotFoundException(String.format("Пользователь с id = %s не найден.", user.getId()));
    }

    //получить список пользователей
    @Override
    public Collection<User> getAll() {
        try {
            return userDbStorage.getAll();
        } catch (Exception ex) {
            throw new NotFoundException("Ошибка получения списка пользоваелей");
        }
    }

    //получить пользователя по id
    @Override
    public User getById(long userId) throws SQLException {
        return userDbStorage.getById(userId).
                orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = %s не найден", userId)));
    }

    @Override
    public void delete(long id) {
        userDbStorage.delete(id);
    }

    //добавить в друзья
    public void addInFriend(long userId, long friendId) throws SQLException {
        try {
            friendDbStorage.addInFriend(userId, friendId);
        } catch (Exception ex) {
            throw new NotFoundException("Ошибка при добавлении друга");
        }
    }

    //удалить из друзей
    public void deleteFromFriends(long userId, long friendId) throws SQLException {
        friendDbStorage.deleteFromFriends(userId, friendId);
    }

    //получить список друзей пользователя user
    public List<User> getListFriends(long userId) throws SQLException {
        return friendDbStorage. getListFriends(userId);
    }

    //получить список общих друзей
    public List<User> getListCommonFriends(long user1, long user2) throws SQLException {
        return friendDbStorage.getListCommonFriends(user1, user2);
    }

    public void validate(User user) {
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
    }
}
