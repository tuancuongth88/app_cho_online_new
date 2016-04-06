package com.onlinemarketing.json;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lib.Debug;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.ProductVO;
import com.onlinemarketing.util.Util;

public class JsonListNewsPoster {

	JSONObject jsonObject;
	StringBuilder request;

	public OutputProduct paserListNewsPoster (String user_id, String session_id, String device_id, int id) {
		OutputProduct obj = new OutputProduct();
		String str = null;
		// check email password

		try {
			request = new StringBuilder(SystemConfig.API);
			request.append(SystemConfig.Product_User + "/" + id);
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
			JSONArray jsonProduct = jsonObject.getJSONArray("data");
			if (obj.getCode() == Constan.getIntProperty("success")) {
				ArrayList<ProductVO> arrProduct = new ArrayList<ProductVO>();
				for (int i = 0; i < jsonProduct.length(); i++) {
					JSONObject objjson_product = jsonProduct.getJSONObject(i);
					ProductVO objproduct = new ProductVO();
					objproduct.setId(objjson_product.getInt("id"));
					objproduct.setName(objjson_product.get("name").toString());
					objproduct.setAvatar(objjson_product.get("avatar").toString());
					objproduct.setPrice(objjson_product.get("price").toString());
					objproduct.setPrice_id(objjson_product.getInt("price_id"));
					objproduct.setCategory_id(objjson_product.getInt("category_id"));
					objproduct.setUser_id(objjson_product.getInt("user_id"));
					objproduct.setType_id(objjson_product.getInt("type_id"));
//					objproduct.setCity_id(objjson_product.getInt("city_id"));
					objproduct.setStartdate(objjson_product.get("start_time").toString());
					objproduct.setStatus(objjson_product.getInt("status"));
					objproduct.setPosition(objjson_product.getInt("position"));
					objproduct.setDelete_at(objjson_product.get("deleted_at").toString());
					objproduct.setCreate_at(objjson_product.get("created_at").toString());
					objproduct.setCheck(objjson_product.getBoolean("block"));
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
