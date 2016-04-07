package com.onlinemarketing.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.onlinemarketing.adapter.HomePageAdapter;
import com.onlinemarketing.adapter.ListSaveSearchAdapter;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.fragment.FragmentCategory;
import com.onlinemarketing.json.JsonProduct;
import com.onlinemarketing.json.JsonSearch;
import com.onlinemarketing.object.CategoryVO;
import com.onlinemarketing.object.Category_SearchVO;
import com.onlinemarketing.object.CityVO;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.PriceVO;
import com.onlinemarketing.object.ProductVO;
import com.onlinemarketing.object.SearchVO;
import com.onlinemarketing.object.SettingVO;
import com.onlinemarketing.object.TimeVO;
import com.onlinemarketing.object.TypeVO;
import com.smile.studio.menu.FragmentDrawerLeft;
import com.smile.studio.menu.FragmentDrawerLeft.FragmentDrawerListener;
import com.smile.studio.menu.FragmentDrawerRight;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class MainActivity extends ActionBarActivity implements FragmentDrawerListener {

	private Toolbar mToolbar;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	ArrayList<SettingVO> listSetting = new ArrayList<SettingVO>();
	ArrayList<ProductVO> list = new ArrayList<ProductVO>();
	ProgressDialog progressDialog;
	private FragmentDrawerLeft drawerFragmentLeft;
	private FragmentDrawerRight drawerFragmentRight;
	public static SearchVO search;
	public static OutputProduct oOput;
	Dialog dialog;
	EditText edit_namSPSearch;
	Spinner sinpnerPriceSearch, sinpnerCategorySearch, spinnerDatetimeSearch, sinpnerTypeProductSearch,sinpnerAddSearch;
	Button btn_search, txt_saveSearch;
	static Output out;
	public static int search_id = 0;
	public int category_id, price_id, time_id, type_id,city_id;
	public String lat, log;
	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	public static int id_category, id_category_search;
	public static int status = SystemConfig.statusHomeProduct;
	ImageView imgsearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		imgsearch = (ImageView) findViewById(R.id.imgSearchToolbar);
		imgsearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Debug.showAlert(MainActivity.this, "co ho");
				new GetSearchAsystask().execute();
			}
		});
		// getSupportActionBar().setIcon(R.drawable.icon_search);
		drawerFragmentLeft = (FragmentDrawerLeft) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_navigation_drawer_left);
		drawerFragmentLeft.setUp(R.id.fragment_navigation_drawer_left, (DrawerLayout) findViewById(R.id.drawer_layout),
				mToolbar);
		drawerFragmentRight = (FragmentDrawerRight) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_navigation_drawer_right);
		drawerFragmentRight.setUp(R.id.fragment_navigation_drawer_right,
				(DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
		drawerFragmentLeft.setDrawerListener(this);
		getSupportFragmentManager().beginTransaction().replace(R.id.container_body, new FragmentCategory()).commit();
	}

	@Override
	public void onDrawerItemSelected(View view, int position) {
		Debug.e("sssssssssss");
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
		sinpnerPriceSearch = (Spinner) dialog.findViewById(R.id.sinpnerPriceSearch);
		sinpnerTypeProductSearch = (Spinner) dialog.findViewById(R.id.sinpnerTypeProductSearch);
		sinpnerAddSearch = (Spinner) dialog.findViewById(R.id.sinpnerAddSearch);
		List<CityVO> lstCity = search.getLstCity();
		String [] city = new String[lstCity.size()];
		for (int i = 0; i < city.length; i++) {
			city[i] = lstCity.get(i).getName();
		}
		ArrayAdapter<String> adapteCity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, city);
		sinpnerAddSearch.setAdapter(adapteCity);
		List<TypeVO> listType = search.getLstType();
		int n1 = listType.size();
		String[] type = new String[n1];
		for (int i = 0; i < n1; i++) {
			type[i] = listType.get(i).getType_name();
		}
		ArrayAdapter<String> adapteType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
		sinpnerTypeProductSearch.setAdapter(adapteType);
		List<Category_SearchVO> listcategory = search.getLstCategorySearch();
		int n = listcategory.size();
		String[] title = new String[n];
		for (int i = 0; i < n; i++) {
			title[i] = listcategory.get(i).getCategory_name();
		}
		ArrayAdapter<String> adapteCategory = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				title);
		sinpnerCategorySearch.setAdapter(adapteCategory);
		sinpnerCategorySearch.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				List<PriceVO> lstprice = search.getLstCategorySearch().get(arg2).getLstPrice();
				int n = lstprice.size();
				String[] titlePrice = new String[n];
				for (int i = 0; i < n; i++) {
					titlePrice[i] = lstprice.get(i).getStart() + "->" + lstprice.get(i).getEnd();
				}
				ArrayAdapter<String> adaptePrice = new ArrayAdapter<String>(MainActivity.this,
						android.R.layout.simple_spinner_item, titlePrice);
				sinpnerPriceSearch.setAdapter(adaptePrice);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		spinnerDatetimeSearch = (Spinner) dialog.findViewById(R.id.sinpnerTimerSearch);
		List<TimeVO> listTime = search.getLstTime();
		int n2 = listTime.size();
		String[] time = new String[n2];
		for (int i = 0; i < n2; i++) {
			time[i] = listTime.get(i).getTime_name();
		}
		ArrayAdapter<String> adapteTime = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, time);
		spinnerDatetimeSearch.setAdapter(adapteTime);
		txt_saveSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!SystemConfig.session_id.isEmpty()) {
					new SaveSearchAsysTask().execute();
				} else {
					startActivity(new Intent(MainActivity.this, LoginActivity.class));
				}
				dialog.dismiss();
			}
		});
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new SearchAsystask().execute();
				dialog.dismiss();
			}
		});

		dialog.show();
	}
