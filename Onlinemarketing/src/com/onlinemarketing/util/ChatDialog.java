package com.onlinemarketing.util;

import java.util.ArrayList;

import com.example.onlinemarketing.R;
import com.lib.Debug;
import com.onlinemarketing.adapter.ListMessageAdapter;
import com.onlinemarketing.config.Constan;
import com.onlinemarketing.config.SystemConfig;
import com.onlinemarketing.json.JsonMessage;
import com.onlinemarketing.object.MessageVO;
import com.onlinemarketing.object.OutputMessage;
import com.smile.android.gsm.utils.AndroidUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ChatDialog {

	Context context;
	static Dialog dialogListMsg;
	ListView listviewChat;
	ArrayList<MessageVO> listMessage = new ArrayList<MessageVO>();
	public static OutputMessage oOputMsg;
	ListMessageAdapter adapterListMessage;
	static Dialog dialog;
	Button btn_SMS, btnListChat, btnSend, btnOk, btnCancle;
	ImageView blockchat;
	EditText editMessage, editSendMessage;
	TextView txtShowMessageChat, txtalert;
	TableLayout tab;
	static String messageMsg;
	int idProduct;
	static int chat_id_room, abc;
	static int id_send;
	static int message_id;
	int status_callWS = 0;
	static int id_remove;

	public ChatDialog(Context context) {
		super();
		this.context = context;
	}

	public ChatDialog(Context context, int id_product) {
		this.idProduct = id_product;
	}

	public void run(int status) {
		if (status == SystemConfig.statusListMessage) {
			status_callWS = SystemConfig.statusListMessage;
			new MessageAsystask().execute(status);
		} else if (status == SystemConfig.statusSendMessage) {
			status_callWS = SystemConfig.statusSendMessage;
			new MessageAsystask().execute(status);
		} else if (status == SystemConfig.statusGetHistoryMessage) {
			status_callWS = SystemConfig.statusGetHistoryMessage;
			new MessageAsystask().execute(status);
		} else if (status == SystemConfig.statusDeleteMessage) {
			status_callWS = SystemConfig.statusDeleteMessage;
			new MessageAsystask().execute(status);
		}
		else if (status == SystemConfig.statusDeleteGroupMessage) {
			status_callWS = SystemConfig.statusDeleteGroupMessage;
			new MessageAsystask().execute(status);
		}
		else if (status == SystemConfig.statusBlockUser) {
			status_callWS = SystemConfig.statusBlockUser;
			new MessageAsystask().execute(status);
		}
		
	}

	public void dialogListMessage() {

		dialogListMsg = new Dialog(context);
		dialogListMsg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogListMsg.setContentView(R.layout.dialog_list_message);
		listviewChat = (ListView) dialogListMsg.findViewById(R.id.listChat);
		listviewChat.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// idProduct = listMessage.get(position).getReceiver_id();
				chat_id_room = listMessage.get(position).getReceiver_id();
				abc = chat_id_room;
				ChatDialog chat = new ChatDialog(context);
				chat.run(SystemConfig.statusGetHistoryMessage);
				dialogChat(idProduct);
				dialogListMsg.dismiss();
			}
		});
		dialogListMsg.show();
	}

	public Dialog dialogChat(final int iduser) {
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_chat);
		// set text, button, edittext and tap
		btn_SMS = (Button) dialog.findViewById(R.id.btnMessageChat);
		btnListChat = (Button) dialog.findViewById(R.id.btnListMessagesChat);
		btnSend = (Button) dialog.findViewById(R.id.btnSendChat);
		editSendMessage = (EditText) dialog.findViewById(R.id.txtMessageChat);
		tab = (TableLayout) dialog.findViewById(R.id.tab);
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				//bat su kien thoat
			}
		});
		btnListChat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AndroidUtils.isConnectedToInternet(context)) {
					ChatDialog chat = new ChatDialog(context);
					chat.run(SystemConfig.statusListMessage);
					dialog.dismiss();
				}
			}
		});
		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				messageMsg = editSendMessage.getText().toString();
				if(iduser > 0){
					chat_id_room = iduser;
				}else{
					chat_id_room = abc;
				}
				run(SystemConfig.statusSendMessage);
