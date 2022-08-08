package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class MpaDbStorage {
    private JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //получить рейтинг
    public Collection<Mpa> getAllMpa() throws SQLException {
        final String sqlQuery = "select * from MPA";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery);
        final Collection<Mpa> listMpa = new ArrayList<>();
        while (rs.next()) {
            Mpa mpa = new Mpa(rs.getLong("MPA_ID"),
                    rs.getString("MPA_NAME"));
            listMpa.add(mpa);
        }
        return listMpa;
    }

    //получить рейтинг по id
    public Mpa getMpaById(long id) throws SQLException {
        final String sqlQuery = "select * from MPA where MPA_ID = ?";
        ResultSet rs = (ResultSet) jdbcTemplate.queryForList(sqlQuery, id);
        return new Mpa(rs.getLong("MPA_ID"),
                rs.getString("MPA_NAME"));
    }
}
