package com.onlinemarketing.adapter;

import java.util.List;

import com.androidquery.AQuery;
import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonProduct;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.ProfileVO;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FavoriteAdapter extends ArrayAdapter<ProfileVO> {
	Context context;
	List<ProfileVO> listData;
	int layoutId;
	LayoutInflater inflater;
	ViewHolder holder;
	static Output out;
	Dialog dialog;
	Button btnOk, btnCancle;
	TextView txtalert;
	public FavoriteAdapter(Context context, int resource, List<ProfileVO> objects) {
		super(context, resource, objects);
		this.context = context;
		this.layoutId = resource;
		this.listData = objects;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	private class ViewHolder {
		ImageView img, imgDelete;
		TextView userName, phone, add;
		private AQuery aQuery = null;

		public ViewHolder(View view) {
			img = (ImageView) view.findViewById(R.id.imgAvatarFavorite);
			userName = (TextView) view.findViewById(R.id.txtNameFavorite);
			phone = (TextView) view.findViewById(R.id.txtPhoneFavorite);
			add = (TextView) view.findViewById(R.id.txtAdderssFavorite);
			imgDelete = (ImageView) view.findViewById(R.id.imgDeleteFavorite);
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
			holder.imgDelete.setTag(new Integer(position));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.init(listData.get(position));
		holder.imgDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Integer positon = (Integer) v.getTag();
				dialogDelete(positon);
			}
		});
		return convertView;
	}

	public void dialogDelete(final int id) {
		dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_delete);
		dialog.setTitle("Thông Báo");
		btnOk = (Button) dialog.findViewById(R.id.btn_Ok_Delete);
		btnCancle = (Button) dialog.findViewById(R.id.btn_Cancle_Delete);
		txtalert = (TextView)dialog.findViewById(R.id.txtalert);
		txtalert.setText("Bạn có chắc chắn muốn xóa user này???");
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new DeleteAsynTask().execute(id);
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

	public class DeleteAsynTask extends AsyncTask<Integer, String, Output> {

		JsonProduct jsonProduct;
		int position;

		@Override
		protected void onPreExecute() {
			jsonProduct = new JsonProduct();
			super.onPreExecute();
		}

		@Override
		protected Output doInBackground(Integer... params) {
			position = params[0];
			int id = SystemConfig.oOputproduct.getProfileVO().get(position).getId();
			Debug.e("Position: " + position + "\n id : " + id);
			out = jsonProduct.paserDeleteBackListAndFavorite(SystemConfig.user_id, SystemConfig.session_id,
					SystemConfig.device_id, id, SystemConfig.statusDeleteFavorite);

			return out;
		}

		@Override
		protected void onPostExecute(Output result) {
			if (result.getCode() == Constan.getIntProperty("success")) {
				SystemConfig.oOputproduct.getProfileVO().remove(position);
				notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}
	}

}
