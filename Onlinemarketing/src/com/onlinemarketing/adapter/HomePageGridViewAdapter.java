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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class HomePageGridViewAdapter extends ArrayAdapter<ProductVO> {

	Context context;
	List<ProductVO> listData;
	int layoutId;
	LayoutInflater inflater;
	ViewHolder holder;
	public HomePageGridViewAdapter(Context context, int resource, List<ProductVO> objects) {
		super(context, resource, objects);
		this.context = context;
		this.layoutId = resource;
		this.listData = objects;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private class ViewHolder {
		ImageView frgProduct;
		TextView tenSP, ngaydang, giaSp;
		private AQuery aQuery = null;

		public ViewHolder(View view) {
			frgProduct = (ImageView) view.findViewById(R.id.item_frgProduct);
			tenSP = (TextView) view.findViewById(R.id.item_txt_titleProductGrid);
			giaSp = (TextView) view.findViewById(R.id.item_txt_priceProductGrid);
			ngaydang = (TextView) view.findViewById(R.id.item_txt_dateProductGrid);
			aQuery = new AQuery(view);
		}

		public void init(ProductVO item) {
			Bitmap bitmap = aQuery.getCachedImage(item.getAvatar());
			if (bitmap != null) {
				aQuery.id(frgProduct).image(bitmap);
			} else {
				aQuery.id(frgProduct).image(item.getAvatar(), true, true, 0, R.drawable.ic_launcher);
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
		
	}
}
