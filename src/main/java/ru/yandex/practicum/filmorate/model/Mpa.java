package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@SuperBuilder
public class Mpa {
    private final int id;
    private final String name;
}
