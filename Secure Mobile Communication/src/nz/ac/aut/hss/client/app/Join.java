package nz.ac.aut.hss.client.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import nz.ac.aut.hss.client.communication.CommunicationException;
import nz.ac.aut.hss.client.communication.KeyStore;

import java.security.KeyStoreException;

public class Join extends Activity {

	private ClientApplication instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join);
		Intent intent = new Intent();
		intent.setAction("android.provider.Telephony.SMS_RECEIVED");
		sendBroadcast(intent);

		instance = ClientApplication.getInstance();

		final EditText phoneNoInput = (EditText) findViewById(R.id.phoneText);
		String phoneNumber = null;
		try {
			Log.i("NUMBER", "getting emulator number");
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			phoneNumber = telephonyManager.getLine1Number();
			Log.i("NUMBER", "got emulator number");
		} catch (Throwable t) {
			Log.i("NUMBER", "couldn't get emulator number", t);
		}
		phoneNoInput.setText(phoneNumber);
	}

	public void onClick(View view) {
		final EditText phoneNoInput = (EditText) findViewById(R.id.phoneText);
		instance.setPhoneNo(phoneNoInput.getText().toString());
		KeyStore keyStore = instance.getKeyStore();
		Toast.makeText(this, "generating key", Toast.LENGTH_LONG).show();
		try {
			keyStore.loadOrCreateAndSaveKeyPair();
			Log.i("KEY_CREATED", "Keys loaded");
		} catch (KeyStoreException e) {
			instance.displayError("Could not load keys: " + e.getMessage(), e);
			return;
		}

		instance.initCommunications("10.0.2.2", 61001);

		try {
			instance.serverComm.requestJoin1();

			Intent intent = new Intent(this, OnetimePasswordInput.class);
			startActivity(intent);
		} catch (Exception e) {
			instance.displayError("Could not join server", e);
		}
	}


}
