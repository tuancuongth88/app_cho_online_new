package com.onlinemarketing.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lib.Debug;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.object.Output;
import com.smile.android.gsm.utils.AndroidUtils;
import com.sun.mail.smtp.SMTPTransport;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Log;

public class Util {
	private static Pattern pattern;
	private static Matcher matcher;
	public static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	/**
	 * Hàm trả về JSONObject
	 * 
	 * @param url
	 *            - Truyền link URL có định dạng JSON
	 * @return - Trả về JSONOBject
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject readJsonFromUrl(String url) throws IOException,
			JSONException {
		InputStream is = new URL(url).openStream();
		try {
			// đọc nội dung với Unicode:
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			Debug.e("jsonText" + jsonText);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}
	public static Output doFileUpload(String user_id, String session_id, String device_id, String link) {
		Output output = new Output();
		File file_path = new File(link);
		try {
			StringBuilder request = new StringBuilder(SystemConfig.API );
			request.append(SystemConfig.Upload_image);
			HttpClient client = new DefaultHttpClient();
			// use your server path of php file
			HttpPost post = new HttpPost(request.toString());


			FileBody bin1 = new FileBody(file_path);
			Log.e("Enter", "Filebody complete " + bin1);

			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("image_url[]", bin1);
			reqEntity.addPart("user_id", new StringBody(user_id));
			reqEntity.addPart("device_id", new StringBody(device_id));
			reqEntity.addPart("session_id", new StringBody(session_id));
			post.setEntity(reqEntity);
			HttpResponse response = client.execute(post);
			HttpEntity resEntity ;
			resEntity = response.getEntity();
			Log.e("user_id", "user_id:"+ user_id+ "/n device_id: "+ device_id+ "/nsession_id:"+session_id );
			Log.e("Enter", "Get Response");
			try {
				String response_str = EntityUtils.toString(resEntity);
				Log.e( "Get Response",response_str);
				if (resEntity != null) {
					JSONObject jsonObject = new JSONObject(response_str);
					output.setCode(jsonObject.getInt("code"));					
					output.setMessage(jsonObject.getString("message"));
					output.setSession_id(jsonObject.getString("session_id"));
					JSONArray jsonArrAvatar = jsonObject.getJSONArray("data");
					if (output.getCode() == Constan.getIntProperty("success")) {
						for (int i = 0; i < jsonArrAvatar.length(); i++) {
							JSONObject objjsonAvatar = jsonArrAvatar.getJSONObject(i);
							if(objjsonAvatar.has("image_url"))
								SystemConfig.Avatar =  objjsonAvatar.getString("image_url");
						}
					}
				}
			} catch (Exception ex) {
				Log.e("Debug", "error: " + ex.getMessage(), ex);
			}
		} catch (Exception e) {
			Log.e("Upload Exception", "");
		}
		return output;
	}
	public static Output UploadImageProduct(String user_id, String session_id, String device_id, String link) {
		Output output = new Output();
		File file_path = new File(link);
		try {
			StringBuilder request = new StringBuilder(SystemConfig.API );
			request.append(SystemConfig.Upload_image);
			request.append("/"+ SystemConfig.Product);
			HttpClient client = new DefaultHttpClient();
			// use your server path of php file
			HttpPost post = new HttpPost(request.toString());


			FileBody bin1 = new FileBody(file_path);
			Log.e("Enter", "Filebody complete " + bin1);

			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("image_url[]", bin1);
			reqEntity.addPart("user_id", new StringBody(user_id));
			reqEntity.addPart("device_id", new StringBody(device_id));
			reqEntity.addPart("session_id", new StringBody(session_id));
			post.setEntity(reqEntity);
			HttpResponse response = client.execute(post);
			HttpEntity resEntity ;
			resEntity = response.getEntity();
			Log.e("user_id", "user_id:"+ user_id+ "/n device_id: "+ device_id+ "/nsession_id:"+session_id );
			Log.e("Enter", "Get Response");
			try {
				String response_str = EntityUtils.toString(resEntity);
				Log.e( "Get Response",response_str);
				if (resEntity != null) {
					JSONObject jsonObject = new JSONObject(response_str);
					output.setCode(jsonObject.getInt("code"));					
					output.setMessage(jsonObject.getString("message"));
					output.setSession_id(jsonObject.getString("session_id"));
					JSONArray jsonArrAvatar = jsonObject.getJSONArray("data");
					if (output.getCode() == Constan.getIntProperty("success")) {
						for (int i = 0; i < jsonArrAvatar.length(); i++) {
							JSONObject objjsonAvatar = jsonArrAvatar.getJSONObject(i);
							if(objjsonAvatar.has("image_url"))
								SystemConfig.productImage =  objjsonAvatar.getString("image_url");
							
						}
					}
				}
			} catch (Exception ex) {
				Log.e("Debug", "error: " + ex.getMessage(), ex);
			}
		} catch (Exception e) {
			Log.e("Upload Exception", "");
		}
		return output;
	}
	public static Output UploadImageArr(String user_id, String session_id, String device_id, List<String> link) {
		Output output = new Output();
		
		try {
			String linkUploadImage = SystemConfig.API+SystemConfig.Upload_image+"/"+SystemConfig.Product;
			StringBuilder request = new StringBuilder(linkUploadImage );
			request.append(SystemConfig.Upload_image);
			HttpClient client = new DefaultHttpClient();
			// use your server path of php file
			HttpPost post = new HttpPost(request.toString());
			MultipartEntity reqEntity = new MultipartEntity();
			File file_path = new File(link.get(0));
//			for (int i = 0; i < link.size(); i++) {
//			}
			FileBody bin1 = new FileBody(file_path);
			reqEntity.addPart("image_url[]", bin1);
			reqEntity.addPart("user_id", new StringBody(user_id));
			reqEntity.addPart("device_id", new StringBody(device_id));
			reqEntity.addPart("session_id", new StringBody(session_id));
			post.setEntity(reqEntity);
			HttpResponse response = client.execute(post);
			HttpEntity resEntity ;
			resEntity = response.getEntity();
			Log.e("user_id", "user_id:"+ user_id+ "/n device_id: "+ device_id+ "/nsession_id:"+session_id );
			Log.e("Enter", "Get Response");
			try {
				String response_str = EntityUtils.toString(resEntity);
				Log.e( "Get Response",response_str);
				if (resEntity != null) {
					JSONObject jsonObject = new JSONObject(response_str);
					output.setCode(jsonObject.getInt("code"));					
					output.setMessage(jsonObject.getString("message"));
					output.setSession_id(jsonObject.getString("session_id"));
					JSONArray jsonArrAvatar = jsonObject.getJSONArray("data");
					List<String> nameImageUpload = new ArrayList<String>();
					if (output.getCode() == Constan.getIntProperty("success")) {
						for (int i = 0; i < jsonArrAvatar.length(); i++) {
							JSONObject objjsonAvatar = jsonArrAvatar.getJSONObject(i);
							if(objjsonAvatar.has("image_url")){
								  String name =objjsonAvatar.getString("image_url");
								  nameImageUpload.add(name);
							}
						}
						output.setImage(nameImageUpload);
					}
				}
			} catch (Exception ex) {
				Log.e("Debug", "error: " + ex.getMessage(), ex);
			}
		} catch (Exception e) {
			Log.e("Upload Exception", "");
		}
		return output;
	}
	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	/**
	 * MÃ£ hÃ³a MD5
	 * 
	 * @param text
	 *            chuá»—i String Chuyá»�n vÃ o
	 * @return tráº£ vá»� MD5
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String hash(String text) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		MessageDigest md;
		md = MessageDigest.getInstance("MD5");
		byte[] md5hash = new byte[32];
		md.update(text.getBytes("iso-8859-1"), 0, text.length());
		md5hash = md.digest();
		return convertToHex(md5hash);
	}

	// Email Pattern
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

	/**
	 * Validate Email with regular expression
	 * 
	 * @param email
	 * @return true for Valid Email and false for Invalid Email
	 */
	public static boolean validate(String email) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();

	}

	/**
	 * Checks for Null String object
	 * 
	 * @param txt
	 * @return true for not null and false for null String object
	 */
	public static boolean isNotNull(String txt) {
		return txt != null && txt.trim().length() > 0 ? true : false;
	}
	public static HttpGet httpget;
	public static HttpPost httpPost;
	public static HttpDelete httpDelete;
	public static HttpResponse response ;
	// ham connecet webservice.
	
	public static String getjSonUrl(String link, int status) {
		StringBuffer objbuffer = new StringBuffer();

		HttpClient httpcliend = new DefaultHttpClient();
		if (status == 1) {
		 	 httpget = new HttpGet(link);
		}
		if (status == 2) {
			 httpPost = new HttpPost(link);
		}
		if (status == 3) {
			 httpDelete = new HttpDelete(link);
		}
		try {
			switch (status) {
			case 1:
				response = httpcliend.execute(httpget);
				break;
			case 2:
				response =httpcliend.execute(httpPost);
				break;
			case 3:
				response =httpcliend.execute(httpDelete);
				break;
			}
			
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream coInputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(coInputStream));
				String line;
				while ((line = reader.readLine()) != null) {
					objbuffer.append(line);
				}
			} else {
				Debug.e(link);
				Debug.e("Fiale to jdklfajlkd");
			}
		} catch (Exception e) {
			Debug.e(e.toString());
		}
		return objbuffer.toString();
	}
	
	 public static void Send(final String username, final String password, String recipientEmail, String title, String message) throws AddressException, MessagingException {
		   Send(username, password, recipientEmail, "", title, message);
	    }

	    /**
	     * Send email using GMail SMTP server.
	     *
	     * @param username GMail username
	     * @param password GMail password
	     * @param recipientEmail TO recipient
	     * @param ccEmail CC recipient. Can be empty if there is no CC recipient
	     * @param title title of the message
	     * @param message message to be sent
	     * @throws AddressException if the email address parse failed
	     * @throws MessagingException if the connection is dead or not in the connected state or if the message is not a MimeMessage
	     */
	    public static void Send(final String username, final String password, String recipientEmail, 
	    		String ccEmail, String title, String message) 
	    		throws AddressException, MessagingException {
//	        Security.addProvider(new Provider());
	        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	        // Get a Properties object
	        Properties props = System.getProperties();
	        props.setProperty("mail.smtps.host", "smtp.gmail.com");
	        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
	        props.setProperty("mail.smtp.socketFactory.fallback", "false");
	        props.setProperty("mail.smtp.port", "465");
	        props.setProperty("mail.smtp.socketFactory.port", "465");
	        props.setProperty("mail.smtps.auth", "true");

	        /*
	        If set to false, the QUIT command is sent and the connection is immediately closed. If set 
	        to true (the default), causes the transport to wait for the response to the QUIT command.

	        ref :   http://java.sun.com/products/javamail/javadocs/com/sun/mail/smtp/package-summary.html
	                http://forum.java.sun.com/thread.jspa?threadID=5205249
	                smtpsend.java - demo program from javamail
	        */
	        props.put("mail.smtps.quitwait", "false");

	        Session session = Session.getInstance(props, null);

	        // -- Create a new message --
	        final MimeMessage msg = new MimeMessage(session);

	        // -- Set the FROM and TO fields --
	        msg.setFrom(new InternetAddress(username + "@gmail.com"));
	        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));

	        if (ccEmail.length() > 0) {
	            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail, false));
	        }

	        msg.setSubject(title);
	        msg.setText(message, "utf-8");
	        msg.setSentDate(new Date());

	        SMTPTransport t = (SMTPTransport)session.getTransport("smtps");

	        t.connect("smtp.gmail.com", username, password);
	        t.sendMessage(msg, msg.getAllRecipients());      
	        t.close();
	    }
	    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
	        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	                bitmap.getHeight(), Config.ARGB_8888);
	        Canvas canvas = new Canvas(output);

	        final int color = 0xff424242;
	        final Paint paint = new Paint();
	        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        paint.setColor(color);
	        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
	                bitmap.getWidth() / 2, paint);
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        canvas.drawBitmap(bitmap, rect, rect, paint);
	        return output;
	    }
}
