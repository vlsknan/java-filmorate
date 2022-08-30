package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.interf.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review create(Review review) {
        String sqlQuery = "INSERT INTO REVIEWS (CONTENT, IS_POSITIVE, USER_ID, FILM_ID) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"REVIEW_ID"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setLong(3, review.getUserId());
            stmt.setLong(4, review.getFilmId());
            return stmt;
        }, keyHolder);
        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return review;
    }

    @Override
    public Optional<Review> update(Review review) {
        final String sqlQuery = "UPDATE REVIEWS " +
                "SET CONTENT = ?, IS_POSITIVE = ? " +
                "WHERE REVIEW_ID = ?";
        return jdbcTemplate.update(sqlQuery, review.getContent(), review.getIsPositive(), review.getReviewId()) == 0 ?
                Optional.empty() :
                Optional.of(review);
    }

    @Override
    public void delete(long id) {
        final String sqlQuery = "DELETE FROM REVIEWS " +
                "WHERE REVIEW_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public Optional<Review> getById(long id) {
        final String sqlQuery = "SELECT * FROM REVIEWS " +
                "WHERE REVIEW_ID = ?";
        List<Review> reviews = jdbcTemplate.query(sqlQuery, this::makeReview, id);
        return reviews.size() == 0 ?
                Optional.empty() :
                Optional.ofNullable(reviews.get(0));
    }

    @Override
    public Collection<Review> getAll() {
        final String sqlQuery = "SELECT * FROM REVIEWS";
        List<Review> reviews = jdbcTemplate.query(sqlQuery, this::makeReview);
        return sortReviewsByUseful(reviews);
    }

    public List<Review> getReviewsByFilmId(long id, int count) {
        final String sqlQuery = "SELECT * FROM REVIEWS " +
                "WHERE FILM_ID = ? " +
                "LIMIT ?";
        List<Review> reviews = jdbcTemplate.query(sqlQuery, this::makeReview, id, count);
        return sortReviewsByUseful(reviews);
    }

    public void addLikeToReview(long reviewId, long userId) {
        deleteDislikeToReview(reviewId, userId);
        final String sqlQuery = "INSERT INTO REVIEW_LIKES (REVIEW_ID, USER_ID) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, reviewId, userId);
    }

    public void addDislikeToReview(long reviewId, long userId) {
        deleteLikeToReview(reviewId, userId);
        final String sqlQuery = "INSERT INTO REVIEW_DISLIKES (REVIEW_ID, USER_ID) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, reviewId, userId);
    }

    public void deleteLikeToReview(long reviewId, long userId) {
        final String sqlQuery = "DELETE FROM REVIEW_LIKES " +
                "WHERE REVIEW_ID = ? " +
                "AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, reviewId, userId);
    }

    public void deleteDislikeToReview(long reviewId, long userId) {
        final String sqlQuery = "DELETE FROM REVIEW_DISLIKES " +
                "WHERE REVIEW_ID = ? " +
                "AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, reviewId, userId);
    }

    public List<Long> getLikesByReviewId(long reviewId) {
        final String sqlQuery = "SELECT USER_ID FROM REVIEW_LIKES " +
                "WHERE REVIEW_ID = ?";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, reviewId);
    }

    public List<Long> getDislikesByReviewId(long reviewId) {
        final String sqlQuery = "SELECT USER_ID FROM REVIEW_DISLIKES " +
                "WHERE REVIEW_ID = ?";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, reviewId);
    }

    private Review makeReview(ResultSet rs, int rowNum) throws SQLException {
        return new Review(
                rs.getLong("REVIEW_ID"),
                rs.getString("CONTENT"),
                rs.getBoolean("IS_POSITIVE"),
                rs.getLong("USER_ID"),
                rs.getLong("FILM_ID"),
                calculateUseful(rs.getLong("REVIEW_ID")));
    }

    private Integer calculateUseful(long reviewId) {
        int likesCount = getLikesByReviewId(reviewId).size();
        int dislikesCount = getDislikesByReviewId(reviewId).size();
        return likesCount - dislikesCount;
    }

    private List<Review> sortReviewsByUseful(List<Review> reviews) {
        return reviews.stream()
                .sorted(Comparator.comparingInt(Review::getUseful).reversed())
                .collect(Collectors.toList());
    }
}
