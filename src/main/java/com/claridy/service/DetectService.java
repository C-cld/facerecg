package com.claridy.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.claridy.dao.UserDao;
import com.claridy.domain.User;
import com.claridy.utils.AuthUtils;
import com.claridy.utils.GsonUtils;
import com.claridy.utils.HttpUtil;

@Service
public class DetectService {
	
	private UserDao userDao;
	
	@Autowired
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	/**
	 * 检测人脸详细属性，目前只返回颜值
	 */
	public String detectFaceDetail(String base64Img) {
		String url = "https://aip.baidubce.com/rest/2.0/face/v3/detect";
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", base64Img);
            map.put("face_field", "beauty");
            map.put("image_type", "BASE64");
            
            String param = GsonUtils.toJson(map);
            String accessToken = AuthUtils.getAuth();
            String result = HttpUtil.post(url, accessToken, "application/json", param);
            JSONObject jsonObject = new JSONObject(result);
            JSONObject r_result = jsonObject.getJSONObject("result");
            JSONArray ja = r_result.getJSONArray("face_list");
            String beauty = String.valueOf(ja.getJSONObject(0).getDouble("beauty"));
            return beauty;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 在人脸库中查找人脸
	 */
	public int findUserIdInFaceSet(String base64Img) {
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
            JSONObject r_result = jsonObject.getJSONObject("result");
            //匹配的用户列表
            JSONArray ja = r_result.getJSONArray("user_list");
            int userId = ja.getJSONObject(0).getInt("user_id");
            Double score = ja.getJSONObject(0).getDouble("score");
            //相似度大于80则认为匹配
            int retval = score.compareTo(80.0);
            if (retval >= 0) {
            	return userId;
            } else {
            	return 0;
            }
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public User findUser(String base64Img) {
		int userId = findUserIdInFaceSet(base64Img);
		String beauty = detectFaceDetail(base64Img);
		if (userId != 0 && beauty !=null) {
			User user = userDao.findUserById(userId);
			user.setBeauty(beauty);
			return user;
		} else if (userId == 0 && beauty != null) {//检测到人脸但是不在人脸库中
			User user = new User();
			user.setBeauty(beauty);
			return user;
		} else {
			return null;
		}
	}
}
