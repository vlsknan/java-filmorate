package ru.yandex.practicum.filmorate.dao.user;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.user.UserDbStorage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    @Order(10)
    @Test
    void createAndGetAllUserTest() {
        User user = new User(1, "test@email","test_login", "test_name",
                LocalDate.of(2000,05,02));

        userDbStorage.create(user);
        assertEquals(1, userDbStorage.getAll().size());
    }

    @Order(20)
    @Test
    void testGetById() throws SQLException {
        User user = new User(1, "test321@email","test_login2321", "test_name",
                LocalDate.of(2001,02,03));
        userDbStorage.create(user);

        Optional<User> userOptional = userDbStorage.getById(1);
        Assertions.assertThat(userOptional).isPresent()
                .hasValueSatisfying(user1 -> Assertions.assertThat(user1)
                        .hasFieldOrPropertyWithValue("name", "test_name"));
    }

    @Order(30)
    @Test
    void updateTest() throws SQLException {
        User user = new User(1, "test25326@email","test3452_login", "test_user",
                LocalDate.of(2005,02,12));

        userDbStorage.update(user);
        Optional<User> userOptional = userDbStorage.getById(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user1 ->
                        assertThat(user1).hasFieldOrPropertyWithValue("name",
                                "test_user"));
    }
}
