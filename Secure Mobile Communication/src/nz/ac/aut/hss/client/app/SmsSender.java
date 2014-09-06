package nz.ac.aut.hss.client.app;

import nz.ac.aut.hss.client.communication.SMSSender;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;

public class SmsSender implements SMSSender{
	
	String reciverNumber;
	String message;
	Context context;
	SmsManager smsManager = SmsManager.getDefault();
	
	public SmsSender(Context context){
		this.context = context;
	}

	@Override
	public void send(String phone, String content) {
		reciverNumber = phone;
		message = content;
		PendingIntent sentPendingIntent = PendingIntent.getBroadcast
	            (context, 0, new Intent("SMS_SENT"), 0);
		PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast
	            (context, 0, new Intent("SMS_DELIVERED"), 0);
		smsManager.sendTextMessage(reciverNumber, null, message, 
				sentPendingIntent, deliveredPendingIntent);
		
	}

}
