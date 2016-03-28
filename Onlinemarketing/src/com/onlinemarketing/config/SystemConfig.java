package com.onlinemarketing.config;

import com.onlinemarketing.object.LoginRegister;
import com.onlinemarketing.object.OutputProduct;

public class SystemConfig {
	public static final String API = "http://onlinemakerting.tk/api/";
//  tên các method 
	public static final String Login = "login?";
	public static final String Register = "register?";
	public static final String LoginSocial = "login_social?";
	public static final String Category = "category";
	public static final String Setting = "setting";
	public static final String Profile = "profile";
	public static final String Produc_Save = "product_saved";
	public static final String Product_User = "product_user";
	public static final String Product = "product";
	public static final String Report = "report";
	public static final String BackList ="blacklist";
	public static final String Delete ="delete";
	public static final String Favorite ="favorite";
	public static final String product_user ="product_user";
	public static final String SearchSave ="search_saved";
	public static final String SearchLog ="search_log";
	public static final String Product_log = "product_log";
	public static final String Upload_image = "upload_image";
	public static final String TextSetting = "text";
	public static final String  Action = "action";
	public static final String  Post_product = "post_product";
	//chat dialog
	public static final String Message = "message";
	public static final String SendMessage = "send";
	public static final String HistoryMsg = "history";
	public static final String sendUserChar = "user";
	public static final String DeleteMessage = "delete";
	public static final String BlockMessage = "block";
	//asystark
	public static final int statusLogin = 1;
	public static final int statusRegister = 2;
	public static final int statusfacebook = 3;
	public static final int statusgoogle = 4;
	public static final int statuslogout = 5;
	// status json paser
	public static final int statusHomeProduct = 1;
	public static final int statusCategoryProduct = 2;
	public static final int statusListSaveProduct = 3;
	//status chatdialog on ultil
	public static final int statusListMessage = 4;
	public static final int statusSendMessage = 5;
	public static final int statusGetHistoryMessage = 6;
	public static final int statusDeleteMessage = 7;
	public static final int statusDeleteGroupMessage = 8;
	public static final int statusBlockUser = 9;
	
	//product detail
	public static final int statusProductSave = 1;
	public static final int statusErrorReport = 2;
	public static final int statusProductDetail = 3;
	
	public static final int statusProfile = 1;
	public static final int statusFavorite = 2;
	public static final int statusDeleteBackList = 1;
	public static final int statusDeleteFavorite = 2;
	public static final int statusDeleteSearch = 3;
	
	public static final int statusSetting = 1;
	public static final int statusSaveSearch = 2;
	
	public static String device_id ;
	public static String user_id = "";
	public static String session_id = "";
	
	//search type cu va moi
	public static final int statusSearchold = 1;
	public static final int statusSearchnew = 2;
	
	//key set sharedPreferences.
	public static String USER_ID = "user_id";
	public static String SESSION_ID = "session_id";
	public static String CHECKLOGIN = "check";
	public static String Email, Pass;
	public static OutputProduct oOputproduct = new OutputProduct();
	public static LoginRegister loginRegiter ;
	public static String Avatar ;
	// method
	public static int httpget = 1;
	public static int httppost = 2;
	public static int httpDelete = 3;

}
