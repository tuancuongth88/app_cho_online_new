package com.onlinemarketing.activity;

import com.lib.Debug;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.smile.android.gsm.utils.AndroidDeviceInfo;
import com.smile.android.gsm.utils.AndroidUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment{
	Dialog objdealog;

	public boolean isConnect() {
		SystemConfig.device_id = AndroidDeviceInfo.getAndroidID(Constan.context);
		Debug.e("DeviceId: " + SystemConfig.device_id);
		boolean isconnect = AndroidUtils.isConnectedToInternet(Constan.context);
		if (!isconnect) {
			showProgressDialogCheckInternet();
		}
		return isconnect;
	}

	public void showProgressDialogCheckInternet() {
		@SuppressWarnings("deprecation")
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(Constan.context, AlertDialog.THEME_HOLO_LIGHT);
		try {
			// alertDialog.setCancelable(false);
			alertDialog.setTitle(Constan.getProperty("ErrorConnectInterNet"));
			alertDialog.setMessage(Constan.getProperty("ErrorConnectInterNetMessage")).setCancelable(false)
					.setPositiveButton(Constan.getProperty("Cancel"), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Debug.e("hủy");
						}

					}).setNegativeButton(Constan.getProperty("Ok"), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								Debug.e("ket noi");
								if (!isConnect()) {
									showProgressDialogCheckInternet();
									dialog.dismiss();
								} else {
									PackageManager packageManager = Constan.context.getPackageManager();
									ActivityInfo info = packageManager
											.getActivityInfo(getActivity().getComponentName(), 0);
									Debug.e("Activity name:" + info.name);
									Intent intent = new Intent(Constan.context, Class.forName(info.name));
									startActivity(intent);
									dialog.dismiss();
									getActivity().finish();
								}
								
							} catch (Exception e) {
								// TODO: handle exception
								Debug.e("loi cmnr:" + e);
							}
						}

					});
		} catch (Exception e) {
			Debug.e(e.toString());
		}
		alertDialog.show();
	}

}
