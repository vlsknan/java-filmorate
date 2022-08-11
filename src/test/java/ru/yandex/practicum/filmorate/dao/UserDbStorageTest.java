package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.Optional;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    void testFindUserById() throws SQLException {
        Optional<User> userOptional = userStorage.getById(1);
        Assertions.assertThat(userOptional).isPresent()
                .hasValueSatisfying(user -> Assertions.assertThat(user)
                        .hasFieldOrPropertyWithValue("name", "user_test"));
    }
}
