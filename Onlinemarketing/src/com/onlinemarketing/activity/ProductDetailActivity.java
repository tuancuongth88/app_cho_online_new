package com.onlinemarketing.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.androidquery.AQuery;
import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.onlinemarketing.adapter.ProductDetailAdapter;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.fragment.FragmentProductDetail;
import com.onlinemarketing.json.JsonProduct;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.ProductVO;
import com.onlinemarketing.util.ChatDialog;
import com.onlinemarketing.util.Message;
import com.onlinemarketing.util.Util;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetailActivity extends FragmentActivity implements OnClickListener {
	CirclePageIndicator mIndicator;
	ArrayList<Fragment> fragments;
	ImageView btnSendSMS_Detail, btnCall, btnChatDirectly_Detail, btnProducSave, btnErrorReport, btnPoster;
	EditText editErrorReport;
	TextView txt_titleDetaile, txtPrice_Detail, txtName_Detail, txtDatime_Detail, txtAdd_Detail, txtKM_Detail,
			txtCategory_Detail, txtTinhTrang_Detail, txt_info_Detail, txt_contact_Detail;
	Dialog dialog;
	Button btnOk, btnCancle;
	Intent intent;
	public static OutputProduct out;
	public static int id_product;
	public static ProductVO objproductDetail;
	ViewPager mPager;
	ProgressDialog progressDialog;
	ImageView imgback;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_product_detail);
		mPager = (ViewPager) findViewById(R.id.pager);
		findById();
		new ProductSaveAndReportAsynTask().execute(SystemConfig.statusProductDetail);

	}

	public void findById() {
		btnSendSMS_Detail = (ImageView) findViewById(R.id.btnSendSMS_Detail);
		btnCall = (ImageView) findViewById(R.id.btnCall_Detail);
		btnPoster = (ImageView) findViewById(R.id.btnPoster_Detail);
		btnProducSave = (ImageView) findViewById(R.id.btnSave_Detail);
		btnErrorReport = (ImageView) findViewById(R.id.btnReportViolations_Detail);
		btnChatDirectly_Detail = (ImageView) findViewById(R.id.btnChatDirectly_Detail);
		txtPrice_Detail = (TextView) findViewById(R.id.txtPrice_Detail);
		txtName_Detail = (TextView) findViewById(R.id.txtName_Detail);
		txtDatime_Detail = (TextView) findViewById(R.id.txtDatime_Detail);
		txtAdd_Detail = (TextView) findViewById(R.id.txtAdd_Detail);
		txtKM_Detail = (TextView) findViewById(R.id.txtKM_Detail);
		txtCategory_Detail = (TextView) findViewById(R.id.txtCategory_Detail);
		txtTinhTrang_Detail = (TextView) findViewById(R.id.txtTinhTrang_Detail);
		txt_info_Detail = (TextView) findViewById(R.id.txt_info_Detail);
		txt_contact_Detail = (TextView) findViewById(R.id.txt_contact_Detail);
		txtPrice_Detail = (TextView) findViewById(R.id.txtPrice_Detail);
		txt_titleDetaile = (TextView) findViewById(R.id.txt_titleDetaile);
		imgback = (ImageView) findViewById(R.id.imgBackTitle);
		imgback.setOnClickListener(this);
		btnChatDirectly_Detail.setOnClickListener(this);
		btnErrorReport.setOnClickListener(this);
		btnProducSave.setOnClickListener(this);
		btnSendSMS_Detail.setOnClickListener(this);
		btnCall.setOnClickListener(this);
		btnPoster.setOnClickListener(this);
	}

	private int index = 0;

