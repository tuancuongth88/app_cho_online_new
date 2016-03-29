package com.onlinemarketing.adapter;

import com.example.onlinemarketing.R;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PostRecycleAdapter extends RecyclerView.Adapter<PostRecycleAdapter.MyViewHolder> {
	private LayoutInflater inflater;
	private Context context;

	
	
	public PostRecycleAdapter(LayoutInflater inflater, Context context) {
		super();
		this.inflater = inflater;
		this.context = context;
	}

	@Override
	public int getItemCount() {
		return 0;
	}

	@Override
	public void onBindViewHolder(MyViewHolder arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		View view = inflater.inflate(R.layout.item_imgproduct_post, parent, false);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		private ImageView imageview = null;

		public MyViewHolder(View itemView) {
			super(itemView);
			imageview = (ImageView) itemView.findViewById(R.id.img_showproductPost);
		}

	}
}
