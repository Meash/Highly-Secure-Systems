package com.example.ecc;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;

public class SmsSender {
	
	String reciverNumber;
	String message;
	Context context;
	SmsManager smsManager = SmsManager.getDefault();
	
	public SmsSender(String reciverNumber, String message, Context context){
		this.reciverNumber = reciverNumber;
		this.message = message;
		this.context = context;
	}
	
	public void sendMessage(){
		Toast.makeText(context, message, Toast.LENGTH_LONG);
		PendingIntent sentPendingIntent = PendingIntent.getBroadcast
	            (context, 0, new Intent("SMS_SENT"), 0);
		PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast
	            (context, 0, new Intent("SMS_DELIVERED"), 0);
		smsManager.sendTextMessage(reciverNumber, null, message, 
				sentPendingIntent, deliveredPendingIntent);
	}

}
