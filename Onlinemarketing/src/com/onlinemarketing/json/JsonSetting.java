package com.onlinemarketing.json;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lib.Debug;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.SettingVO;
import com.onlinemarketing.util.Util;

public class JsonSetting {
	JSONObject jsonObject;
	StringBuilder request;
	public OutputProduct paserSetting(String user_id, String session_id, String device_id) {
		OutputProduct obj = new OutputProduct();
		String str = null ;
		// check email password
		
				try {
					request = new StringBuilder(SystemConfig.API );
					request.append(SystemConfig.Setting);
					request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
					request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
					request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));

					str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
					Debug.e("Str: "+str);
					jsonObject = new JSONObject(str);
					obj.setCode(jsonObject.getInt("code"));					
					obj.setMessage(jsonObject.getString("message"));
					obj.setSession_id(jsonObject.getString("session_id"));
					JSONArray jsonProduct = jsonObject.getJSONArray("data");
					if (obj.getCode() == Constan.getIntProperty("success")) {
						ArrayList<SettingVO> arrSetting = new ArrayList<SettingVO>();
						for (int i = 0; i < jsonProduct.length(); i++) {
							JSONObject objjson_product = jsonProduct.getJSONObject(i);
							SettingVO objSetting = new SettingVO();
							objSetting.setId(objjson_product.getInt("id"));
							objSetting.setName(objjson_product.getString("name"));
							objSetting.setLink(objjson_product.getString("link").toString());
							objSetting.setMethod(objjson_product.get("method").toString());
							objSetting.setAvatar(objjson_product.get("image_url").toString());
							objSetting.setQuantily(objjson_product.get("quantity").toString());
							arrSetting.add(objSetting);
						}
						obj.setSettingVO(arrSetting);
					}
				} catch (Exception e) {
					Debug.e(e.toString());
				}
		return obj;

	}
	
}
