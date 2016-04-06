package com.onlinemarketing.object;

import java.util.List;

public class SearchVO {
	String code,message,user_id,session_id;
	String id,name,lat, log, price_id, category_id, type_id, time_id;
	List<Category_SearchVO> lstCategorySearch;
	List<TypeVO> lstType;
	List<TimeVO> lstTime;
	List<CityVO> lstCity;
	
	public List<CityVO> getLstCity() {
		return lstCity;
	}
	public void setLstCity(List<CityVO> lstCity) {
		this.lstCity = lstCity;
	}
	public List<TimeVO> getLstTime() {
		return lstTime;
	}
	public void setLstTime(List<TimeVO> lstTime) {
		this.lstTime = lstTime;
	}
	public List<TypeVO> getLstType() {
		return lstType;
	}
	public void setLstType(List<TypeVO> lstType) {
		this.lstType = lstType;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getPrice_id() {
		return price_id;
	}
	public void setPrice_id(String price_id) {
		this.price_id = price_id;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	public String getTime_id() {
		return time_id;
	}
	public void setTime_id(String time_id) {
		this.time_id = time_id;
	}
	public List<Category_SearchVO> getLstCategorySearch() {
		return lstCategorySearch;
	}
	public void setLstCategorySearch(List<Category_SearchVO> lstCategorySearch) {
		this.lstCategorySearch = lstCategorySearch;
	}
	
	
}
