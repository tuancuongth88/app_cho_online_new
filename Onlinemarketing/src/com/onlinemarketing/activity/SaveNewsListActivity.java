package com.onlinemarketing.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.onlinemarketing.R;
import com.onlinemarketing.adapter.HomePageAdapter;
import com.onlinemarketing.adapter.SaveNewsListAdapter;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonProduct;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.ProductVO;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SaveNewsListActivity extends BaseActivity implements OnItemClickListener, OnClickListener {
	List<ProductVO> list = new ArrayList<ProductVO>();
	ProgressDialog progressDialog;
	ListView listview;
	HomePageAdapter adapter;
	SaveNewsListAdapter newsListAdapter;
	Intent intent;
	ImageView imgBack;
	TextView txt_showToast;
	// status va link ben fragment right goi sang
	public static int status;
	public static String link;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_news_list);
		listview = (ListView) findViewById(R.id.listPoster);
		imgBack = (ImageView) findViewById(R.id.imgBackTitle);
		txt_showToast = (TextView) findViewById(R.id.txt_showToast);
		imgBack.setOnClickListener(this);
		listview.setOnItemClickListener(this);
		if (isConnect()) {
			new HomeAsystask().execute(status);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ProductDetailActivity.id_product = list.get(position).getId();
		if (status != Constan.getIntProperty("daan"))
			startActivity(new Intent(SaveNewsListActivity.this, ProductDetailActivity.class));
	}

	public class HomeAsystask extends AsyncTask<Integer, Integer, OutputProduct> {
		String Device_id;
		JsonProduct product;

		public HomeAsystask() {
			super();
		}

		@Override
		protected void onPreExecute() {
			product = new JsonProduct();
			progressDialog = new ProgressDialog(SaveNewsListActivity.this);
			progressDialog.setMessage("Loading...");
			progressDialog.setIndeterminate(false);
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected OutputProduct doInBackground(Integer... params) {
			if (status == Constan.getIntProperty("tindaluu")) {
				MainActivity.oOput = product.paserProduct(SystemConfig.user_id, SystemConfig.session_id,
						SystemConfig.device_id, MainActivity.id_category, SystemConfig.statusListSaveProduct);

				list = MainActivity.oOput.getProductVO();
			} else if (status == Constan.getIntProperty("dangban")) {
				MainActivity.oOput = product.paserProductSetting(SystemConfig.user_id, SystemConfig.session_id,
						SystemConfig.device_id, link);
				list = MainActivity.oOput.getProductVO();
			} else if (status == Constan.getIntProperty("doiduyet")) {
				MainActivity.oOput = product.paserProductSetting(SystemConfig.user_id, SystemConfig.session_id,
						SystemConfig.device_id, link);
				list = MainActivity.oOput.getProductVO();
			} else if (status == Constan.getIntProperty("bituchoi")) {
				MainActivity.oOput = product.paserProductSetting(SystemConfig.user_id, SystemConfig.session_id,
						SystemConfig.device_id, link);
				list = MainActivity.oOput.getProductVO();
			} else if (status == Constan.getIntProperty("daan")) {
				MainActivity.oOput = product.paserProductSetting(SystemConfig.user_id, SystemConfig.session_id,
						SystemConfig.device_id, link);
				list = MainActivity.oOput.getProductVO();
			}

			return MainActivity.oOput;
		}

		@Override
		protected void onPostExecute(OutputProduct result) {
			if (list.size() >  0) {
				if (status == Constan.getIntProperty("tindaluu")) {
					newsListAdapter = new SaveNewsListAdapter(SaveNewsListActivity.this, R.layout.item_save_news_list,
							list);
					listview.setAdapter(newsListAdapter);
				} else {
					adapter = new HomePageAdapter(SaveNewsListActivity.this, R.layout.item_trang_chu, list);
					listview.setAdapter(adapter);
				}
			}else {
				listview.setVisibility(View.GONE);
				txt_showToast.setText(Constan.getProperty("Error06"));
				txt_showToast.setVisibility(View.VISIBLE);
			}
			progressDialog.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		this.finish();
	}
}
