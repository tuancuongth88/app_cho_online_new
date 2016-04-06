package com.onlinemarketing.activity;

import java.util.List;

import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.onlinemarketing.adapter.FavoriteAdapter;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.ProfileVO;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class FavoriteActivity extends BaseActivity implements OnItemClickListener, OnClickListener {

	ListView listview;
	FavoriteAdapter adapter;
	public static int id_delete;
	public static String phone;
	public static List<ProfileVO> listProfile;
	static Output out;

	ImageView imgBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);
		listview = (ListView) findViewById(R.id.listFavorite);
		imgBack = (ImageView) findViewById(R.id.imgBackTitle);
		imgBack.setOnClickListener(this);
		Debug.e("list favorite : " + SystemConfig.oOputproduct.getProfileVO());
		if (SystemConfig.oOputproduct.getProfileVO() != null) {
			adapter = new FavoriteAdapter(this, R.layout.item_favorite, SystemConfig.oOputproduct.getProfileVO());
			listview.setAdapter(adapter);
		}
		listview.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		id_delete = SystemConfig.oOputproduct.getProfileVO().get(arg2).getId();
		phone = SystemConfig.oOputproduct.getProfileVO().get(arg2).getPhone();
		PosterDetailActivity.statutActivityCall = 2;
		
		startActivity(new Intent(FavoriteActivity.this, PosterDetailActivity.class));
	}

	@Override
	public void onClick(View v) {
		this.finish();
	}
}