//				setStyleSendMessage(editSendMessage.getText().toString(), 0);
				
			}
		});
		dialog.show();
		return dialog;
	}

	public void loadHistoryChat() { 
		try {
			listMessage = oOputMsg.getArrMessage();
			for (int i = 0; i < listMessage.size(); i++) {
				MessageVO obj = new MessageVO();
				obj = listMessage.get(i);
				if (obj.isSend()) {
					setStyleSendMessage(obj.getMessage(), obj.getId());
				} else if (!obj.isSend()) {
					setStylerecivedMessage(obj.getMessage(), obj.getUsername(),
							obj.getId());
				}
			}
		} catch (Exception e) {
			Debug.e(e.toString());
		}
	}
	
	public void setStyleSendMessage(String text, int id) {
		tab = (TableLayout) dialog.findViewById(R.id.tab);
		final TableRow tr2 = new TableRow(context.getApplicationContext());
		tr2.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.WRAP_CONTENT));

		TextView textview = new TextView(context.getApplicationContext());
		textview.setTextSize(20);
		textview.setTextColor(Color.parseColor("#A901DB"));
		textview.setText(Html.fromHtml("<b>" + "Bạn" + " : </b>" + text));
		tr2.setId(id);
		tr2.addView(textview);
		tr2.setClickable(true);
		tr2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				id_remove = v.getId();
				message_id = tr2.getId();
				tr2.removeView(v);
				dialogDelete();

			}
		});
		tab.addView(tr2);
	}

	public void setStylerecivedMessage(String text, String userName, int id) {
		tab = (TableLayout) dialog.findViewById(R.id.tab);
		final TableRow tr2 = new TableRow(context.getApplicationContext());
		tr2.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.WRAP_CONTENT,
				TableRow.LayoutParams.WRAP_CONTENT));
		TextView textview = new TextView(context.getApplicationContext());
		textview.setTextSize(20);
		textview.setTextColor(Color.parseColor("#0101DF"));
		textview.setText(Html.fromHtml("<b>" + userName + " : </b>" + text));
		tr2.setId(id);
		tr2.addView(textview);
		tr2.setClickable(true);
		tr2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				id_remove = v.getId();
				message_id = tr2.getId();
				dialogDelete();
				tr2.removeView(v);
			}
		});
		tab.addView(tr2);

	}

	public void dialogDelete() {
		dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_delete);
		dialog.setTitle("Thông Báo");
		txtalert = (TextView) dialog.findViewById(R.id.txtalert);
		txtalert.setText("Bạn Muốn xóa message!");
		txtalert.setTextSize(18);
		btnOk = (Button) dialog.findViewById(R.id.btn_Ok_Delete);
		btnCancle = (Button) dialog.findViewById(R.id.btn_Cancle_Delete);
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				run(SystemConfig.statusDeleteMessage);
				dialog.dismiss();
			}
		});
		btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public class MessageAsystask extends
			AsyncTask<Integer, Integer, OutputMessage> {
		JsonMessage message;

		// int status_callWS=0;

		@Override
		protected void onPreExecute() {
			message = new JsonMessage();
			if (SystemConfig.statusListMessage == status_callWS) {
				dialogListMessage();
			}
			super.onPreExecute();
		}

		@Override
		protected OutputMessage doInBackground(Integer... params) {
			switch (params[0]) {
			case SystemConfig.statusListMessage:
				oOputMsg = message.paseListNewMessage(SystemConfig.user_id,
						SystemConfig.session_id, SystemConfig.device_id);
				listMessage = oOputMsg.getArrMessage();
				break;
			case SystemConfig.statusSendMessage:
				oOputMsg = message.SendMessage(SystemConfig.user_id,
						SystemConfig.session_id, SystemConfig.device_id,
						chat_id_room, messageMsg);
				break;
			case SystemConfig.statusGetHistoryMessage:
				oOputMsg = message.paseListHistoryMessage(SystemConfig.user_id,
						SystemConfig.session_id, SystemConfig.device_id,
						chat_id_room);
				break;
			case SystemConfig.statusDeleteMessage:
				oOputMsg = message.paserDeleteMsg(SystemConfig.user_id,
						SystemConfig.session_id, SystemConfig.device_id,
						message_id);
			case SystemConfig.statusDeleteGroupMessage:
				oOputMsg = message.paserDeleteGroupMsg(SystemConfig.user_id,
						SystemConfig.session_id, SystemConfig.device_id,
						idProduct);
			case SystemConfig.statusBlockUser:
				oOputMsg = message.paserBlockUser(SystemConfig.user_id,
						SystemConfig.session_id, SystemConfig.device_id,
						idProduct);
			}
			return oOputMsg;
		}

		@Override
		protected void onPostExecute(OutputMessage result) {
			if (result.getCode() == Constan.getIntProperty("success") && status_callWS == SystemConfig.statusListMessage) {
				adapterListMessage = new ListMessageAdapter(context,R.layout.item_list_message, listMessage);
				listviewChat.setAdapter(adapterListMessage);
			}else if (result.getCode() == Constan.getIntProperty("success") && status_callWS == SystemConfig.statusGetHistoryMessage) {
				loadHistoryChat();
			}else if (result.getCode() == Constan.getIntProperty("success")&& status_callWS == SystemConfig.statusSendMessage) {
				id_send = Integer.parseInt(result.getMessage_id_send());
				setStyleSendMessage(editSendMessage.getText().toString(), id_send);
				editSendMessage.setText(null);
			}
			else if (result.getCode() == Constan.getIntProperty("success")&& status_callWS == SystemConfig.statusDeleteMessage) {
				for (int i = 0; i < listMessage.size(); i++) {
					MessageVO message = listMessage.get(i);
					if(message.getId() == id_send);{
						listMessage.remove(i);
					}
				}
//				tab.removeView(tr2);
				oOputMsg.setArrMessage(listMessage);
				loadHistoryChat();
			}
		}
	}

}
