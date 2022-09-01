package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    //создать отзыв
    @PostMapping
    public Review createReview(@Valid @RequestBody Review review) throws SQLException {
        log.info("POST create review");
        return reviewService.create(review);
    }

    //обновить отзыв
    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) throws SQLException {
        log.info("PUT update review");
        return reviewService.update(review);
    }

    //удалить отзыв
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable long id) {
        log.info("DELETE delete review");
        reviewService.delete(id);
        return ResponseEntity.ok().build();
    }

    //получить отзыв по id
    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable long id) {
        log.info("GET review by id");
        return reviewService.getById(id);
    }

    //получить отзывы к фильму
    @GetMapping
    public List<Review> getReviewsByFilmIdOrAll(@RequestParam(required = false) Long filmId,
                                                @RequestParam(defaultValue = "10") int count) throws SQLException {
        log.info("GET reviews by filmId");
        return reviewService.getReviewsByFilmIdOrAll(filmId, count);
    }

    //добавить лайк отзыву
    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<HttpStatus> addLikeToReview(@PathVariable long id,
                                                      @PathVariable long userId) throws SQLException {
        reviewService.addLikeToReview(id, userId);
        log.info("PUT user add like review", userId, id);
        return ResponseEntity.ok().build();
    }

    //добавить дизлайк отзыву
    @PutMapping("/{id}/dislike/{userId}")
    public ResponseEntity<HttpStatus> addDislikeToReview(@PathVariable long id,
                                                         @PathVariable long userId) throws SQLException {
        reviewService.addDislikeToReview(id, userId);
        log.info("PUT user add dislike review");
        return ResponseEntity.ok().build();
    }

    //удалить лайк у отзыва
    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<HttpStatus> deleteLikeToReview(@PathVariable long id,
                                                         @PathVariable long userId) throws SQLException {
        reviewService.deleteLikeToReview(id, userId);
        log.info("DELETE user delete like review");
        return ResponseEntity.ok().build();
    }

    //удалить дизлайк у отзыва
    @DeleteMapping("/{id}/dislike/{userId}")
    public ResponseEntity<HttpStatus> deleteDislikeToReview(@PathVariable long id,
                                                            @PathVariable long userId) throws SQLException {
        reviewService.deleteDislikeToReview(id, userId);
        log.info("DELETE user delete dislike review");
        return ResponseEntity.ok().build();
    }
}
