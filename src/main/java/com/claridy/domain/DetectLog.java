package com.claridy.domain;

import java.util.Date;

public class DetectLog {
	private int logId;
	private int userId;
	private Date detectDate;

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getDetectDate() {
		return detectDate;
	}

	public void setDetectDate(Date detectDate) {
		this.detectDate = detectDate;
	}

}
