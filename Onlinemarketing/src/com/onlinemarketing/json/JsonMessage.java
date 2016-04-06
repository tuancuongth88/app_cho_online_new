/**
 * @author tuanc_000
 *
 */
package com.onlinemarketing.json;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.lib.Debug;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.object.MessageVO;
import com.onlinemarketing.object.OutputMessage;
import com.onlinemarketing.util.Util;

public class JsonMessage {
	JSONObject jsonObject;
	StringBuilder request;
	/**
	 * Show list message history
	 * @param user_id
	 * @param session_id
	 * @param device_id
	 * @return OutputProduct
	 */
	public OutputMessage paseListNewMessage(String user_id, String session_id, String device_id) {
		OutputMessage oOput = new OutputMessage();
		String str = null ;
		try{
			request = new StringBuilder(SystemConfig.API );
			request.append(SystemConfig.Message);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
			jsonObject = new JSONObject(str);
			oOput.setCode(jsonObject.getInt("code"));					
			oOput.setMessage(jsonObject.getString("message"));
			oOput.setSession_id(jsonObject.getString("session_id"));
			oOput.setUser_Id(jsonObject.getString("user_id"));
			JSONArray objdata = jsonObject.getJSONArray("data");
			if (oOput.getCode() == Constan.getIntProperty("success")) {
				ArrayList<MessageVO> arrayMessage = new ArrayList<MessageVO>();
				//data seand
				for (int i = 0; i < objdata.length(); i++) {
					JSONObject objjson_message = objdata.getJSONObject(i);
					MessageVO objmessage = new MessageVO();
					objmessage.setId(objjson_message.getInt("id"));
					objmessage.setReceiver_id(objjson_message.getInt("chat_id"));
					objmessage.setMessage(objjson_message.getString("message"));
					objmessage.setCreate_at(objjson_message.getString("created_at"));
					objmessage.setUsername(objjson_message.getString("chat_name"));
					objmessage.setAvatar(objjson_message.getString("chat_avatar"));
					objmessage.setStatus(Integer.parseInt(objjson_message.get("status").toString()));
					objmessage.setBlock(objjson_message.getBoolean("block"));
					arrayMessage.add(objmessage);
				}
				
				oOput.setArrMessage(arrayMessage);
			}
		}catch (Exception e) {
			Debug.e(e.toString());
		}
		return oOput;
	}
	public OutputMessage SendMessage(String user_id, String session_id, String device_id, int id_chat, String message) {
		OutputMessage output = new OutputMessage();
		try{
		String str;
		request = new StringBuilder(SystemConfig.API);
		request.append(SystemConfig.Message);
		request.append("/"+SystemConfig.sendUserChar+"/"+id_chat);
		request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
		request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
		request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
		request.append("&message=").append(URLEncoder.encode(message, "UTF-8"));
		Debug.e("Link : " + request.toString());
		str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
		jsonObject = new JSONObject(str);
		output.setCode(jsonObject.getInt("code"));					
		output.setMessage(jsonObject.getString("message"));
		output.setSession_id(jsonObject.getString("session_id"));
		output.setUser_Id(jsonObject.getString("user_id"));
		JSONObject objdata = jsonObject.getJSONObject("data");
		output.setMessage_id_send(objdata.getString("message_id"));
		}catch (Exception e) {
			Debug.e(e.toString());
		}
		return output;
	}
	/**
	 * @param user_id
	 * @param session_id
	 * @param device_id
	 * @param chat_id
	 * @return
	 */
	public OutputMessage paseListHistoryMessage(String user_id, String session_id, String device_id, int chat_id) {
		OutputMessage oOput = new OutputMessage();
		String str = null ;
		try{
			request = new StringBuilder(SystemConfig.API );
			request.append(SystemConfig.Message+"/"+SystemConfig.HistoryMsg+"/"+ chat_id);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
			jsonObject = new JSONObject(str);
			oOput.setCode(jsonObject.getInt("code"));					
			oOput.setMessage(jsonObject.getString("message"));
			oOput.setSession_id(jsonObject.getString("session_id"));
			oOput.setUser_Id(jsonObject.getString("user_id"));
			JSONArray objdata = jsonObject.getJSONArray("data");
			if (oOput.getCode() == Constan.getIntProperty("success")) {
				ArrayList<MessageVO> arrayMessage = new ArrayList<MessageVO>();
				//data seand
				for (int i = 0; i < objdata.length(); i++) {
					JSONObject objjson_message = objdata.getJSONObject(i);
					MessageVO objmessage = new MessageVO();
					objmessage.setId(objjson_message.getInt("id"));
					objmessage.setMessage(objjson_message.getString("message"));
					objmessage.setCreate_at(objjson_message.getString("created_at"));
					objmessage.setUsername(objjson_message.getString("chat_name"));
					objmessage.setAvatar(objjson_message.getString("chat_avatar"));
					objmessage.setSend(Boolean.valueOf(objjson_message.get("send").toString()));
					arrayMessage.add(objmessage);
				}
				
				oOput.setArrMessage(arrayMessage);
			}
		}catch (Exception e) {
			Debug.e(e.toString());
		}
		return oOput;
	} 
	public OutputMessage paserDeleteMsg(String user_id, String session_id, String device_id, int message_id){
		OutputMessage oOput = new OutputMessage();
		String str = null ;
		try{
			request = new StringBuilder(SystemConfig.API );
			request.append(SystemConfig.Message+"/"+SystemConfig.DeleteMessage+"/"+message_id);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			Debug.e(request.toString());
			str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
			jsonObject = new JSONObject(str);
			oOput.setCode(jsonObject.getInt("code"));					
			oOput.setMessage(jsonObject.getString("message"));
			oOput.setSession_id(jsonObject.getString("session_id"));
			oOput.setUser_Id(jsonObject.getString("user_id"));
		}catch (Exception e) {
			Debug.e(e.toString());
		}
		return oOput;
	}
	
