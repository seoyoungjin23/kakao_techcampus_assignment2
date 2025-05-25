package com.scheduler.scheduler.LV3.repository;

import com.scheduler.scheduler.LV3.entity.User;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> mapRowToUser(rs);

    @Override
    public User save(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> data = new HashMap<>();
        data.put("name", user.getName());
        data.put("email", user.getEmail());
        data.put("created_at", Date.valueOf(user.getCreatedAt()));
        data.put("updated_at", Date.valueOf(user.getUpdatedAt()));

        Number key = insert.executeAndReturnKey(new MapSqlParameterSource(data));

        return User.builder()
                .Id(key.longValue())
                .email(user.getEmail())
                .name(user.getName())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM user WHERE id = ?";

        User user = jdbcTemplate.query(sql, userRowMapper, id).stream().findFirst().orElse(null);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";

        User user = jdbcTemplate.query(sql, userRowMapper, email).stream().findFirst().orElse(null);
        return Optional.ofNullable(user);
    }
    private User mapRowToUser(ResultSet rs) throws SQLException {
        return User.builder()
                .Id(rs.getLong("id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .createdAt(rs.getDate("created_at").toLocalDate())
                .updatedAt(rs.getDate("updated_at").toLocalDate())
                .build();
    }

    @Override
    public User updateName(Long userId, String newName, User user) {
        String sql = "UPDATE user SET name = ? WHERE id = ?";

        jdbcTemplate.update(sql, newName, userId);

        return User.builder()
                .Id(userId)
                .name(newName)
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
