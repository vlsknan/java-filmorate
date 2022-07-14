package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private static UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //получить список пользователей
    @GetMapping
    public Collection<User> getUsers() {
        log.info("GET users");
        return userService.getUsers();
    }

    //создать пользователя
    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        log.info("POST user");
        return userService.createUser(user);
    }

    //обновить данные пользователя
    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException, IncorrectParameterException {
        log.info("PUT user");
        return userService.updateUser(user);
    }

    //получить пользователя по id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") long userId) throws IncorrectParameterException {
        log.info("GET user by id");
        return userService.getUserById(userId);
    }

    //получить список друзей
    @GetMapping("/{id}/friends")
    public Set<User> getListFriends(@PathVariable("id") long userId) throws IncorrectParameterException {
        log.info("GET list friends user with id={}", userId);
        return userService.getListFriends(userId);
    }

    //добавить в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public void addInFriends(@PathVariable("id") long userId,
                             @PathVariable("friendId") long friendId) throws IncorrectParameterException {
        log.info("PUT add user in friend");
        userService.addInFriend(userId, friendId);
    }

    //удалить из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable("id") long userId,
                                  @PathVariable("friendId") long friendId) throws IncorrectParameterException {
        log.info("DELETE user from friend");
        userService.deleteFromFriends(userId, friendId);
    }

    //получить список общих друзей
    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getListCommonFriends(@PathVariable("id") long userId,
                                             @PathVariable("otherId") long otherId) throws IncorrectParameterException {
        log.info("GET common friends with user");
        return userService.getListCommonFriends(userId, otherId);
    }
}
