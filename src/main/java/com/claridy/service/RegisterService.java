package com.claridy.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.UserDao;
import com.claridy.domain.User;
import com.claridy.utils.AuthUtils;
import com.claridy.utils.GsonUtils;
import com.claridy.utils.HttpUtil;

@Service
public class RegisterService {
	private UserDao userDao;
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	/**
	 * 注册人脸
	 * @param base64Img
	 * @param user
	 */
	public Boolean register(String base64Img, User user) {
		//查找数据库中的最大id
		int maxId = userDao.getMaxId();
		Boolean flag = registerFace(base64Img, maxId+1);
		if (flag) {
			//往数据库中注册人脸
			userDao.registerUser(user, maxId+1);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 注册人脸入人脸库
	 * @param base64Img
	 */
	public Boolean registerFace(String base64Img,int user_id) {
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", base64Img);
            map.put("group_id", "test");
            map.put("user_id", user_id);
            map.put("liveness_control", "NORMAL");
            map.put("image_type", "BASE64");
            
            String param = GsonUtils.toJson(map);
            String accessToken = AuthUtils.getAuth();
            String result = HttpUtil.post(url, accessToken, "application/json", param);
            
            System.out.println("======" + result);
            JSONObject jsonObject = new JSONObject(result);
            JSONObject r_result = jsonObject.getJSONObject("result");
            if (r_result != null) {
            	return true;
            } else {
            	return false;
            }
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
