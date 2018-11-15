package com.cyy.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cyy.domain.User;
import com.cyy.service.DetectService;
import com.cyy.service.RegisterService;


@RestController
public class MainController {
	private DetectService detectService;
	private RegisterService registerService;

	@Autowired
	public void setDetectService(DetectService detectService) {
		this.detectService = detectService;
	}
	
	@Autowired
	public void setRegisterService(RegisterService registerService) {
		this.registerService = registerService;
	}

	@RequestMapping(value = "/")
	public ModelAndView indexPage() {
		return new ModelAndView("index");
	}

	@RequestMapping(value = "faceDetect")
	public ModelAndView faceDetectPage() {
		return new ModelAndView("faceDetect");
	}
	
	@RequestMapping(value = "faceRegister")
	public ModelAndView faceRegister() {
		return new ModelAndView("faceRegister", "errorMsg", "请保持最帅。");
	}

	/**
	 * @author caiyunye
	 * @Date 2018/10/24
	 * 在人脸库中和数据库中搜索人脸
	 */
	@RequestMapping(value = "faceDetect/detect",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> faceDetect(HttpServletRequest request) {
		String base64Img = request.getParameter("imgValue");
		User user = detectService.findUser(base64Img);
		if (user != null) {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("user", user);
			return map;
		} else {
			return null;
		}
	}
	
	/**
	 * @author caiyunye
	 * @Data 2018/10/25
	 * 注册人脸至人脸库
	 */
	@RequestMapping(value = "faceRegister/register")
	public ModelAndView faceRegister(HttpServletRequest request, User user) {
		String base64Img = request.getParameter("imgValue");
		Boolean flag = registerService.register(base64Img, user);
		if (flag) {
			return new ModelAndView("faceRegister","errorMsg","注册成功。");
		} else {
			return new ModelAndView("faceRegister","errorMsg","注册失败。");
		}
	}
}
