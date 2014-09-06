package nz.ac.aut.hss.client.app;

import android.content.Context;
import android.telephony.gsm.SmsManager;
import nz.ac.aut.hss.client.communication.SMSSender;

public class SmsSender implements SMSSender {

	Context context;

	public SmsSender(Context context) {
		this.context = context;
	}

	@Override
	public void send(String phone, String content) {
		ClientApplication.getInstance().displayError("Sending " + content + " to " + phone, null);

		SmsManager smsManager = SmsManager.getDefault();

//		PendingIntent sentPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), 0);
//		PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED"), 0);
//		smsManager.sendTextMessage(phone, null, content, sentPendingIntent, deliveredPendingIntent);
		smsManager.sendTextMessage(phone, null, content, null, null);
		ClientApplication.getInstance().displayError("Sent " + content + " to " + phone, null);

	}

}
