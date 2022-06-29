package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        validateUser(user);
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        } else {
            user.setId(++id);
            users.put(user.getId(), user);
            log.info("Пользователь с адресом электронной почты {} создан", user.getEmail());
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        validateUser(user);
        if (!users.containsKey(user.getId())) {
            createUser(user);
        } else {
            users.put(user.getId(), user);
            log.info("Пользователь с адресом электронной почты {} обновлен", user.getEmail());
        }
        return user;
    }

    protected void validateUser(User user) throws ValidationException {
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
}
