package com.onlinemarketing.adapter;

import java.util.List;

import com.androidquery.AQuery;
import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonProduct;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.ProductVO;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SaveNewsListAdapter extends ArrayAdapter<ProductVO> {

	Context context;
//	List<ProductVO> listData;
	int layoutId;
	LayoutInflater inflater;
	ViewHolder holder;
	private List<ProductVO> list;
	public SaveNewsListAdapter(Context context, int resource, List<ProductVO> objects) {
		super(context, resource, objects);
		this.context = context;
		this.layoutId = resource;
		this.list = objects;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private class ViewHolder {
		ImageView img, imgDelete;
		TextView tenSP, ngaydang, giaSp;
		private AQuery aQuery = null;

		public ViewHolder(View view) {
			img = (ImageView) view.findViewById(R.id.item_img_trangChu);
			tenSP = (TextView) view.findViewById(R.id.item_txt_tenSP);
			giaSp = (TextView) view.findViewById(R.id.item_txt_giaSP);
			ngaydang = (TextView) view.findViewById(R.id.item_txt_ngay);
			imgDelete = (ImageView) view.findViewById(R.id.item_delete_saveNewsList);
			aQuery = new AQuery(view);
		}

		public void init(ProductVO item) {
			Bitmap bitmap = aQuery.getCachedImage(item.getAvatar());
			if (bitmap != null) {
				aQuery.id(img).image(bitmap);
			} else {
				aQuery.id(img).image(item.getAvatar(), true, true, 0, R.drawable.ic_launcher);
			}
			tenSP.setText(item.getName());
			ngaydang.setText(item.getStartdate());
			giaSp.setText(item.getPrice());
			
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(layoutId, parent, false);
			holder = new ViewHolder(convertView);
			holder.imgDelete.setTag(new Integer(position));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.init(list.get(position));
		holder.imgDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Integer id = (Integer) v.getTag();
				Debug.e("vị trí : " + id);
				new AsystarkDeleteSearchSave().execute(id);
			}
		});
		return convertView;
	}

	public void setItemList(OutputProduct result) {
		// TODO Auto-generated method stub
		
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
			output = obj.paserDeleteBackListAndFavorite(SystemConfig.user_id, SystemConfig.session_id, SystemConfig.device_id, id, SystemConfig.statusDeleteSaveNewsList);
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
