package com.cyy.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;

public class AuthUtils {
	private static String authDate;
	private static String access_token;

	/**
	 * 每隔30天获取一次access_token, 这里的写法将来要优化
	 * @return
	 */
	public static String getAuth() {
		try {
			if (authDate != null) {
				DateFormat df = new SimpleDateFormat("EEE,dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
				Date authDay = df.parse(authDate);
				Date today = new Date();
				//当前日期超过token获取日期30天
				if (today.getTime() - authDay.getTime() >= 2592000) {
					String clientId = "lK9XD2kZ28L0EvzRgtauOECM";
					String clientSecret = "XdFc1WRX0DQRvIyuorj5TQB0CPtINgsG";
					String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
					String getAccessTokenUrl = authHost + "grant_type=client_credentials" + "&client_id=" + clientId+ "&client_secret=" + clientSecret;

					URL realUrl = new URL(getAccessTokenUrl);
					// 打开连接
					HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
					connection.setRequestMethod("GET");
					connection.connect();
					// 获取并响应头
					Map<String, List<String>> map = connection.getHeaderFields();
					for (String key : map.keySet()) {
						System.out.println(key + "---->" + map.get(key));
					}
					authDate = map.get("Date").get(0);
					// 定义BufferedReader输入流来读取URL的响应
					BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					String result = "";
					String line;
					while ((line = in.readLine()) != null) {
						result += line;
					}
					// 返回结果
					System.out.println(result);
					JSONObject jsonObject = new JSONObject(result);
					access_token = jsonObject.getString("access_token");
					return access_token;
				} else {
					return access_token;
				}
			} else {
				String clientId = "lK9XD2kZ28L0EvzRgtauOECM";
				String clientSecret = "XdFc1WRX0DQRvIyuorj5TQB0CPtINgsG";
				String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
				String getAccessTokenUrl = authHost + "grant_type=client_credentials" + "&client_id=" + clientId+ "&client_secret=" + clientSecret;

				URL realUrl = new URL(getAccessTokenUrl);
				// 打开连接
				HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
				// 获取并响应头
				Map<String, List<String>> map = connection.getHeaderFields();
				for (String key : map.keySet()) {
					System.out.println(key + "---->" + map.get(key));
				}
				authDate = map.get("Date").get(0);
				// 定义BufferedReader输入流来读取URL的响应
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String result = "";
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
				// 返回结果
				System.out.println(result);
				JSONObject jsonObject = new JSONObject(result);
				access_token = jsonObject.getString("access_token");
				return access_token;
			}
		} catch (Exception e) {
			System.err.printf("获取token失败！");
			e.printStackTrace(System.err);
			return null;
		}
	}
}
