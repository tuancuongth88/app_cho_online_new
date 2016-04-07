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
import com.onlinemarketing.object.ProfileVO;
import com.onlinemarketing.util.Util;

import android.graphics.Bitmap;

public class JsonProfile {
	JSONObject jsonObject;
	StringBuilder request;
	public OutputProduct paserProfile(String user_id, String session_id, String device_id, int status) {
		OutputProduct obj = new OutputProduct();
		String str = null ;
		// check email password
		JSONObject jsonProfile = null;
		JSONArray jsonProfiledata = null;
				try {
					request = new StringBuilder(SystemConfig.API );
					if (status == SystemConfig.statusProfile) {
						request.append(SystemConfig.Profile);
					}else if (status == SystemConfig.statusFavorite) {
						request.append(SystemConfig.Favorite);
					}
					request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
					request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
					request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
					if (status == SystemConfig.statusProfile) {
						str = Util.getjSonUrl(request.toString(), SystemConfig.httpget);
					}else if (status == SystemConfig.statusFavorite) {
						str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
					}
					Debug.e("link: " + request);
					jsonObject = new JSONObject(str);
					obj.setCode(jsonObject.getInt("code"));					
					obj.setMessage(jsonObject.getString("message"));
					if(jsonObject.has("session_id"))
						obj.setSession_id(jsonObject.getString("session_id"));
					if (status == SystemConfig.statusFavorite) {
						jsonProfiledata = jsonObject.getJSONArray("data");
						if (obj.getCode() == Constan.getIntProperty("success")) 
						{
							ArrayList<ProfileVO> arrprofile = new ArrayList<ProfileVO>();
							for (int i = 0; i < jsonProfiledata.length(); i++) {
								jsonProfile = jsonProfiledata.getJSONObject(i);
								ProfileVO objProfile = new ProfileVO();
								objProfile.setId(jsonProfile.getInt("id"));
								objProfile.setFacebook_id(jsonProfile.getString("facebook_id"));
								objProfile.setGoogle_id(jsonProfile.get("google_id").toString());
								objProfile.setUsername(jsonProfile.get("username").toString());
								objProfile.setAvatar(jsonProfile.get("avatar").toString());
								objProfile.setEmail(jsonProfile.get("email").toString());
								objProfile.setPhone(jsonProfile.get("phone").toString());
								objProfile.setAddress(jsonProfile.get("address").toString());
								objProfile.setLat(jsonProfile.get("lat").toString());
								objProfile.setLongs(jsonProfile.get("long").toString());
								objProfile.setType(jsonProfile.get("type").toString());
								objProfile.setStatus(jsonProfile.get("status").toString());
								objProfile.setCreated_at(jsonProfile.get("created_at").toString());
								
								arrprofile.add(objProfile);
								obj.setProfileVO(arrprofile);
							}
						}
					}else{
						jsonProfile = jsonObject.getJSONObject("data");
						if (obj.getCode() == Constan.getIntProperty("success")) 
							{
								ArrayList<ProfileVO> arrprofile = new ArrayList<ProfileVO>();
									ProfileVO objProfile = new ProfileVO();
									objProfile.setId(jsonProfile.getInt("id"));
									objProfile.setFacebook_id(jsonProfile.getString("facebook_id"));
									objProfile.setGoogle_id(jsonProfile.get("google_id").toString());
									objProfile.setUsername(jsonProfile.get("username").toString());
									objProfile.setAvatar(jsonProfile.get("avatar").toString());
									objProfile.setEmail(jsonProfile.get("email").toString());
									objProfile.setPhone(jsonProfile.get("phone").toString());
									objProfile.setAddress(jsonProfile.get("address").toString());
									objProfile.setLat(jsonProfile.get("lat").toString());
									objProfile.setLongs(jsonProfile.get("long").toString());
									objProfile.setType(jsonProfile.get("type").toString());
									objProfile.setStatus(jsonProfile.get("status").toString());
									objProfile.setCreated_at(jsonProfile.get("created_at").toString());
									arrprofile.add(objProfile);
									obj.setProfileVO(arrprofile);
							}
					}
				} catch (Exception e) {
					Debug.e(e.toString());
				}
		return obj;

	}
	public Output postPaserProfile(String user_id, String session_id, String device_id, 
			ProfileVO profile) {
		Output obj = new Output();
		String str = null ;
		// check email password
		
				try {
					request = new StringBuilder(SystemConfig.API );
					request.append(SystemConfig.Profile);
					request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
					request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
					request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
					
					request.append("&username=").append(URLEncoder.encode(profile.getUsername(), "UTF-8"));
					request.append("&email=").append(profile.getEmail());
					request.append("&phone=").append(URLEncoder.encode(profile.getPhone(), "UTF-8"));
					request.append("&address=").append(URLEncoder.encode(profile.getAddress(), "UTF-8"));
					request.append("&password=").append(URLEncoder.encode(profile.getPass(), "UTF-8"));
					request.append("&old_password=").append(URLEncoder.encode(profile.getOld_pass(), "UTF-8"));
					if(profile.getAvatar() != null)
						request.append("&avatar=").append(URLEncoder.encode(profile.getAvatar(), "UTF-8"));
					else
						request.append("&avatar=");
					str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
					Debug.e("Str: "+str);
					jsonObject = new JSONObject(str);
					obj.setCode(jsonObject.getInt("code"));					
					obj.setMessage(jsonObject.getString("message"));
					obj.setSession_id(jsonObject.getString("session_id"));
				} catch (Exception e) {
					Debug.e(e.toString());
				}
		return obj;

	} 
	
	public Output doFileUpload(String user_id, String session_id, String device_id, String link, Bitmap bit) {
		Output output = new Output();
		output = Util.doFileUpload(user_id, session_id, device_id, link, bit);
		return output;
	}
}
