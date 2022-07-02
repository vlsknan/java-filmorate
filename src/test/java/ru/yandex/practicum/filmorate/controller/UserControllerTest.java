package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private User user;
    private UserController controller;

    @BeforeEach
    protected void beforeEach() {
        controller = new UserController();
        user = new User();
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        user.setEmail("mail@mail.ru");
    }

    @Test
    @DisplayName("id отрицательный")
    protected void validateIdTest() {
        user.setId(-1);
        Exception ex = assertThrows(ValidationException.class, () -> controller.validateUser(user));
        assertEquals("Id не может быть отрицательным.", ex.getMessage());
    }

    @Test
    @DisplayName("имя пустое")
    protected void validateNameTest() throws ValidationException {
        user.setName("");
        controller.validateUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    @DisplayName("имя пустое (null)")
    protected void validateNameNullTest() throws ValidationException {
        user.setName(null);
        controller.validateUser(user);
        assertEquals(user.getLogin(), user.getName());
    }
    @Test
    @DisplayName("почта пустая")
    protected void validateEmailNullTest() {
        user.setEmail(null);
        Exception ex = assertThrows(ValidationException.class, () -> controller.validateUser(user));
        assertEquals("Проверьте адрес электронной почты.", ex.getMessage());
    }

    @Test
    @DisplayName("почта не содержит @")
    protected void validateEmailTest() {
        user.setEmail("afgs.yan.ru");
        Exception ex = assertThrows(ValidationException.class, () -> controller.validateUser(user));
        assertEquals("Проверьте адрес электронной почты.", ex.getMessage());;
    }

    @Test
    @DisplayName("логин пустой")
    protected void validateLoginNullTest() {
        user.setLogin(null);
        Exception ex = assertThrows(ValidationException.class, () -> controller.validateUser(user));
        assertEquals("Логин не может содержать пробелы или быть пустым", ex.getMessage());
    }

    @Test
    @DisplayName("логин содержит пробел")
    protected void validateLoginTest() {
        user.setLogin("Asdf Bahk");
        Exception ex = assertThrows(ValidationException.class, () -> controller.validateUser(user));
        assertEquals("Логин не может содержать пробелы или быть пустым", ex.getMessage());
    }

    @Test
    @DisplayName("день рождение в будущем")
    protected void validateBirthdayTest() {
        user.setBirthday(LocalDate.of(2024, 7, 14));
        Exception ex = assertThrows(ValidationException.class, () -> controller.validateUser(user));
        assertEquals("Дата рождения не может быть в будущем.", ex.getMessage());
    }
}