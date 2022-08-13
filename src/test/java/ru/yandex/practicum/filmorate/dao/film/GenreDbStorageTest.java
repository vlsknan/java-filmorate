package ru.yandex.practicum.filmorate.dao.film;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.film.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.SQLException;
import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;

    @Test
    void getById() throws SQLException {
//        Genre genreById = genreDbStorage.getGenreById(1);
        Optional<Genre> genreOptional = genreDbStorage.getGenreById(1);
        Assertions.assertThat(genreOptional).isPresent()
                .hasValueSatisfying(genre -> Assertions.assertThat(genre)
                        .hasFieldOrPropertyWithValue("name", "Комедия"));
    }

}