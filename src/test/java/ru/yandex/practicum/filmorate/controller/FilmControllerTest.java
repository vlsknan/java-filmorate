package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    Film film;
    FilmController controller;

    @BeforeEach
    void beforeEach() {
        controller = new FilmController();
        film = new Film();
        film.setName("nisi eiusmod");
        film.setDescription("adipisicing");
        film.setReleaseDate(LocalDate.of(1967, 03, 25));
        film.setDuration(100);
    }

    @Test
    @DisplayName("название пустое")
    void validateNameNullTest() {
        film.setName(null);
        assertThrows(ValidationException.class, () -> controller.validateFilm(film));
    }

    @Test
    @DisplayName("описание больше 200 символов")
    void validateDescriptionMore200Test() {
        film.setDescription("американский комедийный боевик режиссёра и сценариста Роусона Маршалла Тёрбера. " +
                "Главные роли исполнили Дуэйн Джонсон, Райан Рейнольдс и Галь Гадот. Это третий совместный проект " +
                "Тёрбера и Джонсона после картин «Полтора шпиона» и «Небоскрёб», " +
                "третья совместная работа Гадот и Джонсона после фильмов «Форсаж 5» и " +
                "«Форсаж 6» и вторая коллаборация между Джонсоном и Рейнольдсом после фильма «Форсаж: Хоббс и Шоу».");
        assertThrows(ValidationException.class, () -> controller.validateFilm(film));
    }

    @Test
    @DisplayName("id отрицательный")
    void validateIdTest() {
        film.setId(-1);
        assertThrows(ValidationException.class, () -> controller.validateFilm(film));
    }

    @Test
    @DisplayName("продолжительность отрицательная")
    void validateDurationTest() {
        film.setDuration(-10);
        assertThrows(ValidationException.class, () -> controller.validateFilm(film));
    }

    @Test
    @DisplayName("релиз раньше 20 декабря 1895 года")
    void validateReleaseTest() {
        film.setReleaseDate(LocalDate.of(1745, 11,1));
        assertThrows(ValidationException.class, () -> controller.validateFilm(film));
    }
}