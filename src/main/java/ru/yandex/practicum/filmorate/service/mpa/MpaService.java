package ru.yandex.practicum.filmorate.service.mpa;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.SQLException;
import java.util.Collection;

@Service
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public Collection<Mpa> getMpa() throws SQLException {
        return mpaDbStorage.getAllMpa();
    }

    //получить жанр по id
    public Mpa getMpaById(long id) throws SQLException {
        return mpaDbStorage.getMpaById(id);
    }
}
