package com.onlinemarketing.activity;

import java.util.ArrayList;

import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.onlinemarketing.adapter.BackListAdapter;
import com.onlinemarketing.adapter.ListSaveSearchAdapter;
import com.onlinemarketing.adapter.ListSaveSearchAdapter.CallbackPosition;
import com.onlinemarketing.adapter.ListSaveSearchAdapter.ViewHolder;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonProduct;
import com.onlinemarketing.json.JsonSearch;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.ProductVO;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

public class ListSaveSearchActivity extends BaseActivity
		implements OnItemClickListener,  CallbackPosition, OnClickListener {

	ListView listview;
	ListSaveSearchAdapter adapter;
	OutputProduct oOput;
	Output out;
	ViewHolder viewHolder;
	ImageView imgBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_save_search);
		listview = (ListView) findViewById(R.id.listSaveSearch);
		imgBack = (ImageView) findViewById(R.id.imgBackTitle);
		imgBack.setOnClickListener(this);
		listview.setOnItemClickListener(this);
		if (isConnect()) {
			new ListSaveSearchAsystask().execute();
		}
	}

	@Override
	protected void onResume() {
		ListSaveSearchAdapter.type = 0;
		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		startActivity(new Intent(ListSaveSearchActivity.this, BackListActivity.class));
		//
	}


	public class ListSaveSearchAsystask extends AsyncTask<Integer, String, OutputProduct> {
		JsonSearch jsonListSaveSearch;
		ArrayList<ProductVO> list = new ArrayList<ProductVO>();

		@Override
		protected void onPreExecute() {
			jsonListSaveSearch = new JsonSearch();
			super.onPreExecute();
		}

		@Override
		protected OutputProduct doInBackground(Integer... params) {
			oOput = jsonListSaveSearch.paserListSearch(SystemConfig.user_id, SystemConfig.session_id,
					SystemConfig.device_id);
			list = oOput.getProductVO();
			SystemConfig.oOputproduct.setProductVO(list);
			return oOput;
		}

		@Override
		protected void onPostExecute(OutputProduct result) {
			adapter = new ListSaveSearchAdapter(getLayoutInflater(), list, ListSaveSearchActivity.this);
			listview.setAdapter(adapter);
		}
	}

	@Override
	public void callbackDeletePosition(int position) {
	}

	@Override
	public void onClick(View v) {
		this.finish();
//		ArrayList<ProductVO> deletelist = new ArrayList<ProductVO>();
//		ArrayList<ProductVO> list = new ArrayList<ProductVO>();
//		for (int i = 0; i < adapter.getList().size(); i++) {
//			if (!adapter.getList().get(i).isCheck()) {
//				list.add(adapter.getList().get(i));
//			} else {
//				deletelist.add(adapter.getList().get(i));
//			}
//		}
//		if (isConnect()) {
//			new DeleteAsynTask().execute();
//		}
	}
	public class DeleteAsynTask extends AsyncTask<Integer, String, Output> {

		JsonProduct jsonProduct;

		@Override
		protected void onPreExecute() {
			jsonProduct = new JsonProduct();
			super.onPreExecute();
		 }

		@Override
		protected Output doInBackground(Integer... params) {
			Debug.e("User: " + SystemConfig.user_id + " Session: " + SystemConfig.session_id + " device: "
					+ SystemConfig.device_id);
			out = jsonProduct.paserDeleteBackListAndFavorite(SystemConfig.user_id, SystemConfig.session_id, SystemConfig.device_id,
					4, SystemConfig.statusDeleteBackList);
			return out;
		}   

		@Override
		protected void onPostExecute(Output result) {
			if (result.getCode() == Constan.getIntProperty("success")) { 
				Debug.showAlert(ListSaveSearchActivity.this, result.getMessage());
//				list.remove(positon);
//				adapter = new BackListAdapter(ListSaveSearchActivity.this, R.layout.item_backlist, list);
//				Debu     g.e("danh sach: " + list.size());
//				listview.setAdapter(adapter);
			}
			super.onPostExecute(result);
		}
	}   
}
