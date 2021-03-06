package nz.ac.aut.hss.client.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import nz.ac.aut.hss.client.communication.ClientCommunication;
import nz.ac.aut.hss.client.communication.ClientCommunications;
import nz.ac.aut.hss.client.communication.ClientDoesNotExistException;
import nz.ac.aut.hss.distribution.protocol.Message;
import nz.ac.aut.hss.distribution.util.ObjectSerializer;

import java.io.IOException;

public class SMSReceiver extends BroadcastReceiver {
	private final ClientCommunications communications;
	private final ClientApplication app;
	private final ObjectSerializer serializer;

	public SMSReceiver() {
		app = ClientApplication.getInstance();
		this.communications = app.getCommunications();
		serializer = new ObjectSerializer();
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		// Retrieves a map of extended data from the intent.
		final Bundle bundle = intent.getExtras();

		try {

			if (bundle != null) {
				ClientApplication.getInstance().displayError("Received something", null);

				final Object[] pdusObj = (Object[]) bundle.get("pdus");

				for (final Object aPdusObj : pdusObj) {

					SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
					String phoneNumber = currentMessage.getDisplayOriginatingAddress();

					String message = currentMessage.getDisplayMessageBody();

					ClientApplication.getInstance()
							.displayError("Received message from " + phoneNumber + ": " + message, null);
					Log.i("SmsReceiver", "senderNum: " + phoneNumber + "; message: " + message);
					
					final ClientCommunication communication;
					try {
						communication = communications.getOrCreate(phoneNumber);
					} catch (ClientDoesNotExistException e) {
						app.displayError("Client " + phoneNumber + " does not exist", e);
						return;
					}

					String plain;
					boolean confidential, authenticated;
					try {
						final Object msgObj = serializer.deserialize(message);
						if (!(msgObj instanceof Message)) {
							app.displayError("Expected class Message, got " + msgObj.getClass().getName(), null);
						}
						Message msg = (Message) msgObj;
						plain = communication.getPlainMessage(msg);
						confidential = communication.isMessageConfidential(msg);
						authenticated = communication.isMessageAuthentic(msg);
					} catch (Exception e) {
						plain = message;
						confidential = authenticated = false;
					}

					ClientApplication.getInstance().displayError(
							"Received " + plain + " (c=" + confidential + ", a=" + authenticated + ")", null);

					Intent receiveMessage = new Intent(context.getApplicationContext(), ReceiveMessage.class);
					receiveMessage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					receiveMessage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					receiveMessage.putExtra(ReceiveMessage.BODY, plain);
					receiveMessage.putExtra(ReceiveMessage.CONFIDENTIAL, confidential);
					receiveMessage.putExtra(ReceiveMessage.AUTHENTICATED, authenticated);
					context.startActivity(receiveMessage);

				} // end for loop
			} // bundle is null
		} catch (Exception e) {
			app.displayError("Communication error: " + e.getMessage(), e);
		}
	}
}
