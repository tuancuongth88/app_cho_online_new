package com.smile.studio.menu;

import java.util.Collections;
import java.util.List;

import com.androidquery.AQuery;
import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.onlinemarketing.activity.SplashActivity;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonCategory;
import com.onlinemarketing.object.CategoryVO;
import com.onlinemarketing.object.Output;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationDrawerLeftAdapter extends RecyclerView.Adapter<NavigationDrawerLeftAdapter.MyViewHolder> {
	List<CategoryVO> data = Collections.emptyList();
	private LayoutInflater inflater;
	private Context context;
	int id_position;
	public static int status = 1;
	public NavigationDrawerLeftAdapter(Context context, List<CategoryVO> data) {
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
		View view = inflater.inflate(R.layout.custom_item_navigator_left, parent, false);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		CategoryVO current = data.get(position);
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
		private AQuery aQuery = null;
		public Output out;
		public boolean isLike = true;
		public MyViewHolder(View itemView) {
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.title);
			aQuery = new AQuery(itemView);
		}

		public void init(CategoryVO item) {
			Bitmap bitmap = aQuery.getCachedImage(item.getAvatar());
			if (bitmap != null) {
				aQuery.id(imageview).image(bitmap);
			} else {
				aQuery.id(imageview).image(item.getAvatar(), true, true, 0, R.drawable.ic_launcher);
			}
			title.setText(item.getName());
		}
		

		public class LikeCategoryAsynTask extends AsyncTask<Integer, String, Output> {

			JsonCategory jsonCategory;

			@Override
			protected void onPreExecute() {
				jsonCategory = new JsonCategory();
				super.onPreExecute();
			}

			@Override
			protected Output doInBackground(Integer... params) {
				Debug.e("User: " + SystemConfig.user_id + " Session: " + SystemConfig.session_id + " device: "
						+ SystemConfig.device_id);
				out = jsonCategory.paserLikeCategory(SystemConfig.user_id, SystemConfig.session_id,
						SystemConfig.device_id, id_position);
				return out;
			}

			@Override
			protected void onPostExecute(Output result) {
				if (result.getCode() == Constan.getIntProperty("success")) {
					Debug.showAlert(context, result.getMessage());
				}
				super.onPostExecute(result);
			}
		}

	}

}
