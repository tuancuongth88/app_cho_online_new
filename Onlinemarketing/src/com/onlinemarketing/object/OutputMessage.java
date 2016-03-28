package com.onlinemarketing.object;

import java.util.ArrayList;

public class OutputMessage {
	private int code;
	private String message, session_id, user_Id, message_id_send;
	private ArrayList<MessageVO> arrMessage;

	public String getMessage_id_send() {
		return message_id_send;
	}

	public void setMessage_id_send(String message_id_send) {
		this.message_id_send = message_id_send;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public String getUser_Id() {
		return user_Id;
	}

	public void setUser_Id(String user_Id) {
		this.user_Id = user_Id;
	}

	public ArrayList<MessageVO> getArrMessage() {
		return arrMessage;
	}

	public void setArrMessage(ArrayList<MessageVO> arrMessage) {
		this.arrMessage = arrMessage;
	}

	

	

}
