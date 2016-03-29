package com.smile.studio.menu;

import com.example.onlinemarketing.R;
import com.lib.recycler.OnItemTouchListener;
import com.onlinemarketing.activity.MainActivity;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.fragment.FragmentCategory;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentDrawerLeft extends Fragment {

	private RecyclerView recyclerView = null;
	private ActionBarDrawerToggle mDrawerToggle = null;
	private DrawerLayout mDrawerLayout = null;
	private NavigationDrawerLeftAdapter adapter = null;
	private View containerView = null;
	private FragmentDrawerListener drawerListener = null;

	Context context;

	public FragmentDrawerLeft() {

	}

	public void setDrawerListener(FragmentDrawerListener listener) {
		this.drawerListener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_navigation_drawer_left, container, false);
		context = layout.getContext();
		recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

		adapter = new NavigationDrawerLeftAdapter(getActivity(), SystemConfig.oOputproduct.getCategoryVO());
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerView.addOnItemTouchListener(
				new OnItemTouchListener(getActivity(), recyclerView, new OnItemTouchListener.ClickListener() {
					@Override
					public void onClick(View view, int position) {
						MainActivity.id_category = SystemConfig.oOputproduct.getCategoryVO().get(position).getId();
						MainActivity.status = SystemConfig.statusCategoryProduct;
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction()
								.replace(R.id.container_body, FragmentCategory.newInstance(position + 1)).commit();
						mDrawerLayout.closeDrawer(containerView);

					}

					@Override
					public void onLongClick(View view, int position) {

					}
				}));
		return layout;
	}

	public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
		containerView = getActivity().findViewById(fragmentId);
		mDrawerLayout = drawerLayout;
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActivity().supportInvalidateOptionsMenu();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);

				if (!isAdded()) {
					return;
				}
				getActivity().supportInvalidateOptionsMenu();
			}

			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				super.onDrawerSlide(drawerView, slideOffset);
				toolbar.setAlpha(1 - slideOffset / 2);
			}

		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

	}

	public interface FragmentDrawerListener {
		public void onDrawerItemSelected(View view, int position);

	}
}