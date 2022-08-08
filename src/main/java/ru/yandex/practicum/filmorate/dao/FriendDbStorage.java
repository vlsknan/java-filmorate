package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FriendDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addInFriend(long userId, long friendId) {
        final String sqlQuery = "insert into USERS (FRIEND_ID) " +
                "values (?) where USER_ID = ?";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery, friendId, userId);
    }

    public void deleteFromFriends(long userId, long friendId) {
        final String sqlQuery = "delete from FRIENDS" +
                " where USER_ID = ?";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery, friendId, userId);
    }

    public Set<User> getListFriends(long id) throws SQLException {
        final String sqlQuery = "select FRIEND_ID from USERS " +
                "where USER_ID = ?";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery, id);
        final Set<User> listFriends = new HashSet<>();
        while (rs.next()) {
            User user = makeUser(rs);
            listFriends.add(user);
        }
        return listFriends;
    }

    public List<User> getListCommonFriends(long user1, long user2) throws SQLException {
        final String sqlQuery = "select F.FRIEND_ID from USERS as U " +
                "inner join FRIENDS as F on U.USER_ID = F.USER_ID " +
                "where USER_ID = ?";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery, user1);
        final List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = makeUser(rs);
            users.add(user);
        }
        return users;
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
