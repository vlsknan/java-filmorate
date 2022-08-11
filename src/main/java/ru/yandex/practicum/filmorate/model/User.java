package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
//    @JsonIgnore
//    private Set<User> friends = new HashSet<>();
}
