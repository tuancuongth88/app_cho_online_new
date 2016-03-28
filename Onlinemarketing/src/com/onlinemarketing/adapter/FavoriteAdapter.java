package com.onlinemarketing.adapter;

import java.util.List;

import com.androidquery.AQuery;
import com.example.onlinemarketing.R;
import com.onlinemarketing.object.BackListVO;
import com.onlinemarketing.object.ProfileVO;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract.Profile;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FavoriteAdapter extends ArrayAdapter<ProfileVO>{
	Context context;
	List<ProfileVO> listData;
	int layoutId;
	LayoutInflater inflater;
	ViewHolder holder;

	public FavoriteAdapter(Context context, int resource, List<ProfileVO> objects) {
		super(context, resource, objects);
		this.context = context;
		this.layoutId = resource;
		this.listData = objects;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private class ViewHolder {
		ImageView img;
		TextView userName, phone, add;
		private AQuery aQuery = null;

		public ViewHolder(View view) {
			img = (ImageView) view.findViewById(R.id.imgAvatarFavorite);
			userName = (TextView) view.findViewById(R.id.txtNameFavorite);
			phone = (TextView) view.findViewById(R.id.txtPhoneFavorite);
			add = (TextView) view.findViewById(R.id.txtAdderssFavorite);
			aQuery = new AQuery(view);
		}

		public void init(ProfileVO item) {
			Bitmap bitmap = aQuery.getCachedImage(item.getAvatar());
			if (bitmap != null) {
				aQuery.id(img).image(bitmap);
			} else {
				aQuery.id(img).image(item.getAvatar(), true, true, 0, R.drawable.ic_launcher);
			}
			userName.setText(item.getUsername());
			phone.setText(item.getPhone());
			add.setText(item.getAddress());
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

	
}
