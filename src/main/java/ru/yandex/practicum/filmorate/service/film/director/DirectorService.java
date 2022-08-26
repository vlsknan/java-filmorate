package ru.yandex.practicum.filmorate.service.film.director;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.dao.director.DirectorDbStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.GeneralService;

import java.sql.SQLException;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectorService implements GeneralService<Director> {
    private final DirectorDbStorage directorDbStorage;

    @Override
    public Collection<Director> getAll() throws SQLException {
        return directorDbStorage.getAll();
    }

    @Override
    public Director getById(long id) throws SQLException {
        Director director = directorDbStorage.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Режиссер с id = %s не найден", id)));
        return director;
    }

    @Override
    public Director create(Director director) throws ValidationException {
        validate(director);
        return directorDbStorage.create(director);
    }

    @Override
    public Director update(Director director) throws ValidationException, SQLException {
        getById(director.getId());
        validate(director);
        return directorDbStorage.update(director).get();
    }

    @Override
    public void delete(long id) throws SQLException {
        getById(id);
        directorDbStorage.delete(id);
    }

    @Override
    public void validate(Director director) throws ValidationException {
        if (director.getName() == null || director.getName().isBlank()) {
            log.debug("Название фильма не указано");
            throw new ValidationException("Название фильма не указано.");
        }
    }
}
