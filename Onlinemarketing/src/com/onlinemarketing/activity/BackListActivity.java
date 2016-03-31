package com.onlinemarketing.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.onlinemarketing.adapter.BackListAdapter;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonProduct;
import com.onlinemarketing.object.BackListVO;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.OutputProduct;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BackListActivity extends BaseActivity implements OnItemClickListener, OnClickListener {

	List<BackListVO> list = new ArrayList<BackListVO>();
	ProgressDialog progressDialog;
	ListView listview;
	BackListAdapter adapter;
	OutputProduct oOput;
	Button btnSendSMS_Detail, btnCall;
	Intent intent;
	TextView txtDelete;
	static Output out;
	Dialog dialog;
	Button btnOk, btnCancle;
	public static int id_delete , positon;

	ImageView imgBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_back_list);
		listview = (ListView) findViewById(R.id.listBackList);
		imgBack = (ImageView) findViewById(R.id.imgBackTitle);
		imgBack.setOnClickListener(this);
		listview.setOnItemClickListener(this);
		if (isConnect()) {
			new BackListAsystask().execute();
		}
	}

	public class BackListAsystask extends AsyncTask<Integer, Integer, OutputProduct> {
		String Device_id;
		JsonProduct jsonBacklist;

		@Override
		protected void onPreExecute() {
			jsonBacklist = new JsonProduct();
			progressDialog = new ProgressDialog(BackListActivity.this);
			// Set progressdialog message
			progressDialog.setMessage("Loading...");
			progressDialog.setIndeterminate(false);
			// Show progressdialog
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected OutputProduct doInBackground(Integer... params) {
			oOput = jsonBacklist.paserBackList(SystemConfig.user_id, SystemConfig.session_id, SystemConfig.device_id);
			list = oOput.getBackListVO();
			return oOput;
		}

		@Override
		protected void onPostExecute(OutputProduct result) {
			adapter = new BackListAdapter(BackListActivity.this, R.layout.item_backlist, list);
			listview.setAdapter(adapter);
			progressDialog.dismiss();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		id_delete = list.get(arg2).getId();
	}


	@Override
	public void onClick(View v) {
		this.finish();
	}


}
