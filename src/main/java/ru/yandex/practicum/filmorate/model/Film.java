package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(of = "id")
@SuperBuilder
public class Film {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Mpa mpa;
    private List<Genre> genres;
}
