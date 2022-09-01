package ru.yandex.practicum.filmorate.storage.dao.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interf.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getAll() {
        final String sqlQuery = "select USER_ID, USER_NAME, EMAIL, LOGIN, BIRTHDAY " +
                "from USERS";
        return jdbcTemplate.query(sqlQuery, this::makeUser);
    }

    @Override
    public User create(User user) {
        final String sqlQuery = "insert into USERS (USER_NAME, LOGIN, EMAIL, BIRTHDAY) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public Optional<User> update(User user) {
        String sqlQuery = "update USERS set USER_NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? " +
                "where USER_ID = ?";
        return  jdbcTemplate.update(sqlQuery, user.getName(),
                user.getLogin(), user.getEmail(), user.getBirthday(), user.getId()) == 0 ?
                Optional.empty() :
                Optional.of(user);
    }

    @Override
    public Optional<User> getById(long id) throws SQLException {
        final String sqlQuery = "select USER_ID, EMAIL, LOGIN, BIRTHDAY, USER_NAME " +
                "from USERS " +
                "where USER_ID = ?";
        List<User> res = jdbcTemplate.query(sqlQuery, this::makeUser, id);
        return res.size() == 0 ?
                Optional.empty() :
                Optional.of(res.get(0));
    }

    @Override
    public void delete(long id) {
        final String sql = "delete from users where user_id = ?";
        jdbcTemplate.update(sql, id);
    }

    private User makeUser(ResultSet rs, int ruwNum) throws SQLException {
        return new User(rs.getLong("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate()
        );
    }
}
