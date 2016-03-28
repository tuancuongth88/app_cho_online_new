package com.onlinemarketing.json;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lib.Debug;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.object.CategoryVO;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.util.Util;

public class JsonCategory {
	JSONObject jsonObject;
	StringBuilder request;

	public OutputProduct paserCategory(String user_id, String session_id, String device_id) {
		OutputProduct list_category = new OutputProduct();
		try {
			request = new StringBuilder(SystemConfig.API);
			request.append(SystemConfig.Category);
			request.append("?user_id=")
					.append(URLEncoder.encode(
							user_id, "UTF-8"));
			request.append("&session_id=").append(
					URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(
					URLEncoder.encode(device_id, "UTF-8"));
			Debug.e("Link: " + request);
			String str = Util.getjSonUrl(request.toString(),
					SystemConfig.httppost);
			jsonObject = new JSONObject(str);
			list_category.setCode(jsonObject.getInt("code"));
			list_category.setMessage(jsonObject.getString("message"));
			list_category.setSession_id(jsonObject.getString("session_id"));
			list_category.setUser_Id(jsonObject.getString("user_id"));
			JSONArray jsonProduct = jsonObject.getJSONArray("data");
			if (list_category.getCode() == Constan.getIntProperty("success")) {
				ArrayList<CategoryVO> arrCategory = new ArrayList<CategoryVO>();
				for (int i = 0; i < jsonProduct.length(); i++) {
					CategoryVO categoryvo = new CategoryVO();
					JSONObject objjson_category = jsonProduct.getJSONObject(i);
					categoryvo.setId(objjson_category.getInt("id"));
					categoryvo.setName(objjson_category.getString("name"));
					if(categoryvo.getId()!= 0){
					categoryvo.setAvatar(objjson_category.getString("image_url"));
					categoryvo.setLike(objjson_category.getBoolean("like"));
					Debug.e("image_url: " + categoryvo.getAvatar() + "\n" + "like: " + categoryvo.isLike());
					}
					arrCategory.add(categoryvo);
				}
				list_category.setCategoryVO(arrCategory);
			}
		} catch (Exception e) {
			Debug.e(e.toString());
		}
		return list_category;

	}
	public Output paserLikeCategory(String user_id, String session_id, String device_id, int id) {
		  Output obj = new Output();
		  String str = null;
		  try {
		   request = new StringBuilder(SystemConfig.API);
		   request.append(SystemConfig.Category + "/" + id + "/" + SystemConfig.Action);
		   request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
		   request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
		   request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
		   Debug.e("link aaaaaaaaaaaaaaaa: " + request.toString());
		   str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
		   Debug.e("Str: " + str);
		   jsonObject = new JSONObject(str);
		   obj.setCode(jsonObject.getInt("code"));
		   obj.setMessage(jsonObject.getString("message"));
		   obj.setSession_id(jsonObject.getString("session_id"));
		   obj.setUser_Id(jsonObject.getString("user_id"));
		  } catch (Exception e) {
		   Debug.e(e.toString());
		  }
		  return obj;
		 }
}
