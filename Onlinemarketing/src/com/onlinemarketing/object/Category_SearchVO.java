package com.onlinemarketing.object;

import java.util.List;

public class Category_SearchVO {
	String category_name;
	int category_id;
	List<PriceVO> lstPrice;
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public List<PriceVO> getLstPrice() {
		return lstPrice;
	}
	public void setLstPrice(List<PriceVO> lstPrice) {
		this.lstPrice = lstPrice;
	}
	
}
