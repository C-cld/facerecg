package com.claridy.domain;

import java.util.Date;

public class DetectLog {
	private int logId;
	private String userId;
	private Date detectDate;

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getDetectDate() {
		return detectDate;
	}

	public void setDetectDate(Date detectDate) {
		this.detectDate = detectDate;
	}
}
