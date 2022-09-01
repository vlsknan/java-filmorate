package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Component
public class EventDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public EventDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void addFriendEvent(long id, long friendId) {
        Event event = getBaseEvent(id, friendId);
        event.setEventType(EventType.FRIEND);
        event.setOperation(EventOperation.ADD);
        add(event);
    }

    public void deleteFriendEvent(long id, long friendId){
        Event event = getBaseEvent(id, friendId);
        event.setEventType(EventType.FRIEND);
        event.setOperation(EventOperation.REMOVE);
        add(event);
    }

    public void addLikeEvent(long filmId, long userId) {
        Event event = getBaseEvent(userId, filmId);
        event.setEventType(EventType.LIKE);
        event.setOperation(EventOperation.ADD);
        add(event);
    }

    public void deleteLikeEvent(long filmId, long userId) {
        Event event = getBaseEvent(userId, filmId);
        event.setEventType(EventType.LIKE);
        event.setOperation(EventOperation.REMOVE);
        add(event);
    }

    public void addReviewEvent(Review review) {
        Event event = getBaseEvent(review.getUserId(), review.getReviewId());
        event.setEventType(EventType.REVIEW);
        event.setOperation(EventOperation.ADD);
        add(event);
    }

    public void updateReviewEvent(Review review) {
        Event event = getBaseEvent(review.getUserId(), review.getReviewId());
        event.setEventType(EventType.REVIEW);
        event.setOperation(EventOperation.UPDATE);
        add(event);
    }

    public void deleteReviewEvent(Review review) {
        Event event = getBaseEvent(review.getUserId(), review.getReviewId());
        event.setEventType(EventType.REVIEW);
        event.setOperation(EventOperation.REMOVE);
        add(event);
    }

    public List<Event> getFeed(long userId) {
        String sqlQuery = "SELECT EVENT_ID, TIMESTAMP, USER_ID, EVENT_TYPE, OPERATION, ENTITY_ID " +
                "FROM EVENTS " +
                "WHERE USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::makeEvent, userId);
    }

    private void add(Event event) {
        String sql = "INSERT INTO EVENTS (TIMESTAMP, USER_ID, EVENT_TYPE, OPERATION, ENTITY_ID) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(c -> {
            PreparedStatement ps = c.prepareStatement(sql, new String[]{"EVENT_ID"});
            ps.setLong(1, event.getTimestamp());
            ps.setLong(2, event.getUserId());
            ps.setString(3, event.getEventType().toString());
            ps.setString(4, event.getOperation().toString());
            ps.setLong(5, event.getEntityId());
            return ps;
        }, keyHolder);
        event.setEventId(keyHolder.getKey().longValue());
    }

    private Event getBaseEvent(long userId, long entityId) {
        Event event = new Event();
        event.setTimestamp(Instant.now().toEpochMilli());
        event.setUserId(userId);
        event.setEntityId(entityId);
        return event;
    }

    private Event makeEvent(ResultSet rs, int num) throws SQLException {
        return new Event(rs.getLong("EVENT_ID"),
                rs.getLong("TIMESTAMP"),
                rs.getLong("USER_ID"),
                EventType.valueOf(rs.getString("EVENT_TYPE")),
                EventOperation.valueOf(rs.getString("OPERATION")),
                rs.getLong("ENTITY_ID"));
    }
}
