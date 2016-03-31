package com.onlinemarketing.adapter;

import java.util.List;

import com.androidquery.AQuery;
import com.example.onlinemarketing.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PostRecycleAdapter extends RecyclerView.Adapter<PostRecycleAdapter.ViewHolder> {
	private List<String> linkImg;
	private List<Bitmap> bit;
	Context context;
	AQuery aQuery;

	// contructer
	public PostRecycleAdapter(Context context, List<String> linkImg, List<Bitmap> bit) {
		this.linkImg = linkImg;
		this.bit = bit;
		this.context = context;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return linkImg.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		// TODO Auto-generated method stub
		// holder.mTextView.setText(linkImg[position]);
		// set image o day
		String link= linkImg.get(position);
		Bitmap bitmap = bit.get(position);
		holder.imgViewIcon.setImageBitmap(bitmap);

	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// TODO Auto-generated method stub
		// create a new view
		View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imgproduct_post, null);
		ViewHolder viewHolder = new ViewHolder(itemLayoutView);
		return viewHolder;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgViewIcon;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.img_showproductPost);
        }
    }

}
