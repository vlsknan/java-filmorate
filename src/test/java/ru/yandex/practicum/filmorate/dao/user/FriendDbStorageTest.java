package ru.yandex.practicum.filmorate.dao.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FriendDbStorageTest {
    private final FriendDbStorage friendDbStorage;
    private final UserDbStorage userDbStorage;

    @Test
    void addGetDeleteFriendsTest() throws SQLException {
        User user = new User(1, "test@email","test_login", "test_name",
                LocalDate.of(2000,05,02));
        User friend = new User(2, "email@email","login", "name",
                LocalDate.of(1999,10,21));
        userDbStorage.create(user);
        userDbStorage.create(friend);

        friendDbStorage.addInFriend(user.getId(), friend.getId());
        assertEquals(1, friendDbStorage.getListFriends(user.getId()).size());

        friendDbStorage.deleteFromFriends(user.getId(), friend.getId());
        assertEquals(0, friendDbStorage.getListFriends(user.getId()).size());
    }

    @Test
    void getListCommonFriendsTest() throws SQLException {
        User user = new User(1, "test2321@email","test2_login", "test_name",
                LocalDate.of(2000,05,02));
        User friend = new User(2, "email2653@email","login2", "name",
                LocalDate.of(1999,10,21));
        User userCommon = new User(3, "comm12541@email","logUser", "userName",
                LocalDate.of(2010,01,10));
        userDbStorage.create(user);
        userDbStorage.create(friend);
        userDbStorage.create(userCommon);

        friendDbStorage.addInFriend(user.getId(), userCommon.getId());
        friendDbStorage.addInFriend(friend.getId(), userCommon.getId());
        assertEquals(1, friendDbStorage.getListCommonFriends(user.getId(), friend.getId()).size());
    }
}