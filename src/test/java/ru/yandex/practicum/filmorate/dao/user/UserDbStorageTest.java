package ru.yandex.practicum.filmorate.dao.user;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    @Test
    void createUserTest() {
        User user = new User(1, "test@email","test_login", "test_name",
                LocalDate.of(2000,05,02));

        userDbStorage.create(user);
        assertEquals(1, userDbStorage.getAll().size());
    }

    @Test
    void testFindUserById() throws SQLException {
        Optional<User> userOptional = userDbStorage.getById(1);
        Assertions.assertThat(userOptional).isPresent()
                .hasValueSatisfying(user -> Assertions.assertThat(user)
                        .hasFieldOrPropertyWithValue("name", "test_name"));
    }
}
