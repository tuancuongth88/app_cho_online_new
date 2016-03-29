package com.onlinemarketing.logingoogle;
import com.example.onlinemarketing.R;
import com.google.android.gms.plus.PlusOneButton;
import com.google.android.gms.plus.PlusOneButton.OnPlusOneClickListener;

import android.content.Context;
import android.content.Intent;
public class Dialog  extends android.app.Dialog  {
	private PlusOneButton button = null;
	public IAction action = null;

	public interface IAction {
		public void perform(Intent intent);
	}

	public Dialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	public Dialog(Context context, int themeResId) {
		super(context, themeResId);
		init();
	}

	public Dialog(Context context) {
		super(context);
		init();
	}

	public void init() {
		setContentView(R.layout.activity_login);
		button = (PlusOneButton) findViewById(R.id.googlebtn);
		button.initialize("https://play.google.com/store/apps/details?id=com.freesmartapps.fancy.dress.changer",
				new OnPlusOneClickListener() {

					@Override
					public void onPlusOneClick(Intent paramIntent) {
						if (action != null) {
							action.perform(paramIntent);
						}
					}
		});
	}
}
