package com.onlinemarketing.fragment;

import java.util.ArrayList;

import com.example.onlinemarketing.R;
import com.onlinemarketing.activity.FavoriteActivity;
import com.onlinemarketing.activity.LoginActivity;
import com.onlinemarketing.activity.MainActivity;
import com.onlinemarketing.activity.PostActivity;
import com.onlinemarketing.activity.ProductDetailActivity;
import com.onlinemarketing.activity.ProfileActivity;
import com.onlinemarketing.activity.SaveNewsListActivity;
import com.onlinemarketing.adapter.HomePageAdapter;
import com.onlinemarketing.adapter.ListMessageAdapter;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonProduct;
import com.onlinemarketing.json.JsonProfile;
import com.onlinemarketing.object.MessageVO;
import com.onlinemarketing.object.OutputMessage;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.ProductVO;
import com.onlinemarketing.object.ProfileVO;
import com.onlinemarketing.util.ChatDialog;
import com.smile.android.gsm.utils.AndroidUtils;
import com.smile.studio.menu.FragmentDrawerLeft;
import com.smile.studio.menu.FragmentDrawerRight;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

public class FragmentCategory extends Fragment implements OnItemClickListener,
		OnClickListener {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	ListView listview, listviewChat;
	HomePageAdapter adapter;
	ListMessageAdapter adapterListMessage;
	ArrayList<ProductVO> list = new ArrayList<ProductVO>();
	Context context;
	View rootView;
	int status;
	static Dialog dialog;
	static Dialog dialogListMsg;
	Button btn_SMS, btnListChat, btnSend;
	EditText editMessage, editSendMessage;
	ProgressDialog progressDialog;
	Button btnHome, btnChat, btnFavorite, btnProfile;
	TextView txtShowMessageChat ,txtShowToast;
	TableLayout tab;
	public static OutputMessage oOputMsg;
	ArrayList<MessageVO> listMessage = new ArrayList<MessageVO>();
	ImageView imgPost;
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static FragmentCategory newInstance(int sectionNumber) {
		FragmentCategory fragment = new FragmentCategory();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_home_page, container,
				false);
		context = rootView.getContext();
		btnHome = (Button) rootView.findViewById(R.id.btnHome_FragmentCategory);
		btnChat = (Button) rootView.findViewById(R.id.btnChat_FragmentCategory);
		btnFavorite = (Button) rootView
				.findViewById(R.id.btnFavorite_FragmentCategory);
		btnProfile = (Button) rootView
				.findViewById(R.id.btnProfile_FragmentCategory);
		listview = (ListView) rootView.findViewById(R.id.listHomePage);
		imgPost = (ImageView) rootView.findViewById(R.id.imgPostHomepage);
		txtShowToast = (TextView) rootView.findViewById(R.id.txt_showToast);
		imgPost.setOnClickListener(this);
		listview.setOnItemClickListener(this);
		btnHome.setOnClickListener(this);
		btnChat.setOnClickListener(this);
		btnFavorite.setOnClickListener(this);
		btnProfile.setOnClickListener(this);
		new HomeAsystask().execute(MainActivity.status);
		return rootView;
	}
	public void callAsystask(int status, Context contectcall){
		if(status == SystemConfig.statusListSaveProduct){
			context = contectcall;
			this.status = status;
			new HomeAsystask().execute(status);
		}
	}

	public class HomeAsystask extends
			AsyncTask<Integer, Integer, OutputProduct> {
		String Device_id;
		JsonProduct product;

		public HomeAsystask() {
			super();
		}

		@Override
		protected void onPreExecute() {
			product = new JsonProduct();
			
			progressDialog = new ProgressDialog(context);
			// Set progressdialog message
			progressDialog.setMessage("Loading...");
			progressDialog.setIndeterminate(false);
			// Show progressdialog
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected OutputProduct doInBackground(Integer... params) {
			switch (params[0]) {
			case SystemConfig.statusHomeProduct:
				MainActivity.oOput = product.paserProduct(
						SystemConfig.user_id, SystemConfig.session_id,
						SystemConfig.device_id, 0,
						SystemConfig.statusHomeProduct);
				break;
			case SystemConfig.statusCategoryProduct:
				MainActivity.oOput = product.paserProduct(
						SystemConfig.user_id, SystemConfig.session_id,
						SystemConfig.device_id, MainActivity.id_category,
						SystemConfig.statusCategoryProduct);
				break;
			case SystemConfig.statusListSaveProduct:
				MainActivity.oOput = product.paserProduct(
						SystemConfig.user_id, SystemConfig.session_id,
						SystemConfig.device_id, MainActivity.id_category,
						SystemConfig.statusListSaveProduct);

				break;
			}

			list = MainActivity.oOput.getProductVO();
			SystemConfig.oOputproduct.setProductVO(list);
			return MainActivity.oOput;
		}

		@Override
		protected void onPostExecute(OutputProduct result) {
			
			
			if (list.size() > 0) {
				if (result.getCode() == Constan.getIntProperty("success")
						&& status == SystemConfig.statusListSaveProduct) {
					startActivity(new Intent(context, SaveNewsListActivity.class));
				}else {
				adapter = new HomePageAdapter(context, R.layout.item_trang_chu,
						list);
				listview.setAdapter(adapter);
				}
			}else {
				listview.setVisibility(View.GONE);
				txtShowToast.setText(Constan.getProperty("Error06"));
				txtShowToast.setVisibility(View.VISIBLE);
			}
			
			
			progressDialog.dismiss();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ProductDetailActivity.id_product = list.get(arg2).getId();
		startActivity(new Intent(context, ProductDetailActivity.class));

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnHome_FragmentCategory:
			if (AndroidUtils.isConnectedToInternet(context)) {
				new HomeAsystask().execute(SystemConfig.statusHomeProduct);
			}
			break;

		case R.id.btnChat_FragmentCategory:
			
			if(!SystemConfig.session_id.isEmpty()){
				if (AndroidUtils.isConnectedToInternet(context)) {
					ChatDialog chat = new ChatDialog(context);
					chat.run(SystemConfig.statusListMessage);
				}
			}
			else{
				startActivity(new Intent(context, LoginActivity.class));
			}
			break;
		case R.id.btnFavorite_FragmentCategory:
			status = SystemConfig.statusFavorite;
			new getProfileAndFavoriteAsystask()
					.execute(SystemConfig.statusFavorite);
			break;
		case R.id.btnProfile_FragmentCategory:
			status = SystemConfig.statusProfile;
			new getProfileAndFavoriteAsystask()
					.execute(SystemConfig.statusProfile);
			break;
		case R.id.imgPostHomepage:
			if(!SystemConfig.session_id.isEmpty())
				startActivity(new Intent(context, PostActivity.class));
			else 
				startActivity(new Intent(context, LoginActivity.class));
			break;
		}
		
	}	

	public class getProfileAndFavoriteAsystask extends
			AsyncTask<Integer, String, OutputProduct> {
		JsonProfile profile;
		ArrayList<ProfileVO> listProfile = new ArrayList<ProfileVO>();

		@Override
		protected void onPreExecute() {
			profile = new JsonProfile();
			super.onPreExecute();
		}

		@Override
		protected OutputProduct doInBackground(Integer... params) {
			switch (params[0]) {
			case SystemConfig.statusProfile:
				MainActivity.oOput = profile.paserProfile(
						SystemConfig.user_id, SystemConfig.session_id,
						SystemConfig.device_id, SystemConfig.statusProfile);
				listProfile = MainActivity.oOput.getProfileVO();
				SystemConfig.oOputproduct.setProfileVO(listProfile);
				break;

			case SystemConfig.statusFavorite:
				MainActivity.oOput = profile.paserProfile(
						SystemConfig.user_id, SystemConfig.session_id,
						SystemConfig.device_id, SystemConfig.statusFavorite);
				listProfile = MainActivity.oOput.getProfileVO();
				SystemConfig.oOputproduct.setProfileVO(listProfile);
				break;
			}
			return MainActivity.oOput;
		}

		@Override
		protected void onPostExecute(OutputProduct result) {
			if (result.getCode() == Constan.getIntProperty("success")
					&& status == SystemConfig.statusProfile) {
				startActivity(new Intent(context, ProfileActivity.class));

			} else if (result.getCode() == Constan.getIntProperty("success")
					&& status == SystemConfig.statusFavorite) {
				FavoriteActivity.listProfile = listProfile;
				startActivity(new Intent(context, FavoriteActivity.class));
			} else {
				startActivity(new Intent(context, LoginActivity.class));
			}
			super.onPostExecute(result);
		}
	}


}