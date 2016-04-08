package com.onlinemarketing.activity;

import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.onlinemarketing.adapter.PostRecycleAdapter;
import com.onlinemarketing.asystask.UploadProduct;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonProduct;
import com.onlinemarketing.json.JsonProfile;
import com.onlinemarketing.logingoogle.Dialog;
import com.onlinemarketing.logingoogle.GPSTracker;
import com.onlinemarketing.logingoogle.LocationAddress;
import com.onlinemarketing.object.CategoryVO;
import com.onlinemarketing.object.CityVO;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.OutputGoogle;
import com.onlinemarketing.object.TypeProductVO;
import com.onlinemarketing.util.CallWSAsynsHttp;
import com.onlinemarketing.util.Util;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author tuanc_000
 *
 */
public class PostActivity extends BaseActivity implements OnClickListener {
	ProgressDialog prgDialog;
	ImageView imgCamrePost, imgLocalPost, img_25Post, img_50Post, img_75Post, imgBack;
	EditText edit_TitlePost, edit_PricePost, edit_AddPost, edit_DescripPost;
	Spinner spnCategoryPost, spnMenuPost, spnCityPost;
	Button btnpost;
	JSONObject json;
	static String imagePart;
	public static List<String> arrImgFromCamere;
	List<Bitmap> arrImgFromCamereBitmap;
	private final int PICK_IMAGE_MULTIPLE = 1;
	private LinearLayout lnrImages;
	private Uri fileUri;
	String imageEncoded;
	private List<CategoryVO> listCategory;
	private List<TypeProductVO> listType;
	private List<CityVO> listCity;
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	String lag, log = "";
	GPSTracker gps;
	Output out;
	OutputGoogle outputgoogle;
	static String title, price, address, description="";
	static int id_category, id_type, city_id;
	private RecyclerView mRecyclerView;
	private PostRecycleAdapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;
	Bitmap bit = null;
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
		edit_TitlePost = (EditText) findViewById(R.id.edit_TitlePost);
		edit_PricePost = (EditText) findViewById(R.id.edit_PricePost);
		edit_AddPost = (EditText) findViewById(R.id.edit_AddPost);
		edit_DescripPost = (EditText) findViewById(R.id.edit_DescripPost);
		spnCategoryPost = (Spinner) findViewById(R.id.spnCategoryPost);
		spnMenuPost = (Spinner) findViewById(R.id.spnMenuPost);
		spnCityPost = (Spinner) findViewById(R.id.spnCityPost);
		imgCamrePost = (ImageView) findViewById(R.id.imgCamrePost);
		lnrImages = (LinearLayout) findViewById(R.id.lnrImages);
		btnpost = (Button) findViewById(R.id.btnpost);
		imgLocalPost = (ImageView) findViewById(R.id.imgLocalPost);
		img_25Post = (ImageView) findViewById(R.id.img_25Post);
		img_50Post = (ImageView) findViewById(R.id.img_50Post);
		img_75Post = (ImageView) findViewById(R.id.img_75Post);
		imgBack = (ImageView) findViewById(R.id.imgBackTitle);
		imgLocalPost.setOnClickListener(this);
		imgCamrePost.setOnClickListener(this);
		btnpost.setOnClickListener(this);
		imgBack.setOnClickListener(this);
		edit_PricePost.setOnClickListener(this);
		edit_DescripPost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!edit_PricePost.getText().toString().isEmpty())
					img_75Post.setImageResource(R.drawable.icon_75_success);
			}
		});
		listCategory = new ArrayList<CategoryVO>();
		listType = new ArrayList<TypeProductVO>();
		listCity = new ArrayList<CityVO>();
		arrImgFromCamere = new ArrayList<String>();
		arrImgFromCamereBitmap = new ArrayList<Bitmap>();
		CallWSAsynsHttp callWS = new CallWSAsynsHttp(this, SystemConfig.API + SystemConfig.Post_product,
				SystemConfig.httpget);
		RequestParams params = new RequestParams();
		params.put("user_id", SystemConfig.user_id);
		params.put("session_id", SystemConfig.session_id);
		params.put("device_id", SystemConfig.device_id);
		invokeWSGet(params);

	}

	/**
	 * ham nay load cac thong tin tren form tao moi san pham
	 * 
	 * @param params
	 */
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
						JSONArray jsonCityArr = jsondata.getJSONArray("cityArray");
						String[] title = new String[jsonCatagoryArr.length()];
						String[] type = new String[jsonTypeArr.length()];
						String[] city = new String[jsonCityArr.length()];
						for (int i = 0; i < jsonCatagoryArr.length(); i++) {
							JSONObject jsonCategory = jsonCatagoryArr.getJSONObject(i);
							CategoryVO objcategory = new CategoryVO();
							objcategory.setId(jsonCategory.getInt("id"));
							objcategory.setName(jsonCategory.getString("name"));
							title[i] = jsonCategory.getString("name");
							listCategory.add(objcategory);
						}
						spnCategoryPost.setAdapter(
								new ArrayAdapter<String>(PostActivity.this, R.layout.item_spinersearch, title));
						for (int i = 0; i < jsonTypeArr.length(); i++) {
							JSONObject jsonType = jsonTypeArr.getJSONObject(i);
							TypeProductVO objTypeVO = new TypeProductVO();
							objTypeVO.setId(jsonType.getInt("type_id"));
							objTypeVO.setName(jsonType.getString("type_name"));
							type[i] = jsonType.getString("type_name");
							listType.add(objTypeVO);
						}
						spnMenuPost.setAdapter(
								new ArrayAdapter<String>(PostActivity.this, R.layout.item_spinersearch, type));
						for (int i = 0; i < jsonCityArr.length(); i++) {
							JSONObject jsonCity = jsonCityArr.getJSONObject(i);
							CityVO objCity = new CityVO();
							objCity.setId(jsonCity.getInt("city_id"));
							objCity.setName(jsonCity.getString("city_name"));
							city[i] = jsonCity.getString("city_name");
							listCity.add(objCity);
						}
						spnCityPost.setAdapter(new ArrayAdapter<String>(PostActivity.this,R.layout.item_spinersearch, city));
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
		try {
			if (requestCode == PICK_IMAGE_MULTIPLE) {
				if (resultCode == RESULT_OK) {
					// Image captured and saved to fileUri specified in the
					// Intent
					Uri uri = data.getData();
					String linkFromCamera = getPicturePath(uri);
					

					linkFromCamera = getPicturePath(uri);
					bit = getThumbnail(linkFromCamera);
					bit = rotateImageIfRequired(bit, uri);
					
					imagePart = linkFromCamera;
					arrImgFromCamereBitmap.add(bit);
					//resize image
					
					new UpdateAsystask().execute(bit);
				}
			}
		} catch (Exception e) {
			finish();
			startActivity(new Intent(this, PostActivity.class));
		}

	}

	private Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) {
		// Detect rotation
		int rotation = getRotation(selectedImage);
		if (rotation != 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(rotation);
			Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
			img.recycle();
			return rotatedImg;
		} else {
			return img;
		}
	}

	private int getRotation(Uri uri) {
		String[] filePathColumn = { MediaStore.Images.Media.ORIENTATION };
		Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
		cursor.moveToFirst();

		int rotation = 0;
		rotation = cursor.getInt(0);
		cursor.close();
		return rotation;
	}

	public Bitmap getThumbnail(String pathHinh) {
		BitmapFactory.Options bounds = new BitmapFactory.Options();
		bounds.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathHinh, bounds);
		if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
			return null;
		int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight : bounds.outWidth;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = originalSize / 500;
		return BitmapFactory.decodeFile(pathHinh, opts);
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
			// intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
			SystemConfig.productImage = "";
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
			if (gps.canGetLocation()) {

				lag = String.valueOf(gps.getLatitude());
				log = String.valueOf(gps.getLongitude());
				// \n is for new line
				LocationAddress locationAddress = new LocationAddress();
				locationAddress.getAddressFromLocation(Double.parseDouble(lag), Double.parseDouble(log),
						getApplicationContext(), new GeocoderHandler());
			} else {
				// can't get location
				// GPS or Network is not enabled
				// Ask user to enable GPS/network in settings
				gps.showSettingsAlert();
			}

			break;
		case R.id.imgBackTitle:
			finish();
			break;
		case R.id.edit_PricePost:
			if (!edit_TitlePost.getText().toString().isEmpty() && listCategory.get(spnCategoryPost.getSelectedItemPosition()).getId() != 0 && listType.get(spnMenuPost.getSelectedItemPosition()).getId() != 0)
				img_50Post.setImageResource(R.drawable.icon_50_success);
			break;
		}

	}

	public void PostProduct() {
		title = edit_TitlePost.getText().toString();
		id_category = listCategory.get(spnCategoryPost.getSelectedItemPosition()).getId();
		id_type = listType.get(spnMenuPost.getSelectedItemPosition()).getId();
		city_id = listCity.get(spnCityPost.getSelectedItemPosition()).getId();
		price = edit_PricePost.getText().toString();
		address = edit_AddPost.getText().toString();
		description = edit_DescripPost.getText().toString();
		boolean checkform = checkform(title, id_category, id_type, price, address, description);
		// lay location va post anh luon

		if (checkform) {
			if (arrImgFromCamere.size() == 0) {
				com.onlinemarketing.util.Message msg = new com.onlinemarketing.util.Message(this);
				msg.showMessage(Constan.getProperty("Error14"));
			} else {

				new AsystarkPostProduct().execute();
			}
		}

	}

	public boolean checkform(String title, int id_category, int id_type, String price, String address,
			String description) {
		boolean tmp = false;
		if (!Util.isNotNull(title)) {
			edit_TitlePost.setError(Constan.getProperty("Error07"));
			tmp = false;
		} else if (city_id == 0) {
			((TextView) spnCityPost.getSelectedView()).setError(Constan.getProperty("Error08"));
			tmp = false;
		} else if (id_category == 0) {
			((TextView) spnCategoryPost.getSelectedView()).setError(Constan.getProperty("Error09"));
			tmp = false;
		} else if (id_type == 0) {
			((TextView) spnMenuPost.getSelectedView()).setError(Constan.getProperty("Error10"));
			tmp = false;
		} else if (!Util.isNotNull(price)) {
			edit_PricePost.setError(Constan.getProperty("Error11"));
			tmp = false;
		} else if (!Util.isNotNull(address)) {
			edit_AddPost.setError(Constan.getProperty("Error12"));
			tmp = false;
		} else if (!Util.isNotNull(description)) {
			edit_DescripPost.setError(Constan.getProperty("Error13"));
			tmp = false;
		} else {
			tmp = true;
		}
		return tmp;
	}

	@SuppressLint("HandlerLeak")
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

	/**
	 * Su ly upload anh trong ActivityResult
	 * 
	 * @author tuanc_000
	 *
	 */
	public class UpdateAsystask extends AsyncTask<Bitmap, Void, Output> {

		JsonProduct jsonProfile;

		@Override
		protected void onPreExecute() {
			jsonProfile = new JsonProduct();
			prgDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Output doInBackground(Bitmap... params) {
			Bitmap bitmap = params[0];
			out = jsonProfile.doFileUpload(SystemConfig.user_id, SystemConfig.session_id, SystemConfig.device_id,
					imagePart,bitmap);
			return out;
		}

		@Override
		protected void onPostExecute(Output result) {
			try{
			prgDialog.dismiss();
			if (result.getCode() == Constan.getIntProperty("success")) {
				LinearLayoutManager layoutManager = new LinearLayoutManager(PostActivity.this,
						LinearLayoutManager.HORIZONTAL, false);
				mRecyclerView = (RecyclerView) findViewById(R.id.recyclerPost);
				mRecyclerView.setHasFixedSize(true);
				mLayoutManager = new LinearLayoutManager(PostActivity.this);
				mRecyclerView.setLayoutManager(mLayoutManager);
				mAdapter = new PostRecycleAdapter(PostActivity.this, arrImgFromCamere, arrImgFromCamereBitmap);
				mRecyclerView.setLayoutManager(layoutManager);
				mRecyclerView.setAdapter(mAdapter);
				arrImgFromCamere.add(SystemConfig.productImage);
				img_25Post.setImageResource(R.drawable.icon_25_success);
			}
			super.onPostExecute(result);
			}catch(Exception ex){
				Debug.e(ex.toString());
			}
		}

	}

	/**
	 * su ly upload san pham
	 * 
	 * @author tuanc_000
	 *
	 */
	public class AsystarkPostProduct extends AsyncTask<Void, Void, Output> {
		Context contextasysn;
		UploadProduct upload;
		List<String> linkImgFromGallery;
		Output output;
		JsonProduct jsonProduct;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			jsonProduct = new JsonProduct();
			super.onPreExecute();
		}

		@Override
		protected Output doInBackground(Void... params) {
			try {
				StringBuffer str = new StringBuffer(SystemConfig.linkGetLaglogFromAddress);
				str.append("?address=").append(URLEncoder.encode(address, "UTF-8"));
				str.append("&sensor=").append(false);

				String response = Util.getjSonUrl(str.toString(), SystemConfig.httpget);
				JSONObject obj = new JSONObject(response);
				outputgoogle = LocationAddress.getLatLong(obj);
				// su ly post san pham
				output = jsonProduct.paserPostProduct(SystemConfig.user_id, SystemConfig.session_id,
						SystemConfig.device_id, arrImgFromCamere, arrImgFromCamere.get(0), title,
						String.valueOf(id_category), String.valueOf(id_type), String.valueOf(outputgoogle.getLat()),
						String.valueOf(outputgoogle.getLog()), price, description, city_id, address);

			} catch (Exception e) {
				Debug.e(e.toString());
			}

			return output;
		}

		@Override
		protected void onPostExecute(Output result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result.getCode() == Constan.getIntProperty("success")) {
				startActivity(new Intent(PostActivity.this, MainActivity.class));
			} else {
				com.onlinemarketing.util.Message msg = new com.onlinemarketing.util.Message(PostActivity.this);
				msg.showMessage(result.getMessage());
			}
		}

	}
}
