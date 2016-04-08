package com.onlinemarketing.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.onlinemarketing.activity.MainActivity.SaveSearchAsysTask;
import com.onlinemarketing.activity.MainActivity.SearchAsystask;
import com.onlinemarketing.adapter.BackListAdapter;
import com.onlinemarketing.adapter.ListSaveSearchAdapter;
import com.onlinemarketing.adapter.ListSaveSearchAdapter.CallbackPosition;
import com.onlinemarketing.adapter.ListSaveSearchAdapter.ViewHolder;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonProduct;
import com.onlinemarketing.json.JsonSearch;
import com.onlinemarketing.object.Category_SearchVO;
import com.onlinemarketing.object.CityVO;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.PriceVO;
import com.onlinemarketing.object.ProductVO;
import com.onlinemarketing.object.SearchVO;
import com.onlinemarketing.object.TimeVO;
import com.onlinemarketing.object.TypeVO;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ListSaveSearchActivity extends BaseActivity
		implements OnItemClickListener, CallbackPosition, OnClickListener {
	ArrayList<ProductVO> list = new ArrayList<ProductVO>();
	ListView listview;
	ListSaveSearchAdapter adapter;
	OutputProduct oOput;
	Output out;
	ViewHolder viewHolder;
	ImageView imgBack;
	Spinner sinpnerPriceSearch, sinpnerCategorySearch, spinnerDatetimeSearch, sinpnerTypeProductSearch,
			sinpnerAddSearch;
	Button btn_search, txt_saveSearch;
	Dialog dialog;
	public int search_id;
	public static SearchVO search;
	EditText edit_namSPSearch;
	ProgressDialog progressDialog;
	TextView txt_showToast;
	public int category_id, price_id, time_id, type_id, city_id;
	public String lat, log;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_save_search);
		listview = (ListView) findViewById(R.id.listSaveSearch);
		imgBack = (ImageView) findViewById(R.id.imgBackTitle);
		txt_showToast = (TextView) findViewById(R.id.txt_showToast);
		imgBack.setOnClickListener(this);
		listview.setOnItemClickListener(this);
		if (isConnect()) {
			new ListSaveSearchAsystask().execute();
		}
	}

	@Override
	protected void onResume() {
		ListSaveSearchAdapter.type = 0;
		// new ListSaveSearchAsystask().execute();
		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		search_id = list.get(arg2).getId();
		Debug.e("aaaaaaaaaa" + list.get(arg2).getId());
		new GetSearchAsystask().execute();

	}

	public class ListSaveSearchAsystask extends AsyncTask<Integer, String, OutputProduct> {
		JsonSearch jsonListSaveSearch;

		@Override
		protected void onPreExecute() {
			jsonListSaveSearch = new JsonSearch();
			super.onPreExecute();
		}

		@Override
		protected OutputProduct doInBackground(Integer... params) {
			oOput = jsonListSaveSearch.paserListSearch(SystemConfig.user_id, SystemConfig.session_id,
					SystemConfig.device_id);
			list = oOput.getProductVO();
			SystemConfig.oOputproduct.setProductVO(list);
			return oOput;
		}

		@Override
		protected void onPostExecute(OutputProduct result) {
			if (list.size() > 0) {
				adapter = new ListSaveSearchAdapter(ListSaveSearchActivity.this, getLayoutInflater(), list,
						ListSaveSearchActivity.this);
				listview.setAdapter(adapter);
			} else {
				listview.setVisibility(View.GONE);
				txt_showToast.setText(Constan.getProperty("Error06"));
				txt_showToast.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void callbackDeletePosition(int position) {
	}

	@Override
	public void onClick(View v) {
		this.finish();
	}

	public class DeleteAsynTask extends AsyncTask<Integer, String, Output> {

		JsonProduct jsonProduct;

		@Override
		protected void onPreExecute() {
			jsonProduct = new JsonProduct();
			super.onPreExecute();
		}

		@Override
		protected Output doInBackground(Integer... params) {
			Debug.e("User: " + SystemConfig.user_id + " Session: " + SystemConfig.session_id + " device: "
					+ SystemConfig.device_id);
			out = jsonProduct.paserDeleteBackListAndFavorite(SystemConfig.user_id, SystemConfig.session_id,
					SystemConfig.device_id, 4, SystemConfig.statusDeleteBackList);
			return out;
		}

		@Override
		protected void onPostExecute(Output result) {
			if (result.getCode() == Constan.getIntProperty("success")) {
				Debug.showAlert(ListSaveSearchActivity.this, result.getMessage());
			}
			super.onPostExecute(result);
		}
	}

	public void dialogHistorySearch(String name, int city_id, int category_id, int price_id, int type_id, int time_id) {
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
		edit_namSPSearch.setText(name);
		List<CityVO> lstCity = search.getLstCity();
		String[] city = new String[lstCity.size()];
		int positionCity = 0;
		int positionCategory = 0;
		final int positionPrice = 0;
		int positionType = 0;
		int positionTime = 0;
		for (int i = 0; i < city.length; i++) {
			city[i] = lstCity.get(i).getName();
			if (city_id == lstCity.get(i).getId())
				positionCity = i;
		}
		ArrayAdapter<String> adapteCity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, city);
		sinpnerAddSearch.setAdapter(adapteCity);
		sinpnerAddSearch.setSelection(positionCity);
		List<TypeVO> listType = search.getLstType();
		int n1 = listType.size();
		String[] type = new String[n1];
		for (int i = 0; i < n1; i++) {
			type[i] = listType.get(i).getType_name();
			if (type_id == listType.get(i).getType_id())
				positionType = i;
		}
		ArrayAdapter<String> adapteType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
		sinpnerTypeProductSearch.setAdapter(adapteType);
		sinpnerTypeProductSearch.setSelection(positionType);
		List<Category_SearchVO> listcategory = search.getLstCategorySearch();
		int n = listcategory.size();
		String[] title = new String[n];
		for (int i = 0; i < n; i++) {
			title[i] = listcategory.get(i).getCategory_name();
			if (category_id == listcategory.get(i).getCategory_id())
				positionCategory = i;
		}
		ArrayAdapter<String> adapteCategory = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
				title);
		sinpnerCategorySearch.setAdapter(adapteCategory);
		sinpnerCategorySearch.setSelection(positionCategory);
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
				ArrayAdapter<String> adaptePrice = new ArrayAdapter<String>(ListSaveSearchActivity.this,
						android.R.layout.simple_spinner_item, titlePrice);
				sinpnerPriceSearch.setAdapter(adaptePrice);
				spinnerDatetimeSearch.setSelection(positionPrice);
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
			if (time_id == listTime.get(i).getTime_id()) {
				positionTime = i;
				Debug.e("Time : " + listTime.get(positionTime).getTime_name());
			}
		}
		ArrayAdapter<String> adapteTime = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, time);
		spinnerDatetimeSearch.setAdapter(adapteTime);
		spinnerDatetimeSearch.setSelection(positionTime);

		txt_saveSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!SystemConfig.session_id.isEmpty()) {
					Debug.showAlert(ListSaveSearchActivity.this, "Tìm kiếm đã được lưu");
				} else {
					startActivity(new Intent(ListSaveSearchActivity.this, LoginActivity.class));
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

	public class SearchAsystask extends AsyncTask<Integer, Integer, OutputProduct> {
		String Device_id;
		JsonSearch jsonSearch;

		@Override
		protected void onPreExecute() {
			jsonSearch = new JsonSearch();

			progressDialog = new ProgressDialog(ListSaveSearchActivity.this);
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
				startActivity(new Intent(ListSaveSearchActivity.this, SearchActivity.class));
				finish();
			} else {
				Debug.showAlert(ListSaveSearchActivity.this, "Không tìm thấy Sản phẩm!!");
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
			search = jsonSearch.getSearch(SystemConfig.user_id, SystemConfig.session_id, SystemConfig.device_id,
					search_id);
			return search;
		}

		@Override
		protected void onPostExecute(SearchVO result) {
			if (result.getCode().equals("200")) {
				Debug.e("time: " + search.getTime_id());
				dialogHistorySearch(search.getName(), Integer.parseInt(search.getCity_id()),
						Integer.parseInt(search.getCategory_id()), Integer.parseInt(search.getPrice_id()),
						Integer.parseInt(search.getType_id()), Integer.parseInt(search.getTime_id()));
			}
		}
	}
}
