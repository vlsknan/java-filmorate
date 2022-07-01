package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    User user;
    UserController controller;

    @BeforeEach
    void beforeEach() {
        controller = new UserController();
        user = new User();
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        user.setEmail("mail@mail.ru");
    }

    @Test
    @DisplayName("id отрицательный")
    void validateIdTest() {
        user.setId(-1);
        assertThrows(ValidationException.class, () -> controller.validateUser(user));
    }

    @Test
    @DisplayName("имя пустое")
    void validateNameTest() throws ValidationException {
        user.setName("");
        controller.validateUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    @DisplayName("имя пустое (null)")
    void validateNameNullTest() throws ValidationException {
        user.setName(null);
        controller.validateUser(user);
        assertEquals(user.getLogin(), user.getName());
    }
    @Test
    @DisplayName("почта пустая")
    void validateEmailNullTest() {
        user.setEmail(null);
        assertThrows(ValidationException.class, () -> controller.validateUser(user));
    }

    @Test
    @DisplayName("почта не содержит @")
    void validateEmailTest() {
        user.setEmail("afgs.yan.ru");
        assertThrows(ValidationException.class, () -> controller.validateUser(user));
    }

    @Test
    @DisplayName("логин пустой")
    void validateLoginNullTest() {
        user.setLogin(null);
        assertThrows(ValidationException.class, () -> controller.validateUser(user));
    }

    @Test
    @DisplayName("логин содержит пробел")
    void validateLoginTest() {
        user.setLogin("Asdf Bahk");
        assertThrows(ValidationException.class, () -> controller.validateUser(user));
    }

    @Test
    @DisplayName("день рождение в будущем")
    void validateBirthdayTest() {
        user.setBirthday(LocalDate.of(2024, 7, 14));
        assertThrows(ValidationException.class, () -> controller.validateUser(user));
    }
}