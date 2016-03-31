package com.onlinemarketing.fragment;

import java.util.List;

import com.androidquery.AQuery;
import com.example.onlinemarketing.R;
import com.onlinemarketing.object.ProductVO;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentProductDetail extends Fragment {

	AQuery aQuery = null;
	String arrayImg;
	public FragmentProductDetail(String arrayImg) {
		super();
		this.arrayImg = arrayImg;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_produc_detail, container, false);
		aQuery = new AQuery(view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ImageView img = (ImageView) view.findViewById(R.id.imageView1);
		// img.setImageResource(this.img);
		Bitmap bitmap = null ;
			bitmap = aQuery.getCachedImage(arrayImg);
			if (bitmap != null) {
				aQuery.id(img).image(bitmap);
			} else {
				aQuery.id(img).image(arrayImg, true, true, 0, R.drawable.ic_launcher);
			}
		

	}
}
