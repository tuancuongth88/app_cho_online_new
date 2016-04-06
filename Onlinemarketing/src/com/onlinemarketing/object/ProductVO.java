package com.onlinemarketing.object;

import java.util.List;

public class ProductVO {
	private int id;
	private String name;
	private String description;
	private String category_name;
	private String type_name;
	private String city_name;
	private String avatar;
	private String price;
	private int price_id;
	private int category_id;
	private int user_id;
	private int type_id;
	private int city_id;
	private String city;
	private String startdate;
	private int status;
	private String status_name;
	private String user_name;
	private String user_avatar;
	private int position;
	private String delete_at;
	private String create_at;
	private String lat, log;
	private String time_id, phone, time_name;
	private boolean isCheck;
	private boolean product_saved;
	private List<String> arrImageDetail;
	private String start, end;
	public boolean isProduct_saved() {
		return product_saved;
	}

	public String getTime_name() {
		return time_name;
	}

	public void setTime_name(String time_name) {
		this.time_name = time_name;
	}

	public void setProduct_saved(boolean product_saved) {
		this.product_saved = product_saved;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getDescription() {
		return description;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getStatus_name() {
		return status_name;
	}

	public void setStatus_name(String status_name) {
		this.status_name = status_name;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_avatar() {
		return user_avatar;
	}

	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}

	public List<String> getArrImageDetail() {
		return arrImageDetail;
	}

	public void setArrImageDetail(List<String> arrImageDetail) {
		this.arrImageDetail = arrImageDetail;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getPrice_id() {
		return price_id;
	}

	public void setPrice_id(int price_id) {
		this.price_id = price_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public int getType_id() {
		return type_id;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getDelete_at() {
		return delete_at;
	}

	public void setDelete_at(String delete_at) {
		this.delete_at = delete_at;
	}

	public String getCreate_at() {
		return create_at;
	}

	public void setCreate_at(String create_at) {
		this.create_at = create_at;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getTime_id() {
		return time_id;
	}

	public void setTime_id(String time_id) {
		this.time_id = time_id;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

}
