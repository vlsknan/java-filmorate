package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;

    @Test
    void getById() throws SQLException {
        Optional<Film> userOptional = filmDbStorage.getById(1);
        Assertions.assertThat(userOptional).isPresent()
                .hasValueSatisfying(user -> Assertions.assertThat(user)
                        .hasFieldOrPropertyWithValue("name", "film_test"));
    }
}