//	public void dialogHistorySearch(String name, int city_id, int category_id, int price_id, int type_id, int time_id) {
//		dialog = new Dialog(this);
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog.setContentView(R.layout.dialog_search);
//		btn_search = (Button) dialog.findViewById(R.id.btn_Search);
//		txt_saveSearch = (Button) dialog.findViewById(R.id.txt_saveSearch);
//		edit_namSPSearch = (EditText) dialog.findViewById(R.id.edit_namSPSearch);
//		sinpnerCategorySearch = (Spinner) dialog.findViewById(R.id.sinpnerCategorySearch);
//		sinpnerPriceSearch = (Spinner) dialog.findViewById(R.id.sinpnerPriceSearch);
//		sinpnerTypeProductSearch = (Spinner) dialog.findViewById(R.id.sinpnerTypeProductSearch);
//		sinpnerAddSearch = (Spinner) dialog.findViewById(R.id.sinpnerAddSearch);
//		edit_namSPSearch.setText(name);
//		List<CityVO> lstCity = search.getLstCity();
//		String [] city = new String[lstCity.size()];
//		int positionCity = 0;
//		int positionCategory = 0;
//		int positionPrice = 0;
//		int positionType = 0;
//		int positionTime = 0;
//		for (int i = 0; i < city.length; i++) {
//			city[i] = lstCity.get(i).getName();
//			if(city_id == lstCity.get(i).getId())
//				positionCity = i;
//		}
//		ArrayAdapter<String> adapteCity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, city);
//		sinpnerAddSearch.setAdapter(adapteCity);
//		sinpnerAddSearch.setSelection(positionCity);
//		List<TypeVO> listType = search.getLstType();
//		int n1 = listType.size();
//		String[] type = new String[n1];
//		for (int i = 0; i < n1; i++) {
//			type[i] = listType.get(i).getType_name();
//			if(type_id == listType.get(i).getType_id())
//				positionType = i;
//		}
//		ArrayAdapter<String> adapteType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
//		sinpnerTypeProductSearch.setAdapter(adapteType);
//		sinpnerTypeProductSearch.setSelection(positionType);
//		List<Category_SearchVO> listcategory = search.getLstCategorySearch();
//		int n = listcategory.size();
//		String[] title = new String[n];
//		for (int i = 0; i < n; i++) {
//			title[i] = listcategory.get(i).getCategory_name();
//			if(category_id == listcategory.get(i).getCategory_id())
//				positionCategory = i;
//		}
//		ArrayAdapter<String> adapteCategory = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
//				title);
//		sinpnerCategorySearch.setAdapter(adapteCategory);
//		sinpnerCategorySearch.setSelection(positionCategory);
//		sinpnerCategorySearch.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				List<PriceVO> lstprice = search.getLstCategorySearch().get(arg2).getLstPrice();
//				int n = lstprice.size();
//				String[] titlePrice = new String[n];
//				for (int i = 0; i < n; i++) {
//					titlePrice[i] = lstprice.get(i).getStart() + "->" + lstprice.get(i).getEnd();
//				}
//				ArrayAdapter<String> adaptePrice = new ArrayAdapter<String>(MainActivity.this,
//						android.R.layout.simple_spinner_item, titlePrice);
//				sinpnerPriceSearch.setAdapter(adaptePrice);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//		spinnerDatetimeSearch = (Spinner) dialog.findViewById(R.id.sinpnerTimerSearch);
//		List<TimeVO> listTime = search.getLstTime();
//		int n2 = listTime.size();
//		String[] time = new String[n2];
//		for (int i = 0; i < n2; i++) {
//			time[i] = listTime.get(i).getTime_name();
//		}
//		ArrayAdapter<String> adapteTime = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, time);
//		spinnerDatetimeSearch.setAdapter(adapteTime);
//		txt_saveSearch.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (!SystemConfig.session_id.isEmpty()) {
//					new SaveSearchAsysTask().execute();
//				} else {
//					startActivity(new Intent(MainActivity.this, LoginActivity.class));
//				}
//				dialog.dismiss();
//			}
//		});
//		btn_search.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				new SearchAsystask().execute();
//				dialog.dismiss();
//			}
//		});
//
//		dialog.show();
//	}
	public class SaveSearchAsysTask extends AsyncTask<String, String, Output> {
		JsonSearch jsonSearch;

		@Override
		protected void onPreExecute() {
			jsonSearch = new JsonSearch();
			super.onPreExecute();
		}

		@Override
		protected Output doInBackground(String... params) {
			city_id = search.getLstCity().get(sinpnerAddSearch.getSelectedItemPosition()).getId();
			time_id = search.getLstTime().get(spinnerDatetimeSearch.getSelectedItemPosition()).getTime_id();
			type_id = search.getLstType().get(sinpnerTypeProductSearch.getSelectedItemPosition()).getType_id();
			category_id = search.getLstCategorySearch().get(sinpnerCategorySearch.getSelectedItemPosition())
					.getCategory_id();
			price_id = search.getLstCategorySearch().get(sinpnerCategorySearch.getSelectedItemPosition()).getLstPrice()
					.get(sinpnerPriceSearch.getSelectedItemPosition()).getPrice_id();
			lat = search.getLat();
			log = search.getLog();
			Debug.e("time_id: " + time_id + "\ntype_id: " + type_id + "\ncategory_id: " + category_id + "\nprice_id: "
					+ price_id + "\nlat: " + lat + "\nlog: " + log);
			out = jsonSearch.paserSaveSearch(SystemConfig.user_id, SystemConfig.session_id, SystemConfig.device_id,
					edit_namSPSearch.getText().toString(), lat, log, String.valueOf(price_id),
					String.valueOf(category_id), String.valueOf(type_id), String.valueOf(time_id), city_id);
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

	public class SearchAsystask extends AsyncTask<Integer, Integer, OutputProduct> {
		String Device_id;
		JsonSearch jsonSearch;

		@Override
		protected void onPreExecute() {
			jsonSearch = new JsonSearch();

			progressDialog = new ProgressDialog(MainActivity.this);
			// Set progressdialog message
			progressDialog.setMessage("Loading...");
			progressDialog.setIndeterminate(false);
			// Show progressdialog
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected OutputProduct doInBackground(Integer... params) {
			city_id = search.getLstCity().get(sinpnerAddSearch.getSelectedItemPosition()).getId();
			time_id = search.getLstTime().get(spinnerDatetimeSearch.getSelectedItemPosition()).getTime_id();
			type_id = search.getLstType().get(sinpnerTypeProductSearch.getSelectedItemPosition()).getType_id();
			category_id = search.getLstCategorySearch().get(sinpnerCategorySearch.getSelectedItemPosition())
					.getCategory_id();
			price_id = search.getLstCategorySearch().get(sinpnerCategorySearch.getSelectedItemPosition()).getLstPrice()
					.get(sinpnerPriceSearch.getSelectedItemPosition()).getPrice_id();
			lat = search.getLat();
			log = search.getLog();
			Debug.e("time_id: " + time_id + "\ntype_id: " + type_id + "\ncategory_id: " + category_id + "\nprice_id: "
					+ price_id + "\nlat: " + lat + "\nlog: " + log);
			oOput = jsonSearch.paserSearch(SystemConfig.user_id, SystemConfig.session_id, SystemConfig.device_id,
					edit_namSPSearch.getText().toString(), city_id, price_id, category_id, type_id, time_id);
			list = oOput.getProductVO();
			SystemConfig.oOputproduct.setProductVO(list);
			return MainActivity.oOput;
		}

		@Override
		protected void onPostExecute(OutputProduct result) {
			if (result.getCode() == Constan.getIntProperty("success")) {
				startActivity(new Intent(MainActivity.this, SearchActivity.class));
			} else {
				Debug.showAlert(MainActivity.this, "Không tìm thấy Sản phẩm!!");
			}
			progressDialog.dismiss();
		}
	}

	public class GetSearchAsystask extends AsyncTask<Integer, String, SearchVO> {
		JsonSearch jsonSearch;

		@Override
		protected void onPreExecute() {
			jsonSearch = new JsonSearch();
			super.onPreExecute();
		}

		@Override
		protected SearchVO doInBackground(Integer... params) {
			search = jsonSearch.getSearch(SystemConfig.user_id, SystemConfig.session_id, SystemConfig.device_id, search_id);
			return search;
		}

		@Override
		protected void onPostExecute(SearchVO result) {
			if (result.getCode().equals("200")) {
				dialogSearch();
			}
		}
	}
}
