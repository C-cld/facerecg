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
	
	/**
	 * 根据人脸库中的id查找数据库中的人
	 * @param userId
	 * @return
	 */
	public User findUserById(final String userId) {
		String sql = " select * from user where user_id = ? ";
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
	
	/**
	 * 注册人入数据库
	 * @param user
	 */
	public void registerUser(User user, String id) {
		String sql = " insert into user(user_id,username,age,sex) values (?,?,?,?) ";
		jdbcTemplate.update(sql, new Object[] {id, user.getUsername(), user.getAge(), user.getSex()});
	}
	
	/**
	 * 获取最大id，用于人脸库id
	 * @return
	 */
	public int getMaxId() {
		String sql = "select max(user_id) from user ";
		int maxId = jdbcTemplate.queryForObject(sql, Integer.class);
		return maxId;
	}
}
