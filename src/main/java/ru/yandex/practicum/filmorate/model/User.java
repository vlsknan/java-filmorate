package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    @JsonIgnore
    @ToString.Exclude
    private Set<User> friends = new HashSet<>();

    public void addInFriends(User friend) {
        friends.add(friend);
        friend.friends.add(this);
    }
//
//    public void removeFriend(User friend) {
//        friends.remove(friend);
//        friend.friends.remove(this);
//    }
}
