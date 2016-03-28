package com.onlinemarketing.json;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lib.Debug;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.ProductVO;
import com.onlinemarketing.util.Util;

public class JsonSearch {

	JSONObject jsonObject;
	StringBuilder request;

	public Output paserSaveSearch(String user_id, String session_id, String device_id, String name, String lat,
			String log, String price_id, String category_id, String type_id, String time_id) {
		Output obj = new Output();
		String str = null;
		try {
			request = new StringBuilder(SystemConfig.API);
			request.append(SystemConfig.SearchSave);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			request.append("&name=").append(URLEncoder.encode(name, "UTF-8"));
			request.append("&lat=").append(URLEncoder.encode(lat, "UTF-8"));
			request.append("&long=").append(URLEncoder.encode(log, "UTF-8"));
			request.append("&price_id=").append(URLEncoder.encode(price_id, "UTF-8"));
			request.append("&category_id=").append(URLEncoder.encode(category_id, "UTF-8"));
			request.append("&type_id=").append(URLEncoder.encode(type_id, "UTF-8"));
			request.append("&time_id=").append(URLEncoder.encode(time_id, "UTF-8"));
			
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
	
	
	public OutputProduct paserListSearch(String user_id, String session_id, String device_id) {
		OutputProduct obj = new OutputProduct();
		String str = null;
		try {
			request = new StringBuilder(SystemConfig.API);
			request.append(SystemConfig.SearchLog);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			Debug.e("link : " + request.toString());
			str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
			Debug.e("str: " + str.toString());
			jsonObject = new JSONObject(str);
			obj.setCode(jsonObject.getInt("code"));
			obj.setMessage(jsonObject.getString("message"));
			obj.setSession_id(jsonObject.getString("session_id"));
			obj.setUser_Id(jsonObject.getString("user_id"));
			JSONArray jsonProduct = jsonObject.getJSONArray("data");
			if (obj.getCode() == Constan.getIntProperty("success")) {
				ArrayList<ProductVO> arrProduct = new ArrayList<ProductVO>();
				for (int i = 0; i < jsonProduct.length(); i++) {
					JSONObject objjson_product = jsonProduct.getJSONObject(i);
					ProductVO objproduct = new ProductVO();
					objproduct.setId(objjson_product.getInt("id"));
					objproduct.setName(objjson_product.get("name").toString());
					objproduct.setPrice_id(objjson_product.getInt("price_id"));
					objproduct.setCategory_id(objjson_product.getInt("category_id"));
					objproduct.setUser_id(objjson_product.getInt("user_id"));
					objproduct.setType_id(objjson_product.getInt("type_id"));
					objproduct.setTime_id(objjson_product.getString("time_id"));
					objproduct.setLat(objjson_product.getString("lat"));
					objproduct.setLog(objjson_product.getString("long"));
					objproduct.setCreate_at(objjson_product.get("created_at").toString());
					arrProduct.add(objproduct);
				}
				obj.setProductVO(arrProduct);
			}

		} catch (Exception e) {
			Debug.e(e.toString());
		}

		return obj;

	}
	
}
