package jp.gihyo.projava.jibc007.controller;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommentService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(Comment comment) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(comment);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("comments");

        insert.execute(param);
    }

    public List<Comment> findAll() {
        String query = "SELECT * FROM comments";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        return result.stream()
                .map(row -> new Comment(
                        row.get("book_id").toString(),
                        row.get("commenterName").toString(),
                        row.get("commentContent").toString()
                ))
                .toList();
    }

    public static record Comment(String bookId, String commenterName, String commentContent) {
    }
}
