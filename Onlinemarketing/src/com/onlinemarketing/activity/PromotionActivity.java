package com.onlinemarketing.activity;

import com.example.onlinemarketing.R;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonNews;
import com.onlinemarketing.json.JsonProduct;
import com.onlinemarketing.object.PromotionVO;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class PromotionActivity extends BaseActivity implements OnClickListener{
	ProgressDialog progressDialog;
	TextView txt_titleKM, txt_DesKM;
	PromotionVO objpromotionVo;
	//status va link ben fragment right goi sang hien tai chua dung
	public static int status;
	public static String link;
	ImageView imgBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promotion);
		txt_titleKM = (TextView) findViewById(R.id.txt_titlePromotion);
		txt_DesKM = (TextView) findViewById(R.id.txt_DescriptionPromotion);
		imgBack = (ImageView) findViewById(R.id.imgBackTitle);
		imgBack.setOnClickListener(this);
		if (isConnect()) {
			new NewsAsystask().execute();
		}
	}
	
	public class NewsAsystask extends AsyncTask<Integer, Integer, PromotionVO> {
		String Device_id;
		JsonNews news;

		public NewsAsystask() {
			super();
		}

		@Override
		protected void onPreExecute() {
			news = new JsonNews();
			progressDialog = new ProgressDialog(PromotionActivity.this);
			progressDialog.setMessage("Loading...");
			progressDialog.setIndeterminate(false);
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected PromotionVO doInBackground(Integer... params) {
			objpromotionVo = news.paserNews(SystemConfig.user_id, SystemConfig.session_id,
					SystemConfig.device_id, link);

			return objpromotionVo;
		}

		@Override
		protected void onPostExecute(PromotionVO result) {
			txt_titleKM.setText(result.getTitle());
			txt_DesKM.setText(result.getDescription());
			progressDialog.dismiss();
		}
	}
	@Override
	public void onClick(View v) {
		this.finish();
	}

}
