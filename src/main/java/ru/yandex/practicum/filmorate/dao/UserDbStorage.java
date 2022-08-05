package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> getAll() throws SQLException {
        final String sqlQuery = "select USER_ID, USER_NAME, EMAIL, LOGIN, BIRTHDAY " +
                "from USERS";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery);
        final Collection<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = makeUser(rs);
            users.add(user);
        }
        return users;
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
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update USERS set " +
                "USER_NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ?";
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
        return user;
    }

    @Override
    public User getById(long id) throws SQLException {
        final String sqlQuery = "select USER_ID, LOGIN, BIRTHDAY, USER_NAME " +
                "from USERS " +
                "where USER_ID = ?";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery);
        return makeUser(rs);
    }

    @Override
    public boolean contains(long id) throws SQLException {
        final String sqlQuery = "select USER_ID, LOGIN, BIRTHDAY, USER_NAME " +
                "from USERS " +
                "where USER_ID = ?";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery);
        if (makeUser(rs) != null) {
            return true;
        }
        return false;
    }

    private static User makeUser(ResultSet rs) throws SQLException {
        return new User(rs.getLong("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("USER_NAME"),
                rs.getDate("BIRTHDAY").toLocalDate(),
                (Set<User>) rs.getObject("FRIEND_ID")
        );
    }
}
