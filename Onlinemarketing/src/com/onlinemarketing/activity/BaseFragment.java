package com.onlinemarketing.activity;

import com.lib.Debug;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.smile.android.gsm.utils.AndroidDeviceInfo;
import com.smile.android.gsm.utils.AndroidUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;

public class BaseFragment extends Fragment{
	Dialog objdealog;
	Context context;
	public boolean isConnect(Context context){
		Constan.context = getActivity().getApplicationContext();
		SystemConfig.device_id = AndroidDeviceInfo.getAndroidID(context);
		Debug.e("DeviceId: " + SystemConfig.device_id);
		boolean isconnect = AndroidUtils.isConnectedToInternet(context);
		if (!isconnect) {
			showProgressDialogCheckInternet();
		}
		return isconnect;
	}
	public void showProgressDialogCheckInternet() {
		@SuppressWarnings("deprecation")
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
		try {
//			alertDialog.setCancelable(false);
			alertDialog.setTitle(Constan.getProperty("ErrorConnectInterNet"));
			alertDialog.setMessage(Constan.getProperty("ErrorConnectInterNetMessage"))
					.setCancelable(false).setPositiveButton(Constan.getProperty("Cancel"), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}

					}).setNegativeButton(Constan.getProperty("Ok"), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
		} catch (Exception e) {
			Debug.e(e.toString());
		}
		alertDialog.show();
	}

}
