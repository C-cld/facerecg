package com.claridy.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.claridy.domain.User;

@Repository
public class UserDao {
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
	
	public User findUserById(final String userId) {
		String sql = "select * from user where user_id = ? ";
		final User user = new User();
		jdbcTemplate.query(sql, new Object[]{userId},
                new RowCallbackHandler() {
                    public void processRow(ResultSet rs) throws SQLException {
                        user.setUserId(rs.getString("user_id"));
                        user.setUsername(rs.getString("username"));
                        user.setAge(rs.getInt("age"));
                        user.setSex(rs.getInt("sex"));
                    }
                });
		return user;
	}
}
