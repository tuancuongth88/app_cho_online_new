package com.onlinemarketing.adapter;

import java.util.List;

import com.androidquery.AQuery;
import com.example.onlinemarketing.R;
import com.onlinemarketing.object.OutputProduct;
import com.onlinemarketing.object.ProductVO;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomePageAdapter extends ArrayAdapter<ProductVO> {

	Context context;
	List<ProductVO> listData;
	int layoutId;
	LayoutInflater inflater;
	ViewHolder holder;
	public HomePageAdapter(Context context, int resource, List<ProductVO> objects) {
		super(context, resource, objects);
		this.context = context;
		this.layoutId = resource;
		this.listData = objects;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private class ViewHolder {
		ImageView img;
		TextView tenSP, ngaydang, giaSp;
		private AQuery aQuery = null;

		public ViewHolder(View view) {
			img = (ImageView) view.findViewById(R.id.item_img_trangChu);
			tenSP = (TextView) view.findViewById(R.id.item_txt_tenSP);
			giaSp = (TextView) view.findViewById(R.id.item_txt_giaSP);
			ngaydang = (TextView) view.findViewById(R.id.item_txt_ngay);
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
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.init(listData.get(position));
		return convertView;
	}

	public void setItemList(OutputProduct result) {
		// TODO Auto-generated method stub
		
	}
}
