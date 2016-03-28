package com.onlinemarketing.object;

public class SettingVO {
	private int id;
	private String name;
	private String link;
	private String method;
	private String avatar;
	private String quantily;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getQuantily() {
		return quantily;
	}
	public void setQuantily(String quantily) {
		this.quantily = quantily;
	}
	
}
