package com.scheduler.scheduler.LV3.repository;

import com.scheduler.scheduler.LV3.entity.Schedule;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository{

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Schedule> scheduleRowMapper = (rs, rowNum) -> mapRowToSchedule(rs);

    @Override
    public Schedule save(Schedule schedule) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("schedule")
                .usingGeneratedKeyColumns("schedule_id");

        Map<String, Object> data = new HashMap<>();
        data.put("todo", schedule.getTodo());
        data.put("author", schedule.getAuthor());
        data.put("password", schedule.getPassword());
        data.put("created_at", Date.valueOf(schedule.getCreatedAt()));
        data.put("updated_at", Date.valueOf(schedule.getUpdatedAt()));

        Number key = insert.executeAndReturnKey(new MapSqlParameterSource(data));

        return Schedule.builder()
                .scheduleId(key.longValue())
                .todo(schedule.getTodo())
                .author(schedule.getAuthor())
                .password(schedule.getPassword())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }

    @Override
    public Schedule findById(Long id) {
        String query = "SELECT * FROM schedule WHERE id = ?";
        return jdbcTemplate.queryForObject(query, scheduleRowMapper, id);
    }

    @Override
    public List<Schedule> findSchedulesByAuthorAndUpdatedAt(String author, LocalDate updatedAt) {
        StringBuilder sql = new StringBuilder("SELECT * FROM schedule WHERE 1=1");
        List<Object> data = new ArrayList<>();

        if (author != null && !author.isEmpty()) {
            sql.append(" AND author = ?");
            data.add(author);
        }

        if (updatedAt != null) {
            sql.append(" AND updated_at = ?");
            data.add(Date.valueOf(updatedAt));
        }

        sql.append(" ORDER BY updated_at DESC");

        return jdbcTemplate.query(sql.toString(), scheduleRowMapper, data.toArray());
    }

    @Override
    public Schedule update(Long scheduleId, Schedule schedule) {
        String sql = "UPDATE schedule SET todo = ?, author = ?, password = ?, updated_at = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                schedule.getTodo(),
                schedule.getAuthor(),
                schedule.getPassword(),
                Date.valueOf(schedule.getUpdatedAt()),
                scheduleId
        );

        return Schedule.builder()
                .scheduleId(scheduleId)
                .todo(schedule.getTodo())
                .author(schedule.getAuthor())
                .password(schedule.getPassword())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }

    @Override
    public void delete(Long scheduleId) {
        String sql = "DELETE FROM schedule WHERE id = ?";
        jdbcTemplate.update(sql, scheduleId);
    }

    private static Schedule mapRowToSchedule(ResultSet rs) throws SQLException {
        return Schedule.builder()
                .scheduleId(rs.getLong("id"))
                .todo(rs.getString("todo"))
                .author(rs.getString("author"))
                .password(rs.getString("password"))
                .createdAt(rs.getDate("created_at").toLocalDate())
                .updatedAt(rs.getDate("updated_at").toLocalDate())
                .build();
    }
}
