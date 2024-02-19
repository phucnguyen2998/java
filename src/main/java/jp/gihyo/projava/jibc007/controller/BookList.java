// File: BookList.java
package jp.gihyo.projava.jibc007.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookList {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    BookList(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(BookItem bookItem) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(bookItem);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("booklist");

        insert.execute(param);
    }

    public List<BookItem> findAll() {
        String query = "SELECT * FROM booklist";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        return result.stream()
                .map(row -> new BookItem(
                        row.get("id").toString(),
                        row.get("name").toString(),
                        row.get("image").toString()
                ))
                .toList();
    }

    public static record BookItem(String id, String name, String image) {
    }
}
