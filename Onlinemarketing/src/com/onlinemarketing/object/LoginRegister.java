package com.onlinemarketing.object;

public class LoginRegister {

	private int code, user_Id;
	private String message, session_id, device_id, facebook_id, google_id,facebook_name, google_name ="";
	public LoginRegister(int code, int user_Id, String message, String session_id, String device_id) {
		super();
		this.code = code;
		this.user_Id = user_Id;
		this.message = message;
		this.session_id = session_id;
		this.device_id = device_id;
	}
	
	public String getFacebook_id() {
		return facebook_id;
	}

	public void setFacebook_id(String facebook_id) {
		this.facebook_id = facebook_id;
	}

	public String getGoogle_id() {
		return google_id;
	}

	public void setGoogle_id(String google_id) {
		this.google_id = google_id;
	}

	public String getFacebook_name() {
		return facebook_name;
	}

	public void setFacebook_name(String facebook_name) {
		this.facebook_name = facebook_name;
	}

	public String getGoogle_name() {
		return google_name;
	}

	public void setGoogle_name(String google_name) {
		this.google_name = google_name;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public LoginRegister() {
		super();
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getUser_Id() {
		return user_Id;
	}
	public void setUser_Id(int user_Id) {
		this.user_Id = user_Id;
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
	
	
	
}
