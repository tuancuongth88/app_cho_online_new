package com.smile.studio.menu;

import java.util.ArrayList;

import com.androidquery.AQuery;
import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.lib.recycler.OnItemTouchListener;
import com.onlinemarketing.activity.ListSaveSearchActivity;
import com.onlinemarketing.activity.LoginActivity;
import com.onlinemarketing.activity.MainActivity;
import com.onlinemarketing.activity.PromotionActivity;
import com.onlinemarketing.activity.RegisterActivity;
import com.onlinemarketing.activity.SaveNewsListActivity;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.fragment.FragmentCategory;
import com.onlinemarketing.json.JsonProfile;
import com.onlinemarketing.json.JsonSetting;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.ProfileVO;
import com.onlinemarketing.object.SettingVO;
import com.onlinemarketing.util.Util;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentDrawerRight extends Fragment implements OnClickListener {

	ArrayList<SettingVO> listSetting = new ArrayList<SettingVO>();
	public static OutputProduct oOput;
	private RecyclerView recyclerView = null;
	private ActionBarDrawerToggle mDrawerToggle = null;
	private DrawerLayout mDrawerLayout = null;
	private NavigationDrawerRightAdapter adapter = null;
	private View containerView = null;
	private FragmentDrawerListener drawerListener = null;
	TextView txtalert, txt_nameNavigaterReight;
	Context context;
	ImageView imgNavigator, imgAvataNavigator;
	static Dialog dialog;
	Button btnOk, btnCancle;
	private AQuery aQuery;

	public FragmentDrawerRight() {

	}

	public void setDrawerListener(FragmentDrawerListener listener) {
		this.drawerListener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new SettingAsystask().execute();
		if (!SystemConfig.session_id.isEmpty()) {
			new getProfileAsystask().execute();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_navigation_drawer_right, container, false);
		context = layout.getContext();
		Constan.context = context;
		recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
		imgNavigator = (ImageView) layout.findViewById(R.id.imgNavigator);
		txt_nameNavigaterReight = (TextView) layout.findViewById(R.id.txt_nameNavigaterReight);
		imgAvataNavigator = (ImageView) layout.findViewById(R.id.imgAvataNavigator);

		imgNavigator.setOnClickListener(this);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.addOnItemTouchListener(
				new OnItemTouchListener(getActivity(), recyclerView, new OnItemTouchListener.ClickListener() {
					@Override
					public void onClick(View view, int position) {
						// drawerListener.onDrawerItemSelected(view, position);
						// su li cac thu menu phai o day
						// load dang ban
						listSetting = SystemConfig.oOputproduct.getSettingVO();
						SettingVO objsetting = listSetting.get(position);
						// đang ban(FragmentCategory)
						FragmentCategory obj = new FragmentCategory();

						if (objsetting.getId() == Constan.getIntProperty("dangban")) {
							SaveNewsListActivity.status = Constan.getIntProperty("dangban");
							SaveNewsListActivity.link = objsetting.getLink();
							startActivity(new Intent(context, SaveNewsListActivity.class));

						}
						// doi duyet(FragmentCategory)
						if (objsetting.getId() == Constan.getIntProperty("doiduyet")) {
							SaveNewsListActivity.status = Constan.getIntProperty("doiduyet");
							SaveNewsListActivity.link = objsetting.getLink();
							startActivity(new Intent(context, SaveNewsListActivity.class));
						}
						// bi tu choi(FragmentCategory)
						if (objsetting.getId() == Constan.getIntProperty("bituchoi")) {
							SaveNewsListActivity.status = Constan.getIntProperty("bituchoi");
							SaveNewsListActivity.link = objsetting.getLink();
							startActivity(new Intent(context, SaveNewsListActivity.class));
						}
						// da an(FragmentCategory)
						if (objsetting.getId() == Constan.getIntProperty("daan")) {
							SaveNewsListActivity.status = Constan.getIntProperty("daan");
							SaveNewsListActivity.link = objsetting.getLink();
							startActivity(new Intent(context, SaveNewsListActivity.class));
						}
						// tin da luu(FragmentCategory)
						if (objsetting.getId() == Constan.getIntProperty("tindaluu")) {
							// obj.callAsystask(SystemConfig.statusListSaveProduct,context);
							SaveNewsListActivity.status = Constan.getIntProperty("tindaluu");
							SaveNewsListActivity.link = objsetting.getLink();
							startActivity(new Intent(context, SaveNewsListActivity.class));
						}
						// tim kiem da luu(FragmentCategory)
						if (objsetting.getId() == Constan.getIntProperty("timkiemdaluu")) {
							startActivity(new Intent(context, ListSaveSearchActivity.class));
						}
						// khuyen mai(chuyen activity)
						if (objsetting.getId() == Constan.getIntProperty("khuyenmai")) {
							PromotionActivity.status = Constan.getIntProperty("khuyenmai");
							PromotionActivity.link = objsetting.getLink();
							startActivity(new Intent(context, PromotionActivity.class));
						}
						// huong dan(chuyen activity)
						if (objsetting.getId() == Constan.getIntProperty("huongdan")) {

						}
						// chia se ung dung(chuyen activity)
						if (objsetting.getId() == Constan.getIntProperty("chiaseungdung")) {
							shareFacebook();
						}
						// lien he(chuyen activity)
						if (objsetting.getId() == Constan.getIntProperty("lienhe")) {
							PromotionActivity.status = Constan.getIntProperty("lienhe");
							PromotionActivity.link = objsetting.getLink();
							startActivity(new Intent(context, PromotionActivity.class));
						}
						// dang xuat(chuyen activity)
						if (objsetting.getId() == Constan.getIntProperty("dangxuat")) {
							SharedPreferences settings = context.getSharedPreferences("PreferencesName",
									Context.MODE_PRIVATE);
							settings.edit().remove(SystemConfig.USER_ID).commit();
							settings.edit().remove(SystemConfig.SESSION_ID).commit();
							settings.edit().remove(SystemConfig.CHECKLOGIN).commit();
							startActivity(new Intent(context, LoginActivity.class));
						}
						// dang nhap(chuyen activity)
						if (objsetting.getId() == Constan.getIntProperty("dangnhap")) {

							startActivity(new Intent(context, LoginActivity.class));
						}
						// dang ky(chuyen activity)
						if (objsetting.getId() == Constan.getIntProperty("dangky")) {
							startActivity(new Intent(context, RegisterActivity.class));
						}
						// kieu xem
						if (objsetting.getId() == Constan.getIntProperty("kieuxem")) {

						}
						mDrawerLayout.closeDrawer(containerView);

					}

					@Override
					public void onLongClick(View view, int position) {

					}
				}));
		return layout;
	}

	public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
		containerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActivity().supportInvalidateOptionsMenu();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				// if (!isAdded()) {
				// return;
				// }
				getActivity().supportInvalidateOptionsMenu();
			}

			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				toolbar.setAlpha(1 - slideOffset / 2);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		new getProfileAsystask().execute();
		new SettingAsystask().execute();
		super.onResume();
	}

	public class SettingAsystask extends AsyncTask<Integer, Integer, OutputProduct> {
		JsonSetting setting;

		@Override
		protected void onPreExecute() {
			setting = new JsonSetting();

			super.onPreExecute();
		}

		@Override
		protected OutputProduct doInBackground(Integer... params) {
			oOput = setting.paserSetting(SystemConfig.user_id, SystemConfig.session_id, SystemConfig.device_id);
			listSetting = oOput.getSettingVO();
			SystemConfig.oOputproduct.setSettingVO(listSetting);
			return oOput;
		}

		@Override
		protected void onPostExecute(OutputProduct result) {
			adapter = new NavigationDrawerRightAdapter(getActivity(), listSetting);
			recyclerView.setAdapter(adapter);
		}
	}

	public interface FragmentDrawerListener {
		public void onDrawerItemSelected(View view, int position);
	}

	@Override
	public void onClick(View v) {
		dialogDelete();
	}

	public void dialogDelete() {
		dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_delete);
		dialog.setTitle("Thông Báo");
		txtalert = (TextView) dialog.findViewById(R.id.txtalert);
		if (!SystemConfig.session_id.isEmpty())
			txtalert.setText("Bạn muốn đămg xuất không!");
		else
			txtalert.setText("Bạn muốn đămg nhập!");
		txtalert.setTextSize(18);
		btnOk = (Button) dialog.findViewById(R.id.btn_Ok_Delete);
		btnCancle = (Button) dialog.findViewById(R.id.btn_Cancle_Delete);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!SystemConfig.session_id.isEmpty()) {
					SharedPreferences settings = context.getSharedPreferences("smile_studio", Context.MODE_PRIVATE);
					settings.edit().clear();
					settings.edit().remove(SystemConfig.USER_ID).commit();
					settings.edit().remove(SystemConfig.SESSION_ID).commit();
					settings.edit().remove(SystemConfig.CHECKLOGIN).commit();
					startActivity(new Intent(context, LoginActivity.class));
				} else {
					startActivity(new Intent(context, LoginActivity.class));
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

	public void shareFacebook() {

		String sAux = "\nLet me recommend you this application\n\n";
		sAux = sAux + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
		Debug.e("*********" + sAux);
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("text/plain");
		share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		share.putExtra(Intent.EXTRA_TEXT, sAux);
		startActivity(Intent.createChooser(share, "Share facebook seccfully"));
	}

	public class getProfileAsystask extends AsyncTask<Integer, String, OutputProduct> {
		JsonProfile profile;
		ArrayList<ProfileVO> listProfile = new ArrayList<ProfileVO>();
		OutputProduct obj = new OutputProduct();

		@Override
		protected void onPreExecute() {
			profile = new JsonProfile();
			aQuery = new AQuery(context);
			super.onPreExecute();
		}

		@Override
		protected OutputProduct doInBackground(Integer... params) {
			obj = profile.paserProfile(SystemConfig.user_id, SystemConfig.session_id, SystemConfig.device_id,
					SystemConfig.statusProfile);
			SystemConfig.oOputproduct.setProfileVO(obj.getProfileVO());
			return MainActivity.oOput;
		}

		@Override
		protected void onPostExecute(OutputProduct result) {
			ProfileVO objprofile = SystemConfig.oOputproduct.getProfileVO().get(0);
			txt_nameNavigaterReight.setText(objprofile.getUsername());
			Bitmap bitmap = aQuery.getCachedImage(objprofile.getAvatar());
			if (bitmap != null) {
				bitmap = Util.getCroppedBitmap(bitmap);
				aQuery.id(imgAvataNavigator).image(bitmap);
			} else {
				aQuery.id(imgAvataNavigator).image(objprofile.getAvatar(), true, true, 0, R.drawable.ic_launcher);
			}
			super.onPostExecute(result);
		}
	}
}