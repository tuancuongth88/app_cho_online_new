package com.onlinemarketing.adapter;

import java.util.ArrayList;

import com.example.onlinemarketing.R;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonProduct;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.ProductVO;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ListSaveSearchAdapter extends BaseAdapter {
	private ArrayList<ProductVO> list;
	private LayoutInflater inflater;
	private CallbackPosition callback;
	public static int type = 0;
	private ViewHolder holder = null;

	public ListSaveSearchAdapter(LayoutInflater layoutInflater, ArrayList<ProductVO> data, CallbackPosition callback) {
		// TODO Auto-generated constructor stub
		this.setList(data);
		this.inflater = layoutInflater;
		this.callback = callback;
	}

	public ArrayList<ProductVO> getList() {
		return list;
	}

	public void setList(ArrayList<ProductVO> list) {
		if (list != null) {
			this.list = list;
		} else {
			list = new ArrayList<ProductVO>();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public ProductVO getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return getItem(position).getId();
	}

	public class ViewHolder {
		TextView txtname;

		public void setData(ProductVO productVO) {
			txtname.setText(productVO.getName());
		}

		public ViewHolder(View view) {
			txtname = (TextView) view.findViewById(R.id.txtNameListSearch);
		}

		public ViewHolder() {
			super();
		}
		
	}

	public interface CallbackPosition {
		void callbackDeletePosition(int position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			convertView = inflater.inflate(R.layout.item_list_save_search, parent, false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.setData(list.get(position));
		ImageView imgDeleteListSearch = (ImageView) convertView.findViewById(R.id.imgDeleteListSearch);
		imgDeleteListSearch.setTag(position);
		imgDeleteListSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Integer index = (Integer) v.getTag();
				int id = index;
				new AsystarkDeleteSearchSave().execute(id);
			}
		});
		return convertView;
	}
	class AsystarkDeleteSearchSave extends AsyncTask<Integer, Void, Output>{
		JsonProduct obj ;
		Output output;
		int position;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			obj = new JsonProduct();
			super.onPreExecute();
		}

		@Override
		protected Output doInBackground(Integer... params) {
			position =params[0]; 
			int id = list.get(position).getId();
			output = obj.paserDeleteBackListAndFavorite(SystemConfig.user_id, SystemConfig.session_id, SystemConfig.device_id, id, SystemConfig.statusDeleteSearch);
			return output;
		}
		
		@Override
		protected void onPostExecute(Output result) {
			// TODO Auto-generated method stub
			if(result.getCode()==200){
			list.remove(position);
			notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
		
	}

}
