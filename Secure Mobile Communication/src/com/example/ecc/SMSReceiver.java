package com.example.ecc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver  {

	final SmsManager sms = SmsManager.getDefault();

	@Override
	public void onReceive(Context context, Intent intent) {

		// Retrieves a map of extended data from the intent.
		final Bundle bundle = intent.getExtras();

		try {

			if (bundle != null) {

				final Object[] pdusObj = (Object[]) bundle.get("pdus");

				for (final Object aPdusObj : pdusObj) {

					SmsMessage currentMessage = SmsMessage
							.createFromPdu((byte[]) aPdusObj);
					String phoneNumber = currentMessage
							.getDisplayOriginatingAddress();

					String message = currentMessage.getDisplayMessageBody();

					Log.i("SmsReceiver", "senderNum: " + phoneNumber
							+ "; message: " + message);

					// Show Alert
					/*
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(context, "senderNum: "
							+ senderNum + ", message: " + message, duration);
					toast.show();*/

					Intent receiveMessage = new Intent(context.getApplicationContext(), ReceiveMessage.class);
					receiveMessage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					receiveMessage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					receiveMessage.putExtra("MESSAGEBODY", message);
					context.startActivity(receiveMessage);

				} // end for loop
			} // bundle is null

		} catch (Exception e) {
			Log.e("SmsReceiver", "Exception smsReceiver" + e);

		}
	}
}
