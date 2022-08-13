package ru.yandex.practicum.filmorate.dao.film;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;

    @Test
    @Order(10)
    void createFilmTest() throws ValidationException {
        Film film = new Film(1, "test_name", "description",
                LocalDate.of(2000, 05, 02), 30, new Mpa(1, "G"),
                new HashSet<>());

        filmDbStorage.create(film);
        assertEquals(1, filmDbStorage.getAll().size());
    }

    @Test
    @Order(20)
    void getById() throws SQLException {
        Optional<Film> userOptional = filmDbStorage.getById(1);
        Assertions.assertThat(userOptional).isPresent()
                .hasValueSatisfying(user -> Assertions.assertThat(user)
                        .hasFieldOrPropertyWithValue("name", "test_name"));
    }
}