package com.onlinemarketing.logingoogle;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.lib.Debug;
import com.loopj.android.http.RequestParams;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.OutputGoogle;
import com.onlinemarketing.util.CallWSAsynsHttp;
import com.onlinemarketing.util.Util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LocationAddress {
	private static final String TAG = "LocationAddress";

    public static void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("-");
                        }
//                        sb.subSequence(sb.length(), 1);
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        message.setData(bundle);
                        
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        result = "Unable to get address for this lat-long.";
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }

    public static OutputGoogle getLatLong(JSONObject jsonObject) {
    	OutputGoogle oOputGoogle = new OutputGoogle();
        try {

        	oOputGoogle.setLog(((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lng"));

         oOputGoogle.setLat(((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lat"));
         oOputGoogle.setAddress(((JSONArray)jsonObject.get("results")).getJSONObject(0)
                 .getString("formatted_address"));
         JSONArray jsonarr = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                 .getJSONArray("address_components");
         String city =null;
         for (int i = 0; i < jsonarr.length(); i++) {
        	 JSONObject json = jsonarr.getJSONObject(i);
        	  city = json.getString("long_name");
        	  JSONArray arrtype = json.getJSONArray("types");
        	  for (int j = 0; j < arrtype.length(); j++) {
        		  JSONObject jsontype  = arrtype.getJSONObject(j);
				if(jsontype.has("administrative_area_level_1")){
					break;
				}
        	  }
		}
         oOputGoogle.setCity(city);
        } catch (JSONException e) {
        	Debug.e(e.toString());
        }
        return oOputGoogle;
    }
  
}
