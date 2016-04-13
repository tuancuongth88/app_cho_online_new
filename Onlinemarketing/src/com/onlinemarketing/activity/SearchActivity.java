package com.onlinemarketing.activity;

import com.example.onlinemarketing.R;
import com.onlinemarketing.adapter.HomePageAdapter;
import com.onlinemarketing.config.SystemConfig;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class SearchActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

	ListView listView;
	ImageView imgBack;
	HomePageAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);       
		imgBack = (ImageView) findViewById(R.id.imgBackTitle);
		listView = (ListView) findViewById(R.id.listSearch);
		adapter = new HomePageAdapter(this, R.layout.item_trang_chu, SystemConfig.oOputproduct.getProductVO());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		imgBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ProductDetailActivity.id_product = SystemConfig.oOputproduct.getProductVO().get(arg2).getId();
		startActivity(new Intent(SearchActivity.this, ProductDetailActivity.class));
	}

}
