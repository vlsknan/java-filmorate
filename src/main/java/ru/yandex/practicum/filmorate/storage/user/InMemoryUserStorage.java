//package ru.yandex.practicum.filmorate.storage.user;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exception.ValidationException;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Slf4j
//public class InMemoryUserStorage implements UserStorage {
//    public final Map<Long, User> users;
//    private static long id;
//
//    public InMemoryUserStorage() {
//        id = 0;
//        users = new HashMap<>();
//    }
//
//    private long nextId() {
//        return ++id;
//    }
//
//    @Override
//    public Collection<User> getAll() {
//        log.info("Количество пользователей: {}", users.size());
//        return users.values();
//    }
//
//    @Override
//    public User create(User user) throws ValidationException {
//        if (users.containsKey(user.getId())) {
//            throw new ValidationException("Пользователь с электронной почтой " +
//                    user.getEmail() + " уже зарегистрирован.");
//        } else {
//            user.setId(nextId());
//            users.put(user.getId(), user);
//            log.info("Пользователь с адресом электронной почты {} создан", user.getEmail());
//        }
//        return user;
//    }
//
//    @Override
//    public User update(User user) {
//        users.put(user.getId(), user);
//        log.info("Пользователь с адресом электронной почты {} обновлен", user.getEmail());
//        return user;
//    }
//
//    @Override
//    public User getById(long userId) {
//        return users.get(userId);
//    }
//
//    @Override
//    public boolean contains(long id) {
//        return users.containsKey(id);
//    }
//}
