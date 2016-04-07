package com.onlinemarketing.activity;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.onlinemarketing.activity.ProductDetailActivity.ProductSaveAndReportAsynTask;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonProfile;
import com.onlinemarketing.object.CategoryVO;
import com.onlinemarketing.object.CityVO;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.ProfileVO;
import com.onlinemarketing.object.TypeProductVO;
import com.onlinemarketing.util.Message;
import com.onlinemarketing.util.Util;

public class ProfileActivity extends BaseActivity implements OnClickListener {

	private int PICK_IMAGE = 0;
	private Bitmap bitmap;
	ProfileVO profile = new ProfileVO();
	Output out;
	ImageView imgAvatar;
	EditText editName, editPhone, editAdd, editPass, editConfigPass,editErrorReport;
	Button btnSave, btnBacklist,btnOk,btnCancle;
	TextView btnApprovePhone, editMail;
	Uri selectedUriImage;
	String picturePath;
	Bitmap selectedBitmap;
	private Uri fileUri;
	private AQuery aQuery;
	ProgressDialog prgDialog;
	Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_profile);
		aQuery = new AQuery(this);
		super.onCreate(savedInstanceState);
		imgAvatar = (ImageView) findViewById(R.id.imgAvatar_profile);
		editName = (EditText) findViewById(R.id.editName_profile);
		editMail = (TextView) findViewById(R.id.editEmail_profile);
		editAdd = (EditText) findViewById(R.id.editAddress_profile);
		editPass = (EditText) findViewById(R.id.editPass_profile);
		editConfigPass = (EditText) findViewById(R.id.editConfigPass_profile);
		editPhone = (EditText) findViewById(R.id.editPhone_profile);
		btnApprovePhone = (TextView) findViewById(R.id.btnApprovePhone_profile);
		btnSave = (Button) findViewById(R.id.btnSave_profile);
		// imgAvatar = (ImageView) findViewById(R.id.imgAvatar_profile);
		btnBacklist = (Button) findViewById(R.id.btnBackList);
		btnBacklist.setOnClickListener(this);
		imgAvatar.setOnClickListener(this);
		btnApprovePhone.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		setData(SystemConfig.oOputproduct.getProfileVO());
	}

	public void setData(ArrayList<ProfileVO> proVo) {
		ProfileVO obj = proVo.get(0);
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("Please wait...");
		prgDialog.setCancelable(false);

		Bitmap bitmap = aQuery.getCachedImage(obj.getAvatar());
		if (bitmap != null) {
			bitmap = Util.getCroppedBitmap(bitmap);
			aQuery.id(imgAvatar).image(bitmap);
		} else {
			aQuery.id(imgAvatar).image(obj.getAvatar(), true, true, 0, R.drawable.ic_launcher);
		}
		editName.setText(obj.getUsername());
		editPhone.setText(obj.getPhone());
		editMail.setText(obj.getEmail());
		editAdd.setText(obj.getAddress());
	}

	private Drawable LoadImageFromWebOperations(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "");
			return d;
		} catch (Exception e) {
			System.out.println("Exc=" + e);
			return null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSave_profile:
			profile.setUsername(editName.getText().toString());
			profile.setPhone(editPhone.getText().toString());
			profile.setEmail(editMail.getText().toString());
			profile.setAddress(editAdd.getText().toString());
			profile.setPass(editPass.getText().toString());
			profile.setOld_pass(editConfigPass.getText().toString());
			if(selectedBitmap != null)
				profile.setAvatar(SystemConfig.Avatar);
			else{
				String[] imagename =SystemConfig.oOputproduct.getProfileVO().get(0).getAvatar().split("/");
				profile.setAvatar(imagename[imagename.length - 1]);
			}
			new UpdateAsystask().execute(Constan.getIntProperty("status_update_profile"));
			break;

		case R.id.btnApprovePhone_profile:
//			dialogApprovePhone();
			RequestParams params = new RequestParams();
			params.put("user_id", SystemConfig.user_id);
			params.put("device_id", SystemConfig.device_id);
			params.put("session_id", SystemConfig.session_id);
			Message msg = new Message(this);
			if(!editPhone.getText().toString().isEmpty() && !editPhone.getText().toString().equals("null"))
				if(Util.isValidPhoneNo(editPhone.getText().toString()))
					verifyAccount(params);
				else
					msg.showMessage(Constan.getProperty("Error05"));	
			else 
				msg.showMessage(Constan.getProperty("Error04"));
			break;
		case R.id.btnBackList:
			startActivity(new Intent(ProfileActivity.this, BackListActivity.class));
			break;
		case R.id.imgAvatar_profile:
			getGallery();
			break;
		}
	}

	public ContentResolver getGallery() {
		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(i, PICK_IMAGE);
		return null;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
			selectedUriImage = data.getData();
			picturePath = getPicturePath(selectedUriImage);
			selectedBitmap = getThumbnail(picturePath);
			selectedBitmap = rotateImageIfRequired(selectedBitmap, selectedUriImage);
//			selectedBitmap = Util.getCroppedBitmap(selectedBitmap);
			new UpdateAsystask().execute(Constan.getIntProperty("status_upload_avatar"));

		}

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

	private Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) {
		// Detect rotation
		int rotation = getRotation();
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

	private int getRotation() {
		String[] filePathColumn = { MediaStore.Images.Media.ORIENTATION };
		Cursor cursor = getContentResolver().query(selectedUriImage, filePathColumn, null, null, null);
		cursor.moveToFirst();

		int rotation = 0;
		rotation = cursor.getInt(0);
		cursor.close();
		return rotation;
	}

	public class UpdateAsystask extends AsyncTask<Integer, String, Output> {

		JsonProfile jsonProfile;

		@Override
		protected void onPreExecute() {
			jsonProfile = new JsonProfile();
			 prgDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Output doInBackground(Integer... params) {
			if (params[0] == Constan.getIntProperty("status_upload_avatar")) {
				out = jsonProfile.doFileUpload(SystemConfig.user_id, SystemConfig.session_id, SystemConfig.device_id,
						picturePath,selectedBitmap);
			} else {
				out = jsonProfile.postPaserProfile(SystemConfig.user_id, SystemConfig.session_id,
						SystemConfig.device_id, profile);
			}
			return out;
		}

		@Override
		protected void onPostExecute(Output result) {
			prgDialog.dismiss();
			if (result.getCode() == Constan.getIntProperty("success")) {
				Debug.showAlert(ProfileActivity.this, result.getMessage());
				Bitmap bit = selectedBitmap;
				if (bit != null) {
					bit = Util.getCroppedBitmap(bit);
					imgAvatar.setImageBitmap(bit);
				}else {
					Bitmap bitmap = aQuery.getCachedImage(SystemConfig.oOputproduct.getProfileVO().get(0).getAvatar());
					if (bitmap != null) {
						bitmap = Util.getCroppedBitmap(bitmap);
						aQuery.id(imgAvatar).image(bitmap);
					}
					else {
						aQuery.id(imgAvatar).image(SystemConfig.oOputproduct.getProfileVO().get(0).getAvatar(), true, true, 0, R.drawable.ic_launcher);
					}
				}
			}
			super.onPostExecute(result);
		}

	}
	
	/**
	 * ham nay load cac thong tin tren form tao moi san pham
	 * 
	 * @param params
	 */
	public void verifyAccount(RequestParams params) {
		// TODO Auto-generated method stub
		// Show Progress Dialog
		prgDialog.show();
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(SystemConfig.API + SystemConfig.Verify_phone, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				// Hide Progress Dialog
				prgDialog.hide();
				try {
					List<CategoryVO> lstCategoryVO = new ArrayList<CategoryVO>();
					// JSON Object
					JSONObject json = new JSONObject(response);
					if (json.getInt("code") == 200) {
						Message msg = new Message(ProfileActivity.this);
						msg.showMessage(json.getString("message"));
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

	public void dialogApprovePhone() {
		dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_forgot_pass);
		TextView title = (TextView) dialog.findViewById(R.id.textviewTitle);
		EditText code = (EditText) dialog.findViewById(R.id.editErrorReport);
		title.setText("Nhập mã code!");
		code.setHint("Mã xác nhận");;
		editErrorReport = (EditText) dialog.findViewById(R.id.editErrorReport);
		btnOk = (Button) dialog.findViewById(R.id.btn_Ok_ErrorReport);
		btnCancle = (Button) dialog.findViewById(R.id.btn_Cancle_ErrorReport);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				new ProductSaveAndReportAsynTask().execute(SystemConfig.statusErrorReport);
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
}
