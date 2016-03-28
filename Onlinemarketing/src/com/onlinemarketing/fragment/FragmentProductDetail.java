package com.onlinemarketing.fragment;

import com.example.onlinemarketing.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentProductDetail extends Fragment {

	private int img ;

	
	public FragmentProductDetail(int img) {
		super();
		this.img = img;
	}

@Override
public View onCreateView(LayoutInflater inflater,
		@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.fragment_produc_detail, container, false);
	return view;
}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ImageView img = (ImageView) view.findViewById(R.id.imageView1);
		img.setImageResource(this.img);
		
	}
}
