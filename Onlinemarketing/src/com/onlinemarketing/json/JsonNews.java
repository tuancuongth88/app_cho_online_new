package com.onlinemarketing.json;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lib.Debug;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.PromotionVO;
import com.onlinemarketing.util.Util;

public class JsonNews {
	JSONObject jsonObject;
	StringBuilder request;

	public PromotionVO paserNews(String user_id, String session_id, String device_id,String link) {
		PromotionVO obj = new PromotionVO();
		String str = null;
		try {
			request = new StringBuilder(link);
//			request.append(SystemConfig.TextSetting+"/1");
//			request.append(link);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
			Debug.e("Str: " + str);
			jsonObject = new JSONObject(str);
			obj.setCode(jsonObject.getInt("code"));
			obj.setMessage(jsonObject.getString("message"));
			obj.setSession_id(jsonObject.getString("session_id"));
			obj.setUser_Id(jsonObject.getString("user_id"));
			JSONArray jsondata= jsonObject.getJSONArray("data");
			for (int i = 0; i < jsonObject.length(); i++) {
				JSONObject jsonobj = jsondata.getJSONObject(i);
				obj.setTitle(jsonobj.getString("title"));
				obj.setDescription(jsonobj.getString("description"));
			}
			
		} catch (Exception e) {
			Debug.e(e.toString());
		}
		return obj;
	}

}
