package com.onlinemarketing.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.logingoogle.GPSTracker;
import com.onlinemarketing.logingoogle.LocationAddress;
import com.onlinemarketing.object.CategoryVO;
import com.onlinemarketing.object.TypeProductVO;
import com.onlinemarketing.util.CallWSAsynsHttp;
import com.onlinemarketing.util.Util;

import android.R.bool;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.drm.DrmStore.Action;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PostActivity extends BaseActivity implements OnClickListener {
	ProgressDialog prgDialog;
	ImageView imgCamrePost,imgLocalPost;
	TextView edit_TitlePost, edit_PricePost, edit_AddPost, edit_DescripPost;
	Spinner spnCategoryPost, spnMenuPost;
	Button btnpost;
	JSONObject json;
	List<String> arrImgFromCamere;
	private ArrayList<String> imagesPathList;
	private Bitmap yourbitmap;
	private Bitmap resized;
	private final int PICK_IMAGE_MULTIPLE = 1;
	private LinearLayout lnrImages;
	private Uri fileUri;
	String imageEncoded;    
	private List<CategoryVO> listCategory;
	private List<TypeProductVO> listType;
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	double lag,log;
	GPSTracker gps;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		setData();

	}

	public void setData() {
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("Please wait...");
		prgDialog.setCancelable(false);
		edit_TitlePost = (TextView) findViewById(R.id.edit_TitlePost);
		edit_PricePost = (TextView) findViewById(R.id.edit_PricePost);
		edit_AddPost = (TextView) findViewById(R.id.edit_AddPost);
		edit_DescripPost = (TextView) findViewById(R.id.edit_DescripPost);
		spnCategoryPost = (Spinner) findViewById(R.id.spnCategoryPost);
		spnMenuPost = (Spinner) findViewById(R.id.spnMenuPost);
		imgCamrePost = (ImageView) findViewById(R.id.imgCamrePost);
		lnrImages = (LinearLayout) findViewById(R.id.lnrImages);
		btnpost = (Button) findViewById(R.id.btnpost);
		imgLocalPost = (ImageView) findViewById(R.id.imgLocalPost);
		imgLocalPost.setOnClickListener(this);
		imgCamrePost.setOnClickListener(this);
		btnpost.setOnClickListener(this);
		
		 listCategory = new ArrayList<CategoryVO>();
		 listType = new ArrayList<TypeProductVO>();
		loadDataToForm();

	}

	public void loadDataToForm() {
		CallWSAsynsHttp callWS = new CallWSAsynsHttp(this, SystemConfig.API + SystemConfig.Post_product,
				SystemConfig.httpget);
		RequestParams params = new RequestParams();
		params.put("user_id", SystemConfig.user_id);
		params.put("session_id", SystemConfig.session_id);
		params.put("device_id", SystemConfig.device_id);
		invokeWSGet(params);

	}

	public void invokeWSGet(RequestParams params) {
		// TODO Auto-generated method stub
		// Show Progress Dialog
		prgDialog.show();
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(SystemConfig.API + SystemConfig.Post_product, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					List<CategoryVO> lstCategoryVO = new ArrayList<CategoryVO>();
					// JSON Object
					JSONObject json = new JSONObject(response);
					if (json.getInt("code") == 200) {
						JSONObject jsondata = json.getJSONObject("data");
						JSONArray jsonCatagoryArr = jsondata.getJSONArray("categoryArray");
						JSONArray jsonTypeArr = jsondata.getJSONArray("typeArray");
						String[] title = new String[jsonCatagoryArr.length()];
						String[] type = new String[jsonTypeArr.length()];
						for (int i = 0; i < jsonCatagoryArr.length(); i++) {
							JSONObject jsonCategory = jsonCatagoryArr.getJSONObject(i);
							CategoryVO objcategory = new CategoryVO();
							objcategory.setId(jsonCategory.getInt("id"));
							objcategory.setName(jsonCategory.getString("name"));
							title[i] = jsonCategory.getString("name");
							listCategory.add(objcategory);
						}
						spnCategoryPost.setAdapter(new ArrayAdapter<String>(PostActivity.this, R.layout.item_spinersearch, title));
						for (int i = 0; i < jsonTypeArr.length(); i++) {
							JSONObject jsonType = jsonTypeArr.getJSONObject(i);
							TypeProductVO objTypeVO = new TypeProductVO();
							objTypeVO.setId(jsonType.getInt("type_id"));
							objTypeVO.setName(jsonType.getString("type_name"));
							type[i] = jsonType.getString("type_name");
							listType.add(objTypeVO);
						}
						spnMenuPost.setAdapter(new ArrayAdapter<String>(PostActivity.this, R.layout.item_spinersearch, type));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Throwable error, String content) {
				prgDialog.hide();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		 if (requestCode == PICK_IMAGE_MULTIPLE) {
		        if (resultCode == RESULT_OK) {
		            // Image captured and saved to fileUri specified in the Intent
		            Toast.makeText(this, "Image saved to:\n" +
		                     data.getData(), Toast.LENGTH_LONG).show();
		            Uri uri= data.getData();
		           String linkFromCamera=  getPicturePath(uri);
		           arrImgFromCamere.add(linkFromCamera);
		        } else if (resultCode == RESULT_CANCELED) {
		            // User cancelled the image capture
		        } else {
		            // Image capture failed, advise user
		        }
		 }
		
	}
	public String getPicturePath(Uri uriImage) {
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uriImage, filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String path = cursor.getString(columnIndex);
		cursor.close();
		return path;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgCamrePost:
//			intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
			Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			startActivityForResult(i, PICK_IMAGE_MULTIPLE);
			break;

		case R.id.btnpost:
			PostProduct();
			break;
		case R.id.imgLocalPost:

			
			  gps = new GPSTracker(PostActivity.this);

				// check if GPS enabled		
		        if(gps.canGetLocation()){
		        	
		        	lag = gps.getLatitude();
		        	log = gps.getLongitude();
		        	// \n is for new line
		        	Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lag + "\nLong: " + log, Toast.LENGTH_LONG).show();
		        	  LocationAddress locationAddress = new LocationAddress();
	                    locationAddress.getAddressFromLocation(lag, log,
	                            getApplicationContext(), new GeocoderHandler());
		        }else{
		        	// can't get location
		        	// GPS or Network is not enabled
		        	// Ask user to enable GPS/network in settings
		        	gps.showSettingsAlert();
		        }
			
			break;
		}
	}
	public void PostProduct(){
		
		boolean checkform = checkform();
		if(checkform){
			
		}
	}
	public boolean checkform(){
		boolean tmp = false;
		String title = edit_TitlePost.getText().toString();
		int id_category = listCategory.get(spnCategoryPost.getSelectedItemPosition()).getId();
		int id_type = listType.get(spnMenuPost.getSelectedItemPosition()).getId();
		String price  = edit_PricePost.getText().toString();
		String address =  edit_AddPost.getText().toString();
		String description =  edit_DescripPost.getText().toString();
		if(!Util.isNotNull(title))
		{
			edit_TitlePost.setError( "Bạn phải nhập tiêu đề!" );
			tmp =  false;
		}
		else if(id_category == 0)
		{
			((TextView)spnCategoryPost.getSelectedView()).setError("bạn phải chọn danh mục!");
			tmp =  false;
		}
		else if(id_type == 0)
		{
			((TextView)spnMenuPost.getSelectedView()).setError("bạn phải tình trạng sản phẩm!");
			tmp =  false;
		}
		else if(!Util.isNotNull(price))
		{
			edit_PricePost.setError( "Bạn phải nhập giá!" );
			tmp =  false;
		}
		else if(!Util.isNotNull(address))
		{
			edit_AddPost.setError( "Bạn phải nhập địa chỉ!" );
			tmp =  false;
		}
		else if(!Util.isNotNull(description)){
			edit_DescripPost.setError( "Bạn phải nhập mô tả!" );
			tmp =  false;
		}
		else {
			tmp = true;
		}
		return tmp;
	}

	
	 private class GeocoderHandler extends Handler {
	        @Override
	        public void handleMessage(Message message) {
	            String locationAddress;
	            switch (message.what) {
	                case 1:
	                    Bundle bundle = message.getData();
	                    locationAddress = bundle.getString("address");
	                    break;
	                default:
	                    locationAddress = null;
	            }
	            edit_AddPost.setText(locationAddress);
	        }
	    }

}
