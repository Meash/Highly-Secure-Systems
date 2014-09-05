package com.example.ecc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nz.ac.aut.hss.client.communication.CommunicationDisplay;
import nz.ac.aut.hss.client.communication.SMSListener;
import nz.ac.aut.hss.client.communication.SMSReceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReciver extends BroadcastReceiver implements SMSReceiver {

	final SmsManager sms = SmsManager.getDefault();
	List<SMSListener> boardcastListeners = new ArrayList<SMSListener>();
	Map<String, List<SMSListener>> listeners = new HashMap<String, List<SMSListener>>();

	@Override
	public void onReceive(Context context, Intent intent) {

		// Retrieves a map of extended data from the intent.
		final Bundle bundle = intent.getExtras();

		try {

			if (bundle != null) {

				final Object[] pdusObj = (Object[]) bundle.get("pdus");

				for (int i = 0; i < pdusObj.length; i++) {

					SmsMessage currentMessage = SmsMessage
							.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage
							.getDisplayOriginatingAddress();

					String senderNum = phoneNumber;
					String message = currentMessage.getDisplayMessageBody();

					Log.i("SmsReceiver", "senderNum: " + senderNum
							+ "; message: " + message);

					distribute(senderNum, message, boardcastListeners);
					List<SMSListener> specificListeners = listeners
							.get(senderNum);
					if (specificListeners != null)
						distribute(senderNum, message, specificListeners);

					// Show Alert
					/*
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(context, "senderNum: "
							+ senderNum + ", message: " + message, duration);
					toast.show();*/
					
					Intent reciveMessage = new Intent(context.getApplicationContext(),ReciveMessage.class);
					reciveMessage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					reciveMessage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					reciveMessage.putExtra("MESSAGEBODY", message);
					context.startActivity(reciveMessage);

				} // end for loop
			} // bundle is null

		} catch (Exception e) {
			Log.e("SmsReceiver", "Exception smsReceiver" + e);

		}
	}

	@Override
	public void addListener(SMSListener listener) {
		boardcastListeners.add(listener);
	}

	@Override
	public void addListener(String phoneNumber, SMSListener listener) {
		List<SMSListener> list = listeners.get(phoneNumber);
		if (list == null) {
			list = new ArrayList<SMSListener>();
			listeners.put(phoneNumber, list);
		}
		list.add(listener);
	}

	public void distribute(final String phone, final String message,
			final List<SMSListener> listeners) {
		for (SMSListener smsListener : listeners) {
			smsListener.receive(phone, message);
		}
	}

}
