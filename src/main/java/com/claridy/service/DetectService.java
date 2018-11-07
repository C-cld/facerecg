package com.claridy.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.DetectLogDao;
import com.claridy.dao.UserDao;
import com.claridy.domain.DetectLog;
import com.claridy.domain.User;
import com.claridy.utils.AuthUtils;
import com.claridy.utils.GsonUtils;
import com.claridy.utils.HttpUtil;

@Service
public class DetectService {
	
	private UserDao userDao;
	private DetectLogDao detectLogDao;
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Autowired
	public void setDetectLogDao(DetectLogDao detectLogDao) {
		this.detectLogDao = detectLogDao;
	}
	
	/**
	 * 在人脸库中查找人脸
	 */
	public String findUserIdInFaceSet(String base64Img) {
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/search";
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", base64Img);
            map.put("group_id_list", "test");
            map.put("liveness_control", "NORMAL");
            map.put("image_type", "BASE64");
            
            String param = GsonUtils.toJson(map);
            String accessToken = AuthUtils.getAuth();
            String result = HttpUtil.post(url, accessToken, "application/json", param);
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getInt("error_code") != 0) {//识别失败
            	return null;
            } else {
            	JSONObject r_result = jsonObject.getJSONObject("result");
                //匹配的用户列表
                JSONArray ja = r_result.getJSONArray("user_list");
                String userId = ja.getJSONObject(0).getString("user_id");
                //相似度大于80则认为匹配
                Double score = ja.getJSONObject(0).getDouble("score");
                int retval = score.compareTo(80.0);
                if (retval >= 0) {
                	return userId;
                } else {
                	return "0";
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public User findUser(String base64Img) {
		String userId = findUserIdInFaceSet(base64Img);
		//扫描记录放到log中
		insertDetectLog(userId);
		if (userId == null) { //没有检测到人脸
			return null;
		} else if (userId.equals("0")) { //检测到人脸但是不在人脸库
			User user = new User();
			return user;
		} else {
			User user = userDao.findUserById(userId);
			return user;
		}
	}
	
	/**
	 * 每扫描一次都添加日志记录
	 * @param userId
	 */
	public void insertDetectLog(String userId) {
        DetectLog detectLog = new DetectLog();
        if (userId != null) {
        	detectLog.setUserId(userId);
        }
        detectLog.setDetectDate(new Date());
        detectLogDao.insertDetectLog(detectLog);
	}
}
