package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GeneralService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class UserService implements GeneralService<User> {
    private final UserStorage userDbStorage;
    private final FriendDbStorage friendDbStorage;

    @Autowired
    public UserService(UserStorage userDbStorage, FriendDbStorage friendDbStorage) {
        this.userDbStorage = userDbStorage;
        this.friendDbStorage = friendDbStorage;
    }

    //создать пользователя
    public User create(User user) throws ValidationException {
        validate(user);
        return userDbStorage.create(user);
    }

    //обновить данные пользователя
    public User update(User user) throws ValidationException, SQLException {
        check(user.getId());
        validate(user);
        return userDbStorage.update(user);
    }

    //получить список пользователей
    public Collection<User> getAll() throws SQLException {
        return userDbStorage.getAll();
    }

    //получить пользователя по id
    public User getById(long userId) throws SQLException {
        check(userId);
        return userDbStorage.getById(userId);
    }

    //добавить в друзья
    public void addInFriend(long userId, long friendId) throws SQLException {
        check(userId);
        check(friendId);

//        User user = getById(userId);
//        User friend = getById(friendId);
//
//        user.getFriends().add(getById(friendId));
//        friend.getFriends().add(getById(userId));
        friendDbStorage.addInFriend(userId, friendId);
    }

    //получить список друзей пользователя user
    public Set<User> getListFriends(long userId) throws SQLException {
        check(userId);
        //User user = userDbStorage.getById(userId);
        return friendDbStorage. getListFriends(userId);
    }

    //удалить из друзей
    public void deleteFromFriends(long userId, long friendId) throws SQLException {
        check(userId);
        check(friendId);

//        User user = getById(userId);
//        User friend = getById(friendId);
//
//        user.getFriends().remove(friend);
//        friend.getFriends().remove(user);
        friendDbStorage.deleteFromFriends(userId, friendId);
    }

    //получить список общих друзей
    public List<User> getListCommonFriends(long user1, long user2) throws SQLException {
        check(user1);
        check(user2);

//        List<User> commonFriends = new ArrayList<>(getById(user1).getFriends());
//        List<User> fr = new ArrayList<>(getById(user2).getFriends());
//        commonFriends.retainAll(fr);

        return friendDbStorage.getListCommonFriends(user1, user2);
    }

    public void validate(User user) throws ValidationException {
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

    public void check(long userId) throws SQLException {
        if (!userDbStorage.contains(userId)) {
            throw new NotFoundException(String.format("Пользователь с id=%s не найден", userId));
        }
    }
}
