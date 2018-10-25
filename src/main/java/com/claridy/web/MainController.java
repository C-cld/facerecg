package com.claridy.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.claridy.domain.User;
import com.claridy.service.DetectService;

@RestController
public class MainController {
	private DetectService detectService;

	@Autowired
	public void setDetectService(DetectService detectService) {
		this.detectService = detectService;
	}

	@RequestMapping(value = "/facerecg")
	public ModelAndView indexPage() {
		return new ModelAndView("index");
	}

	@RequestMapping(value = "/facerecg/faceDetect")
	public ModelAndView faceDetectPage() {
		return new ModelAndView("faceDetect");
	}

	/**
	 * @author caiyunye
	 * @Date 2018/10/24
	 * 在人脸库中和数据库中搜索人脸
	 */
	@RequestMapping(value = "/facerecg/detect",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> faceDetect(HttpServletRequest request) {
		String data = request.getParameter("imgValue");
		User user = detectService.findUser(data);
		if (user != null) {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("user", user);
			return map;
		} else {
			return null;
		}
	}
	
	
}