	public OutputMessage paserDeleteGroupMsg(String user_id, String session_id, String device_id, int message_id){
		OutputMessage oOput = new OutputMessage();
		String str = null ;
		try{
			request = new StringBuilder(SystemConfig.API );
			request.append(SystemConfig.Message+"/"+SystemConfig.sendUserChar+"/"+SystemConfig.DeleteMessage+"/"+message_id);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			request.append("&chat_id=").append(message_id);
			Debug.e(request.toString());
			str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
			jsonObject = new JSONObject(str);
			oOput.setCode(jsonObject.getInt("code"));					
			oOput.setMessage(jsonObject.getString("message"));
			oOput.setSession_id(jsonObject.getString("session_id"));
			oOput.setUser_Id(jsonObject.getString("user_id"));
		}catch (Exception e) {
			Debug.e(e.toString());
		}
		return oOput;
	}
	
	public OutputMessage paserBlockUser(String user_id, String session_id, String device_id, int account_id){
		OutputMessage oOput = new OutputMessage();
		String str = null ;
		try{
			request = new StringBuilder(SystemConfig.API );
			request.append(SystemConfig.BlockMessage+"/"+account_id);
			request.append("?user_id=").append(URLEncoder.encode(user_id, "UTF-8"));
			request.append("&session_id=").append(URLEncoder.encode(session_id, "UTF-8"));
			request.append("&device_id=").append(URLEncoder.encode(device_id, "UTF-8"));
			Debug.e(request.toString());
			str = Util.getjSonUrl(request.toString(), SystemConfig.httppost);
			jsonObject = new JSONObject(str);
			oOput.setCode(jsonObject.getInt("code"));					
			oOput.setMessage(jsonObject.getString("message"));
			oOput.setSession_id(jsonObject.getString("session_id"));
			oOput.setUser_Id(jsonObject.getString("user_id"));
		}catch (Exception e) {
			Debug.e(e.toString());
		}
		return oOput;
	}
}
