//package ru.yandex.practicum.filmorate.storage.film;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exception.ValidationException;
//import ru.yandex.practicum.filmorate.model.Film;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Slf4j
//public class InMemoryFilmStorage implements FilmStorage {
//    public final Map<Long, Film> films;
//    private static long id;
//
//    public InMemoryFilmStorage() {
//        id = 0;
//        films = new HashMap<>();
//    }
//
//    private long nextId() {
//        return ++id;
//    }
//
//    @Override
//    public Collection<Film> getAll() {
//        log.info("Количество фильмов: {}", films.size());
//        return films.values();
//    }
//
//    @Override
//    public Film create(Film film) throws ValidationException {
//        if (films.containsKey(film.getId())) {
//            throw new ValidationException("Фильм \"" +
//                    film.getName() + "\" уже есть в списке.");
//        } else {
//            film.setId(nextId());
//            films.put(film.getId(), film);
//            log.info("Фильм {} создан.", film.getName());
//        }
//        return film;
//    }
//
//    @Override
//    public Film update(Film film) throws ValidationException {
//        if (!films.containsKey(film.getId())) {
//            this.create(film);
//        } else {
//            films.put(film.getId(), film);
//            log.info("Фильм {} обновлен.", film.getName());
//        }
//        return film;
//    }
//
//    @Override
//    public Film getById(long id) {
//        return films.get(id);
//    }
//
//    @Override
//    public boolean contains(long id) {
//        return films.containsKey(id);
//    }
//}
