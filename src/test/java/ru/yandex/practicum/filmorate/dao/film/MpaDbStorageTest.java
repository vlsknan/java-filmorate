package ru.yandex.practicum.filmorate.dao.film;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.film.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    void getById() {
        Optional<Mpa> mpaOptional = mpaDbStorage.getMpaById(1);
        Assertions.assertThat(mpaOptional).isPresent()
                .hasValueSatisfying(mpa -> Assertions.assertThat(mpa)
                        .hasFieldOrPropertyWithValue("name", "G"));
    }

    @Test
    void getAllTest() {
        assertEquals(5, mpaDbStorage.getAllMpa().size());
    }
}