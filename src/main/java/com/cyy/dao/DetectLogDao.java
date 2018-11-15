package com.cyy.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cyy.domain.DetectLog;

@Repository
public class DetectLogDao {
private JdbcTemplate jdbcTemplate;
	
	@Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
	
	/**
	 * 扫描日志
	 * @param detectLog
	 */
	public void insertDetectLog(DetectLog detectLog) {
		String sql = " insert into detectlog(user_id,detect_date) values (?,?) ";
		jdbcTemplate.update(sql, new Object[] {detectLog.getUserId(), detectLog.getDetectDate()});
	}
}
