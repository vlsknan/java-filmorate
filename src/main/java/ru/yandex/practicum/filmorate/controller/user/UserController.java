package ru.yandex.practicum.filmorate.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //получить список пользователей
    @GetMapping
    public Collection<User> getUsers() throws SQLException {
        log.info("GET users");
        return userService.getAll();
    }

    //создать пользователя
    @PostMapping
    public User createUser(@RequestBody User user) throws ValidationException {
        log.info("POST user");
        return userService.create(user);
    }

    //обновить данные пользователя
    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException, SQLException {
        log.info("PUT user");
        return userService.update(user);
    }

    //получить пользователя по id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") long userId) throws SQLException {
        log.info("GET user by id");
        return userService.getById(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus>  deleteUserById(@PathVariable long id) {
        log.info("DELETE user by id");
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    //получить список друзей
    @GetMapping("/{id}/friends")
    public List<User> getListFriends(@PathVariable("id") long userId) throws SQLException {
        log.info("GET list friends user with id={}", userId);
        return userService.getListFriends(userId);
    }

    //добавить в друзья
    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<HttpStatus> addInFriends(@PathVariable("id") long userId,
                                                   @PathVariable("friendId") long friendId) throws SQLException {
        log.info("PUT add user in friend");
        userService.addInFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    //удалить из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<HttpStatus> deleteFromFriends(@PathVariable("id") long userId,
                                  @PathVariable("friendId") long friendId) throws SQLException {
        log.info("DELETE user from friend");
        userService.deleteFromFriends(userId, friendId);
        return ResponseEntity.ok().build();
    }

    //получить список общих друзей
    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getListCommonFriends(@PathVariable("id") long userId,
                                           @PathVariable("otherId") long otherId) throws SQLException {
        log.info("GET common friends with user");
        return userService.getListCommonFriends(userId, otherId);
    }

    //получить ленту событий
    @GetMapping("/{userId}/feed")
    public List<Event> getFeed(@PathVariable long userId) throws SQLException {
        log.info("GET list event user {}", userId);
        return userService.getFeed(userId);
    }

    //получить список фильмов-рекоммендаций для пользователя
    @GetMapping("/{id}/recommendations")
    public List<Film> getFilmRecommendations(@PathVariable long id) throws SQLException {
        log.info("GET film recommendations");
        return userService.getFilmRecommendations(id);
    }
}
