package com.onlinemarketing.asystask;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lib.Debug;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.onlinemarketing.activity.BaseActivity;
import com.onlinemarketing.activity.PostActivity;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.logingoogle.LocationAddress;
import com.onlinemarketing.object.Output;
import com.onlinemarketing.object.OutputGoogle;
import com.onlinemarketing.util.Util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class UploadProduct extends BaseActivity{
	ProgressDialog prgDialog;
	private Context context;
	private String linkWS;
	private int status;
	String jsonResponse ;
	OutputGoogle outputgoogle;
	public UploadProduct(Context context, int status) {
		super();
		this.context = context;
		this.status = status ;
		prgDialog = new ProgressDialog(context);
		 // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
	}
 
	 public String postFile(RequestParams params, String linkUpload){
				// TODO Auto-generated method stub
				// Show Progress Dialog
		        AsyncHttpClient client = new AsyncHttpClient();
		        client.get(linkUpload,params ,new AsyncHttpResponseHandler() {
		        	 @Override
		        	 public void onSuccess(String response) {
		                 // Hide Progress Dialog
		                 try {
		                	 Debug.e("ket quả dat duoc sau 2,5 ngày: "+response);
		                         JSONObject obj = new JSONObject(response);
		                         if(obj.getInt("status") == 200){
		                         }
		                         
		                         else{
		                             Toast.makeText(context.getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
		                         }
		                 } catch (JSONException e) {
		                     Toast.makeText(context.getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
		                     e.printStackTrace();
		                 }
		                 
		             }
		             // When the response returned by REST has Http response code other than '200'
		             @Override
		             public void onFailure(int statusCode, Throwable error,
		                 String content) {
		                 // Hide Progress Dialog 
		                 // When Http response code is '404'
		                 if(statusCode == 404){
		                     Toast.makeText(context.getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
		                 } 
		                 // When Http response code is '500'
		                 else if(statusCode == 500){
		                     Toast.makeText(context.getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
		                 } 
		                 // When Http response code other than 404, 500
		                 else{
		                     Toast.makeText(context.getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
		                 }
		             }
		         });
		         return jsonResponse;
			}
	
}
