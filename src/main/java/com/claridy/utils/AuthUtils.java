package com.claridy.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class AuthUtils {
	public static String getAuth() {
		String clientId = "lK9XD2kZ28L0EvzRgtauOECM";
		String clientSecret = "XdFc1WRX0DQRvIyuorj5TQB0CPtINgsG";
		String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
		String getAccessTokenUrl = authHost + "grant_type=client_credentials" + "&client_id=" + clientId + "&client_secret=" + clientSecret;
		
		try {
			URL realUrl = new URL(getAccessTokenUrl);
			//打开连接
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			//获取并响应头
			Map<String, List<String>> map = connection.getHeaderFields();
			for (String key : map.keySet()) {
				System.out.println(key + "---->" + map.get(key));
			}
			//定义BufferedReader输入流来读取URL的响应
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String result = "";
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			//返回结果
			System.out.println(result);
			JSONObject jsonObject = new JSONObject(result);
			String access_token = jsonObject.getString("access_token");
			return access_token;
		} catch (Exception e) {
			System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
		}
		return null;
	}
}
