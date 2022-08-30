package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ProblemLikesException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.dao.ReviewDbStorage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService implements GeneralService<Review> {
    private final ReviewDbStorage reviewDbStorage;
    private final UserService userService;
    private final FilmService filmService;
    //private final EventStorageDao eventStorageDao;

    @Override
    public Collection<Review> getAll() {
        return reviewDbStorage.getAll();
    }

    @Override
    public Review create(Review review) throws SQLException {
        userService.getById(review.getUserId());
        filmService.getById(review.getFilmId());
        Review createdReview = reviewDbStorage.create(review);
        //eventStorageDao.addReviewEvent(createdReview);
        return createdReview;
    }

    @Override
    public Review update(Review review) throws SQLException {
        userService.getById(review.getUserId());
        filmService.getById(review.getFilmId());
        getById(review.getReviewId());
        Review updatedReview = reviewDbStorage.update(review).get();
        //eventStorageDao.updateReviewEvent(updatedReview);
        return updatedReview;
    }

    @Override
    public void delete(long id) {
        Review deletedReview = getById(id);
        reviewDbStorage.delete(id);
        //eventStorageDao.deleteReviewEvent(deletedReview);
    }

    @Override
    public Review getById(long id) {
        return reviewDbStorage.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Отзыв с id = %s не найден", id)));
    }

    public List<Review> getReviewsByFilmIdOrAll(Long filmId, int count) throws SQLException {
        if (filmId == null) {
            return new ArrayList<>(reviewDbStorage.getAll());
        }
        filmService.getById(filmId);
        return reviewDbStorage.getReviewsByFilmId(filmId, count);
    }

    public void addLikeToReview(long reviewId, long userId) throws SQLException {
        getById(reviewId);
        userService.getById(userId);
        if (reviewDbStorage.getLikesByReviewId(reviewId).contains(userId)) {
            throw new ProblemLikesException("Пользователь не может поставить несколько лайков одному отзыву.");
        }
        reviewDbStorage.addLikeToReview(reviewId, userId);
    }

    public void addDislikeToReview(long reviewId, long userId) throws SQLException {
        getById(reviewId);
        userService.getById(userId);
        if (reviewDbStorage.getDislikesByReviewId(reviewId).contains(userId)) {
            throw new ProblemLikesException("Пользователь не может поставить несколько дизлайков одному отзыву.");
        }
        reviewDbStorage.addDislikeToReview(reviewId, userId);
    }

    public void deleteLikeToReview(long reviewId, long userId) throws SQLException {
        getById(reviewId);
        userService.getById(userId);
        if (!reviewDbStorage.getLikesByReviewId(reviewId).contains(userId)) {
            throw new ProblemLikesException("Пользователь не может убрать лайк, который ранее не ставил.");
        }
        reviewDbStorage.deleteLikeToReview(reviewId, userId);
    }

    public void deleteDislikeToReview(long reviewId, long userId) throws SQLException {
        getById(reviewId);
        userService.getById(userId);
        if (!reviewDbStorage.getDislikesByReviewId(reviewId).contains(userId)) {
            throw new ProblemLikesException("Пользователь не может убрать дизлайк, который ранее не ставил.");
        }
        reviewDbStorage.deleteDislikeToReview(reviewId, userId);
    }

    @Override
    public void validate(Review review) {
        if (!userService.getAll().contains(review.getUserId())) {
            throw new ValidationException("id пользователя не существует");
        }
    }
}
