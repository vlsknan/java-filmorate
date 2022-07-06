package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.GeneralService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class UserService implements GeneralService<User> {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //создать пользователя
    public User create(User user) throws ValidationException {
        validate(user);
        return userStorage.create(user);
    }

    //обновить данные пользователя
    public User update(User user) throws ValidationException {
        check(user.getId());
        validate(user);
        return userStorage.update(user);
    }

    //получить список пользователей
    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    //получить пользователя по id
    public User getById(long userId) {
        check(userId);
        return userStorage.getById(userId);
    }

    //добавить в друзья
    public void addInFriend(long userId, long friendId) {
        check(userId);
        check(friendId);

        User user = getById(userId);
        User friend = getById(friendId);

        user.getFriends().add(getById(friendId));
        friend.getFriends().add(getById(userId));
    }

    //получить список друзей пользователя user
    public Set<User> getListFriends(long userId) {
        check(userId);
        User user = userStorage.getById(userId);
        return user.getFriends();
    }

    //удалить из друзей
    public void deleteFromFriends(long userId, long friendId) {
        check(userId);
        check(friendId);

        User user = getById(userId);
        User friend = getById(friendId);

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
    }

    //получить список общих друзей
    public List<User> getListCommonFriends(long user1, long user2) {
        check(user1);
        check(user2);

        List<User> commonFriends = new ArrayList<>(getById(user1).getFriends());
        List<User> fr = new ArrayList<>(getById(user2).getFriends());
        commonFriends.retainAll(fr);

        return commonFriends;
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

    public void check(long userId) {
        if (!userStorage.contains(userId)) {
            throw new NotFoundException(String.format("Пользователь с id=%s не найден", userId));
        }
    }
}
