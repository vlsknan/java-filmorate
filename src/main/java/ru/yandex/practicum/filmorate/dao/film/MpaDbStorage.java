package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class MpaDbStorage {
    private JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //получить рейтинг
    public Collection<Mpa> getAllMpa() {
        final String sqlQuery = "select * from MPA";
        return jdbcTemplate.query(sqlQuery, this::makeMpa);
    }

    //получить рейтинг по id
    public Optional<Mpa> getMpaById(long id) {
        final String sqlQuery = "select * from MPA where MPA_ID = ?";
        List<Mpa> result = jdbcTemplate.query(sqlQuery, this::makeMpa, id);
            return result.size() == 0 ?
                    Optional.empty() :
                    Optional.of(result.get(0));
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("MPA_ID"),
                rs.getString("MPA_NAME"));
    }
}
