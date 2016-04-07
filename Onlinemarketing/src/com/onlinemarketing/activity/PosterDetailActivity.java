package com.onlinemarketing.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.onlinemarketing.adapter.BackListAdapter;
import com.onlinemarketing.adapter.HomePageAdapter;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonListNewsPoster;
import com.onlinemarketing.json.JsonProduct;
import com.onlinemarketing.object.BackListVO;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.ProductVO;
import com.onlinemarketing.util.ChatDialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PosterDetailActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
	List<ProductVO> list = new ArrayList<ProductVO>();
	List<BackListVO> backListVOs = new ArrayList<BackListVO>();
	ProgressDialog progressDialog;
	ListView listview;
	static Output out;
	HomePageAdapter adapter;
	OutputProduct oOput;
	ImageView btnSendSMS_Detail, btnCall, img_chat, imgBack, imgFavorite;
	Intent intent;
	int idUser;
	String phone;
	public static int statutActivityCall;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poster_detail);
		listview = (ListView) findViewById(R.id.listPoster);
		btnSendSMS_Detail = (ImageView) findViewById(R.id.btnSendSMS_Detail);
		btnCall = (ImageView) findViewById(R.id.btnCall_Detail);
		img_chat = (ImageView) findViewById(R.id.btnChatDirectly_Detail);
		imgBack = (ImageView) findViewById(R.id.imgBackTitle);
		imgFavorite = (ImageView) findViewById(R.id.imgFavoriteTitle);
		imgFavorite.setOnClickListener(this);
		btnCall.setOnClickListener(this);
		btnSendSMS_Detail.setOnClickListener(this);
		img_chat.setOnClickListener(this);
		imgBack.setOnClickListener(this);
		listview.setOnItemClickListener(this);
		SetData();
	}

	public void SetData() {
		if (statutActivityCall == 1) {
			idUser = ProductDetailActivity.objproductDetail.getUser_id();
			phone = ProductDetailActivity.objproductDetail.getPhone();
			if (isConnect()) 	{
				new NewsPosterAsystask().execute();
			}

			if(!SystemConfig.user_id.isEmpty())
			if (Integer.parseInt(SystemConfig.user_id) == ProductDetailActivity.objproductDetail.getUser_id()) {
				imgFavorite.setVisibility(View.INVISIBLE);
			} else
				imgFavorite.setVisibility(View.VISIBLE);
			else 
				imgFavorite.setVisibility(View.VISIBLE);
		} else if (statutActivityCall == 2) {
			idUser = FavoriteActivity.id_delete;
			phone = FavoriteActivity.phone;
			if (isConnect()) {
				new NewsPosterAsystask().execute();
			}

		}
		
	}

	public class NewsPosterAsystask extends AsyncTask<Integer, Integer, OutputProduct> {
		String Device_id;
		JsonListNewsPoster newsPoster;

		@Override
		protected void onPreExecute() {
			newsPoster = new JsonListNewsPoster();
			progressDialog = new ProgressDialog(PosterDetailActivity.this);
			// Set progressdialog message
			progressDialog.setMessage("Loading...");
			progressDialog.setIndeterminate(false);
			// Show progressdialog
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected OutputProduct doInBackground(Integer... params) {
			oOput = newsPoster.paserListNewsPoster(SystemConfig.user_id, SystemConfig.session_id,
					SystemConfig.device_id, idUser);
			list = oOput.getProductVO();
			return oOput;
		}

		@Override
		protected void onPostExecute(OutputProduct result) {
			adapter = new HomePageAdapter(PosterDetailActivity.this, R.layout.item_trang_chu, list);
			listview.setAdapter(adapter);
			progressDialog.dismiss();
			if(list.size() > 0){
				ProductVO objproduct =  list.get(0);
				if(objproduct.isCheck()){
					btnCall.setClickable(false);
					btnSendSMS_Detail.setClickable(false);
					img_chat.setClickable(false);
					btnCall.setImageResource(R.drawable.call_hidden);
					btnSendSMS_Detail.setImageResource(R.drawable.sms_hidden);
					img_chat.setImageResource(R.drawable.chat_hidden);
					imgFavorite.setVisibility(View.INVISIBLE);
				}
				if (objproduct.isFavorite()) {
					imgFavorite.setClickable(false);
					imgFavorite.setImageResource(R.drawable.icon_favorite_success);
				}
			}

		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ProductDetailActivity.id_product = list.get(arg2).getId();
		startActivity(new Intent(PosterDetailActivity.this, ProductDetailActivity.class));
		this.finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSendSMS_Detail:
			intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
			startActivity(intent);
			break;

		case R.id.btnCall_Detail:
			intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
			startActivity(intent);
			break;
		case R.id.btnChatDirectly_Detail:
			ChatDialog chat = new ChatDialog(this);
			chat.run(SystemConfig.statusGetHistoryMessage,idUser);
			chat.dialogChat(idUser);
			break;
		case R.id.imgFavoriteTitle:
			if (!SystemConfig.session_id.isEmpty()) 
				if (isConnect()) {
					new FavoriteUserAsystask().execute();
				}
			else
				Debug.showAlert(PosterDetailActivity.this, "Ban can dang nhap tai khoan!!!!");
			break;
		case R.id.imgBackTitle:
			this.finish();
			break;
		}
	}

	public class FavoriteUserAsystask extends AsyncTask<Integer, String, Output> {

		JsonProduct jsonProduct;

		@Override
		protected void onPreExecute() {
			jsonProduct = new JsonProduct();
			super.onPreExecute();
		}

		@Override
		protected Output doInBackground(Integer... params) {
			Debug.e("User: " + ProductDetailActivity.objproductDetail.getUser_id());
			out = jsonProduct.paserDeleteBackListAndFavorite(SystemConfig.user_id, SystemConfig.session_id,
					SystemConfig.device_id, idUser,
					SystemConfig.statusFavoriteUser);
			return out;
		}

		@Override
		protected void onPostExecute(Output result) {
			if (result.getCode() == Constan.getIntProperty("success")) {
				Debug.showAlert(PosterDetailActivity.this, result.getMessage());
				imgFavorite.setImageResource(R.drawable.icon_favorite_success);
				imgFavorite.setClickable(false);
			}
			super.onPostExecute(result);
		}
	}

}
