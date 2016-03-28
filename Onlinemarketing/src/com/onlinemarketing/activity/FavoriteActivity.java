package com.onlinemarketing.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.onlinemarketing.activity.BackListActivity.DeleteAsynTask;
import com.onlinemarketing.adapter.BackListAdapter;
import com.onlinemarketing.adapter.FavoriteAdapter;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonProduct;
import com.onlinemarketing.object.BackListVO;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.ProfileVO;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FavoriteActivity extends BaseActivity implements OnItemClickListener, OnClickListener{

	ListView listview;
	FavoriteAdapter adapter;
	private int id_delete, positon;
	// List<ProfileVO> list = new ArrayList<ProfileVO>();
	static Output out;
	Dialog dialog;
	Button btnOk, btnCancle;
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
		positon = arg2;
		dialogDelete();
	}

	public void dialogDelete() {
		dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_delete);
		dialog.setTitle("Thông Báo");
		btnOk = (Button) dialog.findViewById(R.id.btn_Ok_Delete);
		btnCancle = (Button) dialog.findViewById(R.id.btn_Cancle_Delete);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isConnect()) {
					new DeleteAsynTask().execute();
				}
				dialog.dismiss();
			}
		});
		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
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
			out = jsonProduct.paserDeleteBackListAndFavorite(SystemConfig.user_id, SystemConfig.session_id,
					SystemConfig.device_id, id_delete, SystemConfig.statusDeleteFavorite);

			return out;
		}

		@Override
		protected void onPostExecute(Output result) {
			if (result.getCode() == Constan.getIntProperty("success")) {
				Debug.showAlert(FavoriteActivity.this, result.getMessage());
				SystemConfig.oOputproduct.getProfileVO().remove(positon);
				adapter = new FavoriteAdapter(FavoriteActivity.this, R.layout.item_favorite,
						SystemConfig.oOputproduct.getProfileVO());
				Debug.e("danh sach: " + SystemConfig.oOputproduct.getProfileVO().size());
				listview.setAdapter(adapter);
			}
			super.onPostExecute(result);
		}
	}
	@Override
	public void onClick(View v) {
		this.finish();
	}
}
