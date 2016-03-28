package com.onlinemarketing.json;

import java.net.URLEncoder;

import org.json.JSONObject;

import com.lib.Debug;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.object.LoginRegister;
import com.onlinemarketing.util.Util;
import com.smile.android.gsm.utils.AndroidUtils;

public class JsonAccount {

	JSONObject jsonObject;
	StringBuilder request;

	public LoginRegister paserRegister(String email, String pass, String device_id, String user_id, String session_id,
			int indext) {
		LoginRegister obj = new LoginRegister();
		// check email password
		if (Util.isNotNull(email) && Util.isNotNull(pass)) {
			if (Util.validate(email)) {
				try {
					if (indext == Constan.getIntProperty("statusRegister")) {
						request = new StringBuilder(SystemConfig.API + SystemConfig.Register);
					} else if (indext == Constan.getIntProperty("statusLogin")) {
						request = new StringBuilder(SystemConfig.API + SystemConfig.Login);
					}
					request.append("email=").append(email);
					request.append("&password=").append(URLEncoder.encode(pass, "UTF-8"));
					request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));

					request.append("&user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
					request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
					Debug.e("link: " + request.toString());
					String str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
					Debug.e("Str: " + str);
					jsonObject = new JSONObject(str);
					obj.setCode(Integer.parseInt(jsonObject.getString("code")));
					obj.setMessage(jsonObject.getString("message"));
					if (obj.getCode() == Constan.getIntProperty("success")) {
						obj.setUser_Id(Integer.parseInt(jsonObject.getString("user_id")));
						obj.setSession_id(jsonObject.getString("session_id"));
					}

				} catch (Exception e) {
					Debug.e(e.toString());
				}
			} else {
				obj.setCode(Constan.getIntProperty("loginerror"));
				obj.setMessage(Constan.getProperty("Error02"));
			}
		} else {
			obj.setCode(Constan.getIntProperty("loginerror"));
			obj.setMessage(Constan.getProperty("Error01"));
		}

		return obj;

	}

	public LoginRegister paserLoginFacebook(String device_id, String user_id, String session_id, String facebook_id,
			String google_id, String user_name) {
		LoginRegister obj = new LoginRegister();
		try {
			request = new StringBuilder(SystemConfig.API + SystemConfig.LoginSocial);
			request.append("device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			request.append("&user_id=");
			request.append("&session_id=");
			request.append("&facebook_id=").append(URLEncoder.encode(facebook_id, "UTF-8"));
			request.append("&google_id=").append(URLEncoder.encode(google_id, "UTF-8"));
			request.append("&username=").append(URLEncoder.encode(user_name, "UTF-8"));
			String str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
			Debug.e("Str: " + str);
			jsonObject = new JSONObject(str);
			obj.setCode(Integer.parseInt(jsonObject.getString("code")));
			obj.setMessage(jsonObject.getString("message"));
			if (obj.getCode() == Constan.getIntProperty("success")) {
				obj.setUser_Id(Integer.parseInt(jsonObject.getString("user_id")));
				obj.setSession_id(jsonObject.getString("session_id"));
			}

		} catch (Exception e) {
			Debug.e(e.toString());
		}
		return obj;
	}
}
