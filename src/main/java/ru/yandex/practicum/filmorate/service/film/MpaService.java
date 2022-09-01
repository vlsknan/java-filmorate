package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.film.MpaDbStorage;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    public Collection<Mpa> getMpa() {
        return mpaDbStorage.getAllMpa();
    }

    //получить жанр по id
    public Mpa getMpaById(long id) {
        Optional<Mpa> res = mpaDbStorage.getMpaById(id);
        if (res.isPresent()) {
            return res.get();
        }
        throw new NotFoundException(String.format("Рейтинг с id = %s не найден.", id));
    }
}
