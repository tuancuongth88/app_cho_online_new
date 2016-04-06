package com.onlinemarketing.json;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.lib.Debug;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.object.Category_SearchVO;
import com.onlinemarketing.object.CityVO;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.PriceVO;
import com.onlinemarketing.object.ProductVO;
import com.onlinemarketing.object.SearchVO;
import com.onlinemarketing.object.TimeVO;
import com.onlinemarketing.object.TypeVO;
import com.onlinemarketing.util.Util;

import android.text.format.Time;

public class JsonSearch {

	JSONObject jsonObject;
	StringBuilder request;

	public Output paserSaveSearch(String user_id, String session_id, String device_id, String name, String lat,
			String log, String price_id, String category_id, String type_id, String time_id, int city_id) {
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
			request.append("&city_id=").append(URLEncoder.encode(String.valueOf(city_id), "UTF-8"));
			
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

	public SearchVO getSearch(String user_id, String session_id, String device_id, int id) {
		SearchVO objSearch = new SearchVO();
		String str = null;
		try {
			request = new StringBuilder(SystemConfig.API);
			request.append(SystemConfig.Search + "/" + id);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			Debug.e("link : " + request.toString());
			str = Util.getjSonUrl(request.toString(), SystemConfig.httpget);
			Debug.e("str: " + str.toString());
			jsonObject = new JSONObject(str);
			objSearch.setCode(jsonObject.getString("code"));
			objSearch.setMessage(jsonObject.getString("message"));
			objSearch.setSession_id(jsonObject.getString("session_id"));
			objSearch.setUser_id(jsonObject.getString("user_id"));
			JSONObject jsonSearch = jsonObject.getJSONObject("data");
			if (objSearch.getCode().equals(String.valueOf(Constan.getIntProperty("success")))) {
				if (jsonSearch.has("id"))
					objSearch.setId(jsonSearch.getString("id"));
				objSearch.setName(jsonSearch.getString("name"));
				objSearch.setLat(jsonSearch.getString("lat"));
				objSearch.setLog(jsonSearch.getString("long"));
				objSearch.setPrice_id(jsonSearch.getString("price_id"));
				objSearch.setCategory_id(jsonSearch.getString("category_id"));
				objSearch.setType_id(jsonSearch.getString("type_id"));
				objSearch.setTime_id(jsonSearch.getString("time_id"));
				JSONArray jsonArrCity = jsonSearch.getJSONArray("cityArray");
				List<CityVO> arrCity = new ArrayList<CityVO>();
				for (int i = 0; i < jsonArrCity.length(); i++) {
					CityVO objCity = new CityVO();
					JSONObject jsonCity = jsonArrCity.getJSONObject(i);
					objCity.setId(jsonCity.getInt("city_id"));
					objCity.setName(jsonCity.get("city_name").toString());
					arrCity.add(objCity);
					
				}
				JSONArray jsonArrType = jsonSearch.getJSONArray("typeArray");
				List<TypeVO> arrType = new ArrayList<TypeVO>();
				for (int i = 0; i < jsonArrType.length(); i++) {
					TypeVO objType = new TypeVO();
					JSONObject jsonType = jsonArrType.getJSONObject(i);
					objType.setType_id(jsonType.getInt("type_id"));
					objType.setType_name(jsonType.get("type_name").toString());
					arrType.add(objType);
				}
				JSONArray jsonArrTime = jsonSearch.getJSONArray("timeArray");
				List<TimeVO> arrTime = new ArrayList<TimeVO>();
				for (int i = 0; i < jsonArrTime.length(); i++) {
					TimeVO objTime = new TimeVO();
					JSONObject jsonTime = jsonArrTime.getJSONObject(i);
					objTime.setTime_id(jsonTime.getInt("time_id"));
					objTime.setTime_name(jsonTime.getString("time_name"));
					arrTime.add(objTime);
				}
				JSONArray JsonArrcategory = jsonSearch.getJSONArray("categoryArray");
				// add category
				List<Category_SearchVO> arrcategory = new ArrayList<Category_SearchVO>();
				for (int i = 0; i < JsonArrcategory.length(); i++) {
					Category_SearchVO objcategory = new Category_SearchVO();
					JSONObject jsoncategory = JsonArrcategory.getJSONObject(i);
					objcategory.setCategory_id(jsoncategory.getInt("category_id"));
					objcategory.setCategory_name(jsoncategory.getString("category_name"));
					JSONArray jsonArrPrice = jsoncategory.getJSONArray("price");
					List<PriceVO> arrPrice = new ArrayList<PriceVO>();
					for (int j = 0; j < jsonArrPrice.length(); j++) {
						PriceVO objPrice = new PriceVO();
						JSONObject jsonPrice = jsonArrPrice.getJSONObject(j);
						objPrice.setPrice_id(jsonPrice.getInt("price_id"));
						objPrice.setEnd(jsonPrice.get("end").toString());
						objPrice.setStart(jsonPrice.get("start").toString());
						arrPrice.add(objPrice);
					}
					arrcategory.add(objcategory);
					objcategory.setLstPrice(arrPrice);
				}
				objSearch.setLstCategorySearch(arrcategory);
				objSearch.setLstType(arrType);
				objSearch.setLstTime(arrTime);
				objSearch.setLstCity(arrCity);

			}
		} catch (Exception e) {
			Debug.e(e.toString());
		}
		return objSearch;
	}

	public OutputProduct paserSearch(String user_id, String session_id, String device_id, String name, int city_id,
			int price_id, int category_id, int type_id, int time_id) {
		OutputProduct obj = new OutputProduct();
		String str = null;
		// check email password

		try {
			request = new StringBuilder(SystemConfig.API);
			request.append(SystemConfig.SearchAction);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));

			request.append("&name=").append(URLEncoder.encode(name, "UTF-8"));
			request.append("&city_id=").append(URLEncoder.encode(String.valueOf(city_id), "UTF-8"));
			request.append("&price_id=").append(URLEncoder.encode(String.valueOf(price_id), "UTF-8"));
			request.append("&category_id=").append(URLEncoder.encode(String.valueOf(category_id), "UTF-8"));
			request.append("&type_id=").append(URLEncoder.encode(String.valueOf(type_id), "UTF-8"));
			request.append("&time_id=").append(URLEncoder.encode(String.valueOf(time_id), "UTF-8"));
			Debug.e("link : " + request.toString());
			str = Util.getjSonUrl(request.toString(), SystemConfig.httpget);
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
					if (objjson_product.has("avatar"))
						objproduct.setAvatar(objjson_product.get("avatar").toString());
					objproduct.setPrice(objjson_product.get("price").toString());
					objproduct.setPrice_id(objjson_product.getInt("price_id"));
					objproduct.setCategory_id(objjson_product.getInt("category_id"));
					objproduct.setUser_id(objjson_product.getInt("user_id"));
					objproduct.setType_id(objjson_product.getInt("type_id"));
					objproduct.setCity_id(objjson_product.getInt("city_id"));
					objproduct.setStartdate(objjson_product.get("start_time").toString());

					objproduct.setStatus(objjson_product.getInt("status"));
					objproduct.setPosition(objjson_product.getInt("position"));
					objproduct.setDelete_at(objjson_product.get("deleted_at").toString());
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
