package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Review {
    private Long reviewId;
    @NotBlank(message = "Текст отзыва не может быть пустым.")
    @NotNull(message = "Отсутствует текст отзыва.")
    private String content;
    @NotNull(message = "Отсутствует характеристика отзыва.")
    private Boolean isPositive;
    @NotNull(message = "Отсутствует ID пользователя.")
    private Long userId;
    @NotNull(message = "Отсутствует ID фильма.")
    private Long filmId;
    private int useful;
}