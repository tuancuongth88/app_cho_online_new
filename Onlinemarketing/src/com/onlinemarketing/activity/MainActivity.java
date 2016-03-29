package com.onlinemarketing.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.fragment.FragmentCategory;
import com.onlinemarketing.json.JsonSearch;
import com.onlinemarketing.object.CategoryVO;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.SettingVO;
import com.smile.studio.menu.FragmentDrawerLeft;
import com.smile.studio.menu.FragmentDrawerLeft.FragmentDrawerListener;
import com.smile.studio.menu.FragmentDrawerRight;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends ActionBarActivity implements FragmentDrawerListener {

	private Toolbar mToolbar;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	ArrayList<SettingVO> listSetting = new ArrayList<SettingVO>();

	private FragmentDrawerLeft drawerFragmentLeft;
	private FragmentDrawerRight drawerFragmentRight;
	public static OutputProduct oOput;

	Dialog dialog;
	EditText edit_namSPSearch;
	Spinner sinpnerPriceSearch, sinpnerCategorySearch, spinnerDatetimeSearch;
	Button btn_search,  txt_saveSearch;
	static Output out;
	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	public static int id_category, id_category_search;
	public static int status = SystemConfig.statusHomeProduct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		drawerFragmentLeft = (FragmentDrawerLeft) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_left);
		drawerFragmentLeft.setUp(R.id.fragment_navigation_drawer_left, (DrawerLayout) findViewById(R.id.drawer_layout),
				mToolbar);
		drawerFragmentRight = (FragmentDrawerRight) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_right);
		drawerFragmentRight.setUp(R.id.fragment_navigation_drawer_right, (DrawerLayout) findViewById(R.id.drawer_layout),
				mToolbar);
		drawerFragmentLeft.setDrawerListener(this);
		getSupportFragmentManager().beginTransaction().replace(R.id.container_body, new FragmentCategory()).commit();
	}

	@Override
	public void onDrawerItemSelected(View view, int position) {
		Debug.e("sssssssssss");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		dialogSearch();
		return (super.onOptionsItemSelected(menuItem));
	}

	public void dialogSearch() {
		dialog = new Dialog(this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_search);
		btn_search = (Button) dialog.findViewById(R.id.btn_Search);
		txt_saveSearch = (Button) dialog.findViewById(R.id.txt_saveSearch);
		edit_namSPSearch = (EditText) dialog.findViewById(R.id.edit_namSPSearch);

		sinpnerCategorySearch = (Spinner) dialog.findViewById(R.id.sinpnerCategorySearch);
		spinnerDatetimeSearch = (Spinner) dialog.findViewById(R.id.sinpnerTimerSearch);
		List<String> arr = new ArrayList<String>();
		arr.add("Hôm nay");
		arr.add("Tuần Trước");
		arr.add("Tháng trước");

		ArrayAdapter<String> adapter_option = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
		adapter_option.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerDatetimeSearch.setAdapter(adapter_option);

		int n = SystemConfig.oOputproduct.getCategoryVO().size();
		String[] title = new String[n];
		for (int i = 0; i < n; i++) {
			title[i] = SystemConfig.oOputproduct.getCategoryVO().get(i).getName();
		}
		sinpnerCategorySearch.setAdapter(new ArrayAdapter<String>(this, R.layout.item_spinersearch, title));

		txt_saveSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new SaveSearchAsysTask().execute();
			}
		});
		dialog.show();
	}

	public class SaveSearchAsysTask extends AsyncTask<String, String, Output> {
		JsonSearch jsonSearch;

		@Override
		protected void onPreExecute() {
			jsonSearch = new JsonSearch();
			super.onPreExecute();
		}

		@Override
		protected Output doInBackground(String... params) {
			CategoryVO objcategory = new CategoryVO();
			id_category_search = sinpnerCategorySearch.getSelectedItemPosition();
			int statusType = 0;
//			if (rdbSearchNew.isChecked())
//				statusType = SystemConfig.statusSearchnew;
//			if (rdbSearchOld.isChecked())
//				statusType = SystemConfig.statusSearchold;
			objcategory = SystemConfig.oOputproduct.getCategoryVO().get(id_category_search);
			out = jsonSearch.paserSaveSearch(SystemConfig.user_id, SystemConfig.session_id, SystemConfig.device_id,
					edit_namSPSearch.getText().toString(), "lag", "log", "price", String.valueOf(id_category_search),
					String.valueOf(statusType), "time");
			return out;
		}

		@Override
		protected void onPostExecute(Output result) {
			if (result.getCode() == Constan.getIntProperty("success")) {
				Debug.showAlert(MainActivity.this, result.getMessage());
			}
			super.onPostExecute(result);
		}

	}
}
