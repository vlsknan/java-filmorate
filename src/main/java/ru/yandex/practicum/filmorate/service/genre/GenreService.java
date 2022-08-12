package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    //получить все жанрры
    public Collection<Genre> getAllGenres() throws SQLException {
        return genreDbStorage.getAllGenres();
    }

    //получить жанр по id
    public Genre getGenreById(long id) throws SQLException {
        Optional<Genre> resGenre = genreDbStorage.getGenreById(id);
        if (resGenre.isPresent()) {
            return resGenre.get();
        }
//        List<Genre> resGenre = genreDbStorage.getGenreById(id);
//        if (resGenre.size() != 0) {
//            return resGenre.get(0);
//        }
        throw new NotFoundException(String.format("Жанр с id = %s не найден.", id));
    }
}
