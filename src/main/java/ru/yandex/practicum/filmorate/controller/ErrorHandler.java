package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ProblemLikesException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    /* 400 — если ошибка валидации: ValidationException (BAD_REQUEST)
    404 — для всех ситуаций, если искомый объект не найден (NOT_FOUND)
    500 — если возникло исключение (Internal Server Error) */

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final ValidationException e) {
        log.info("400: {}", e.getMessage());
        return new ErrorResponse(String.format("Ошибка с полем \"%s\".", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.info("404: {}", e.getMessage());
        return new ErrorResponse(String.format("Ошибка с полем \"%s\".", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerErrorException(final Exception e) {
        log.info("500: {}", e.getMessage());
        return new ErrorResponse(String.format("Ошибка с полем \"%s\".", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleProblematicLikesException(final ProblemLikesException e) {
        log.info("409: {}", e.getMessage());
        return new ErrorResponse(String.format("Ошибка с полем \"%s\".", e.getMessage()));
    }
}
