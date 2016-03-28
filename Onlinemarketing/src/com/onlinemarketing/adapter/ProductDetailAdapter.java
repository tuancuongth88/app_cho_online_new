package com.onlinemarketing.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ProductDetailAdapter extends FragmentPagerAdapter {


	private ArrayList<Fragment> fragments;
	private List<String> titles;

	public ProductDetailAdapter(FragmentManager fm, ArrayList<Fragment> fragments, List<String> titles) {
		super(fm);
		this.fragments = fragments;
		this.titles = titles;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position % titles.size());
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles.get(position);
	}

}
