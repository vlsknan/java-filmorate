package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

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
    private Set<User> friends = new HashSet<>();

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
