package com.onlinemarketing.json;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lib.Debug;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.object.BackListVO;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.ProductVO;
import com.onlinemarketing.util.Util;

import android.graphics.Bitmap;

public class JsonProduct {
	JSONObject jsonObject;
	StringBuilder request;

	public OutputProduct paserProduct(String user_id, String session_id, String device_id, int id, int status) {
		OutputProduct obj = new OutputProduct();
		String str = null;
		// check email password

		try {
			request = new StringBuilder(SystemConfig.API);
			if (status == SystemConfig.statusCategoryProduct)
				request.append(SystemConfig.Category + "/" + id);
			if (status == SystemConfig.statusListSaveProduct)
				request.append(SystemConfig.Product_log);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			Debug.e("link : " + request.toString());
			if (status == SystemConfig.statusCategoryProduct || status == SystemConfig.statusListSaveProduct)
				str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
			else
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
					if(objjson_product.has("avatar"))
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
					arrProduct.add(objproduct);
				}
				obj.setProductVO(arrProduct);
			}

		} catch (Exception e) {
			Debug.e(e.toString());
		}

		return obj;

	}

	public OutputProduct paserNews(String user_id, String session_id, String device_id, int id) {
		OutputProduct obj = new OutputProduct();
		String str = null;
		try {
			request = new StringBuilder(SystemConfig.API);
			request.append(SystemConfig.Produc_Save + "/" + id);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
			Debug.e("Str: " + str);
			jsonObject = new JSONObject(str);
			obj.setCode(jsonObject.getInt("code"));
			obj.setMessage(jsonObject.getString("message"));
			obj.setSession_id(jsonObject.getString("session_id"));
		} catch (Exception e) {
			Debug.e(e.toString());
		}
		return obj;
	}

	public OutputProduct paserErrorReport(String user_id, String session_id, String device_id, int id, String message) {
		OutputProduct obj = new OutputProduct();
		String str = null;
		try {
			request = new StringBuilder(SystemConfig.API);
			request.append(SystemConfig.Product + "/" + id + "/" + SystemConfig.Report);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			request.append("&message=").append(URLEncoder.encode(message, "UTF-8"));

			str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
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

	public Output paserDeleteBackListAndFavorite(String user_id, String session_id, String device_id, int id,
			int status) {
		Output obj = new Output();
		String str = null;
		try {
			request = new StringBuilder(SystemConfig.API);
			if (status == SystemConfig.statusDeleteBackList) {
				request.append(SystemConfig.BackList + "/" + id + "/" + SystemConfig.Delete);
			} else if (status == SystemConfig.statusDeleteFavorite) {
				request.append(SystemConfig.Favorite + "/" + id + "/" + SystemConfig.Delete);
			} else if (status == SystemConfig.statusDeleteSearch) {
				request.append(SystemConfig.SearchLog + "/" + id + "/" + SystemConfig.Delete);
			}else if (status == SystemConfig.statusDeleteSaveNewsList) {
				request.append(SystemConfig.Product_log + "/" + id + "/" + SystemConfig.Delete);
			}
			else if (status == SystemConfig.statusFavoriteUser) {
				request.append(SystemConfig.Favorite + "/" + id + "/" + SystemConfig.Action);
			}
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));

			str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
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

	public OutputProduct paserBackList(String user_id, String session_id, String device_id) {
		OutputProduct obj = new OutputProduct();
		String str = null;
		try {
			request = new StringBuilder(SystemConfig.API);
			request.append(SystemConfig.BackList);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));

			str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
			jsonObject = new JSONObject(str);
			obj.setCode(jsonObject.getInt("code"));
			obj.setMessage(jsonObject.getString("message"));
			obj.setSession_id(jsonObject.getString("session_id"));
			obj.setUser_Id(jsonObject.getString("user_id"));
			JSONArray jsonArray = jsonObject.getJSONArray("data");
			if (obj.getCode() == Constan.getIntProperty("success")) {
				ArrayList<BackListVO> arrBacklist = new ArrayList<BackListVO>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject objjson_product = jsonArray.getJSONObject(i);
					BackListVO objBackList = new BackListVO();
					objBackList.setId(objjson_product.getInt("id"));
					objBackList.setUsername(objjson_product.get("username").toString());
					objBackList.setAvatar( objjson_product.get("avatar").toString());
					arrBacklist.add(objBackList);
					Debug.e("objproduct: " + objBackList.getAvatar());
				}
				obj.setBackListVO(arrBacklist);
			}
		} catch (Exception e) {
			Debug.e(e.toString());
		}
		return obj;
	}

	public OutputProduct paserProductDetail(String user_id, String session_id, String device_id, int id) {
		OutputProduct obj = new OutputProduct();
		List<String> img = new ArrayList<String>();
		String str = null;
		// check email password
		try {
			request = new StringBuilder(SystemConfig.API);
			request.append(SystemConfig.Product + "/" + id);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
			Debug.e(request.toString());
			Debug.e(str);
			jsonObject = new JSONObject(str);
			obj.setCode(jsonObject.getInt("code"));
			obj.setMessage(jsonObject.getString("message"));
			obj.setSession_id(jsonObject.getString("session_id"));
			JSONObject objjson_product = jsonObject.getJSONObject("data");
			if (obj.getCode() == Constan.getIntProperty("success")) {
				ArrayList<ProductVO> arrProduct = new ArrayList<ProductVO>();
				ProductVO objproduct = new ProductVO();
				objproduct.setId(objjson_product.getInt("id"));
				objproduct.setName(objjson_product.get("name").toString());
				objproduct.setAvatar(objjson_product.get("avatar").toString());
				objproduct.setPrice(objjson_product.get("price").toString());
				// objproduct.setPrice_id(objjson_product.getInt("price_id"));
				objproduct.setCategory_id(objjson_product.getInt("category_id"));
				objproduct.setUser_id(objjson_product.getInt("user_id"));
				objproduct.setType_id(objjson_product.getInt("type_id"));
				objproduct.setAddress(objjson_product.get("address").toString());
				objproduct.setStartdate(objjson_product.get("start_time").toString());
				objproduct.setStatus(objjson_product.getInt("status"));
				objproduct.setPosition(objjson_product.getInt("position"));
				objproduct.setPhone(objjson_product.get("phone").toString());
				objproduct.setUser_avatar(objjson_product.get("user_avatar").toString());
				objproduct.setDescription(objjson_product.getString("description"));
				objproduct.setCategory_name(objjson_product.getString("category_name"));
				objproduct.setType_name(objjson_product.getString("type_name"));
				objproduct.setCity_name(objjson_product.getString("city"));
				objproduct.setLat(objjson_product.getString("lat"));
				objproduct.setLog(objjson_product.getString("long"));
				objproduct.setStatus_name(objjson_product.getString("status_name"));
				objproduct.setUser_name(objjson_product.getString("user_name"));
				objproduct.setCheck(objjson_product.getBoolean("block"));
				objproduct.setProduct_saved(objjson_product.getBoolean("product_saved"));
				JSONArray objImage = objjson_product.getJSONArray("image_list");

				for (int i = 0; i < objImage.length(); i++) {
					JSONObject objimg = objImage.getJSONObject(i);
					String linkimage = objimg.getString("image_url");
					img.add(linkimage);
				}
				objproduct.setArrImageDetail(img);

				arrProduct.add(objproduct);
				obj.setProductVO(arrProduct);
			}

		} catch (Exception e) {
			Debug.e(e.toString());
		}

		return obj;

	}

	public OutputProduct paserProductSetting(String user_id, String session_id, String device_id, String link) {
		OutputProduct obj = new OutputProduct();
		String str = null;
		// check email password

		try {
			request = new StringBuilder(link);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			Debug.e("link : " + request.toString());
			str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
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
					// objproduct.setCheck(objjson_product.getBoolean("isCheck"));
					arrProduct.add(objproduct);
					Debug.e("objproduct: " + objproduct.getAvatar());
				}
				obj.setProductVO(arrProduct);
			}

		} catch (Exception e) {
			Debug.e(e.toString());
		}

		return obj;

	}
	
	//upload file
	public Output doFileUpload(String user_id, String session_id, String device_id, String file, Bitmap bit) {
		Output output = new Output();
		output = Util.UploadImageProduct(user_id, session_id, device_id, file, bit);
		return output;
	}

	
	public Output paserPostProduct(String user_id, String session_id, String device_id, 
			List<String> image_url, String avatar, String name, String category_id,
			String type_id, String lat, String log, String price, String description, int city_id, String address
			) {
		Output obj = new Output();
		String str = null;
		try {
			 String arrimg = Arrays.toString(image_url.toArray()); 
			request = new StringBuilder(SystemConfig.API);
			request.append(SystemConfig.Post_product);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			for (int i = 0; i < image_url.size(); i++) {
				request.append("&image_url[]=").append(URLEncoder.encode(image_url.get(i), "UTF-8"));
			}
			request.append("&avatar=").append(URLEncoder.encode(avatar, "UTF-8"));
			request.append("&name=").append(URLEncoder.encode(name, "UTF-8"));
			request.append("&category_id=").append(URLEncoder.encode(category_id, "UTF-8"));
			request.append("&type_id=").append(URLEncoder.encode(type_id, "UTF-8"));
			request.append("&lat=").append(URLEncoder.encode(lat, "UTF-8"));
			request.append("&long=").append(URLEncoder.encode(log, "UTF-8"));
			request.append("&price=").append(URLEncoder.encode(price, "UTF-8"));
			request.append("&description=").append(URLEncoder.encode(description, "UTF-8"));
			request.append("&city_id=").append(city_id);
			request.append("&address=").append(URLEncoder.encode(address, "UTF-8"));

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

	public Output paserForgotPass(String user_id, String session_id, String device_id, String email) {
		Output obj = new Output();
		String str = null;
		try {
			request = new StringBuilder(SystemConfig.API);
			request.append(SystemConfig.ResetPassword);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			request.append("&email=").append(URLEncoder.encode(email, "UTF-8"));
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