//	private void autoChange() {
//		new Timer().schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						index += 1;
//						index %= fragments.size();
//						mIndicator.setCurrentItem(index);
//					}
//				});
//
//			}
//		}, 5000, 5000);
//
//	}


	@Override
	public void onClick(View v) {
		String phone = objproductDetail.getPhone();
		Debug.e("pHONEaaaaaaaaaaaa: " + phone);
		switch (v.getId()) {
		case R.id.btnSendSMS_Detail:
			intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
			startActivity(intent);
			break;

		case R.id.btnCall_Detail:
			intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
			startActivity(intent);
			break;
		case R.id.btnPoster_Detail:
			PosterDetailActivity.statutActivityCall = 1;
			startActivity(new Intent(ProductDetailActivity.this, PosterDetailActivity.class));
			finish();
			break;
		case R.id.btnSave_Detail:
			if (SystemConfig.user_id.isEmpty() && SystemConfig.session_id.isEmpty()) {
				startActivity(new Intent(ProductDetailActivity.this, LoginActivity.class));
			} else {
//				if(!objproductDetail.isProduct_saved())
				new ProductSaveAndReportAsynTask().execute(SystemConfig.statusProductSave);
			}
			break;
		case R.id.btnChatDirectly_Detail:
			if(!SystemConfig.session_id.isEmpty()){
				ChatDialog chat = new ChatDialog(this);
				chat.run(SystemConfig.statusGetHistoryMessage,objproductDetail.getUser_id());
				chat.dialogChat(objproductDetail.getUser_id());
			}else {
				Message msg = new Message(ProductDetailActivity.this);
				msg.showMessage("Bạn phải đăng nhập!");
			}
			break;

		case R.id.btnReportViolations_Detail:
			if (SystemConfig.user_id.isEmpty() && SystemConfig.session_id.isEmpty()) {
				startActivity(new Intent(ProductDetailActivity.this, LoginActivity.class));
			} else {
				dialogErrorReport();
			}
			break;
		case R.id.imgBackTitle:
			finish();
			break;
		}

	}

	public void dialogErrorReport() {
		dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_errorreport);
		dialog.setTitle("Thông Báo");
		editErrorReport = (EditText) dialog.findViewById(R.id.editErrorReport);
		btnOk = (Button) dialog.findViewById(R.id.btn_Ok_ErrorReport);
		btnCancle = (Button) dialog.findViewById(R.id.btn_Cancle_ErrorReport);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new ProductSaveAndReportAsynTask().execute(SystemConfig.statusErrorReport);
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

	public class ProductSaveAndReportAsynTask extends AsyncTask<Integer, String, OutputProduct> {

		JsonProduct jsonProduct;
		AQuery aQuery = null;
		int status;

		@Override
		protected void onPreExecute() {
			jsonProduct = new JsonProduct();
			aQuery = new AQuery(ProductDetailActivity.this);
			progressDialog = new ProgressDialog(ProductDetailActivity.this);
			// Set progressdialog message
			progressDialog.setMessage("Loading...");
			progressDialog.setIndeterminate(false);
			// Show progressdialog
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected OutputProduct doInBackground(Integer... params) {
			status = params[0];
			switch (params[0]) {
			case SystemConfig.statusProductSave:
				out = jsonProduct.paserNews(SystemConfig.user_id, SystemConfig.session_id, SystemConfig.device_id,
						objproductDetail.getId());
				break;

			case SystemConfig.statusErrorReport:
				out = jsonProduct.paserErrorReport(SystemConfig.user_id, SystemConfig.session_id,
						SystemConfig.device_id, objproductDetail.getId(), editErrorReport.getText().toString());
				break;
			case SystemConfig.statusProductDetail:
				out = jsonProduct.paserProductDetail(SystemConfig.user_id, SystemConfig.session_id,
						SystemConfig.device_id, id_product);
				ProductDetailActivity.objproductDetail = out.getProductVO().get(0);
				break;
			}

			return out;
		}

		@Override
		protected void onPostExecute(OutputProduct result) {
			progressDialog.dismiss();
			if (result.getCode() == Constan.getIntProperty("success")) {
				if (status == SystemConfig.statusProductDetail) {
					objproductDetail = result.getProductVO().get(0);
					Debug.e("chi tiet sp:" + objproductDetail.getName());
					txt_titleDetaile.setText(objproductDetail.getName());
					txtPrice_Detail.setText(objproductDetail.getPrice());
					txtName_Detail.setText(objproductDetail.getUser_name());
					txtDatime_Detail.setText(objproductDetail.getStartdate());
					txtAdd_Detail.setText(objproductDetail.getCity_name());
					txtCategory_Detail.setText(objproductDetail.getCategory_name());
					txtTinhTrang_Detail.setText(objproductDetail.getType_name());
					txt_info_Detail.setText(objproductDetail.getDescription());
					txt_contact_Detail.setText(objproductDetail.getAddress());
					Bitmap bitmap = aQuery.getCachedImage(objproductDetail.getUser_avatar());
					if (bitmap != null) {
						bitmap = Util.getCroppedBitmap(bitmap);
						aQuery.id(btnPoster).image(bitmap);
					} else {
						aQuery.id(btnPoster).image(objproductDetail.getUser_avatar(), true, true, 0,
								R.drawable.ic_launcher);
					}
					fragments = new ArrayList<Fragment>();
					List<String> image = new ArrayList<String>();
					for (int i = 0; i < objproductDetail.getArrImageDetail().size(); i++) {
						image.add(objproductDetail.getArrImageDetail().get(i));
						fragments.add(new FragmentProductDetail(image.get(i)));
					}
					Debug.e("Link Anhaaaaaaa: " + objproductDetail.getArrImageDetail());
					ProductDetailAdapter mAdapter = new ProductDetailAdapter(getSupportFragmentManager(), fragments);
					mPager.setAdapter(mAdapter);
					mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
					mIndicator.setViewPager(mPager);
					//autoChange();
					Debug.e("objproductDetail.isCheck(): " + objproductDetail.isCheck());
					if (objproductDetail.isCheck()) {
						btnCall.setClickable(false);
						btnSendSMS_Detail.setClickable(false);
						btnChatDirectly_Detail.setClickable(false);
						btnCall.setImageResource(R.drawable.call_hidden);
						btnSendSMS_Detail.setImageResource(R.drawable.sms_hidden);
						btnChatDirectly_Detail.setImageResource(R.drawable.chat_hidden);
					} else if (objproductDetail.isCheck() == false) {
						btnCall.setClickable(true);
						btnSendSMS_Detail.setClickable(true);
						btnChatDirectly_Detail.setClickable(true);
					}
					if (objproductDetail.isProduct_saved()) {
						btnProducSave.setImageResource(R.drawable.icon_save_detail_true);;
						btnProducSave.setClickable(false);
					} else
						btnProducSave.setClickable(true);

				}else if(status == SystemConfig.statusProductSave){
					btnProducSave.setClickable(false);
					btnProducSave.setImageResource(R.drawable.icon_save_detail_true);
				}
			}else{
				Message msg = new Message(ProductDetailActivity.this);
				msg.showMessage(result.getMessage());
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
}