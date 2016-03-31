package com.smile.studio.menu;

import java.util.Collections;
import java.util.List;

import com.androidquery.AQuery;
import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.onlinemarketing.object.SettingVO;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationDrawerRightAdapter extends RecyclerView.Adapter<NavigationDrawerRightAdapter.MyViewHolder> {
	List<SettingVO> data = Collections.emptyList();
	private LayoutInflater inflater;
	private Context context;

	public NavigationDrawerRightAdapter(Context context, List<SettingVO> data) {
		this.setContext(context);
		inflater = LayoutInflater.from(context);
		this.data = data;
	}

	public void delete(int position) {
		data.remove(position);
		notifyItemRemoved(position);
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.custom_item_navigator_reght, parent, false);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		SettingVO current = data.get(position);
		holder.init(current);
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		private ImageView imageview = null;
		private TextView title = null;
		private TextView number = null;
		private AQuery aQuery = null;

		public MyViewHolder(View itemView) {
			super(itemView);
			imageview = (ImageView) itemView.findViewById(R.id.imgReghtNavigator);
			title = (TextView) itemView.findViewById(R.id.title);
			number = (TextView) itemView.findViewById(R.id.numberRightNavigator);
			aQuery = new AQuery(itemView);
		}

		public void init(SettingVO item) {
			Bitmap bitmap = aQuery.getCachedImage(item.getAvatar());
			if (bitmap != null) {
				aQuery.id(imageview).image(bitmap);
			} else {
				aQuery.id(imageview).image(item.getAvatar(), true, true, 0, R.drawable.ic_launcher);
			}
			title.setText(item.getName());
			number.setText(item.getQuantily().replace("null", ""));
		}
	}
}
