package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private Film film;
    private FilmController controller;

    @BeforeEach
    protected void beforeEach() {
        controller = new FilmController();
        film = new Film();
        film.setName("nisi eiusmod");
        film.setDescription("adipisicing");
        film.setReleaseDate(LocalDate.of(1967, 03, 25));
        film.setDuration(100);
    }

    @Test
    @DisplayName("название фильма пустое (null)")
    protected void validateNameNullTest() {
        film.setName(null);
        Exception ex = assertThrows(ValidationException.class, () -> controller.validateFilm(film));
        assertEquals("Название фильма не указано.", ex.getMessage());
    }

    @Test
    @DisplayName("название фильма пустое")
    protected void validateNameTest() {
        film.setName("");
        Exception ex = assertThrows(ValidationException.class, () -> controller.validateFilm(film));
        assertEquals("Название фильма не указано.", ex.getMessage());
    }
    @Test
    @DisplayName("описание больше 200 символов")
    protected void validateDescriptionMore200Test() {
        film.setDescription("американский комедийный боевик режиссёра и сценариста Роусона Маршалла Тёрбера. " +
                "Главные роли исполнили Дуэйн Джонсон, Райан Рейнольдс и Галь Гадот. Это третий совместный проект " +
                "Тёрбера и Джонсона после картин «Полтора шпиона» и «Небоскрёб», " +
                "третья совместная работа Гадот и Джонсона после фильмов «Форсаж 5» и " +
                "«Форсаж 6» и вторая коллаборация между Джонсоном и Рейнольдсом после фильма «Форсаж: Хоббс и Шоу».");
        Exception ex = assertThrows(ValidationException.class, () -> controller.validateFilm(film));
        assertEquals("Описание фильма не должно превышать 200 символов.", ex.getMessage());
    }

    @Test
    @DisplayName("id отрицательный")
    protected void validateIdTest() {
        film.setId(-1);
        Exception ex = assertThrows(ValidationException.class, () -> controller.validateFilm(film));
        assertEquals("Id не может быть отрицательным.", ex.getMessage());
    }

    @Test
    @DisplayName("продолжительность отрицательная")
    protected void validateDurationTest() {
        film.setDuration(-10);
        Exception ex = assertThrows(ValidationException.class, () -> controller.validateFilm(film));
        assertEquals("Продолжительность фильма не может быть отрицательной.", ex.getMessage());
    }

    @Test
    @DisplayName("релиз раньше 20 декабря 1895 года")
    protected void validateReleaseTest() {
        film.setReleaseDate(LocalDate.of(1745, 11,1));
        Exception exception = assertThrows(ValidationException.class, () -> controller.validateFilm(film));
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года.", exception.getMessage());
    }
}