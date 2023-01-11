package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    long id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
    Mpa mpa;
    Set<Genre> genres;
    List<Director> directors;
}
