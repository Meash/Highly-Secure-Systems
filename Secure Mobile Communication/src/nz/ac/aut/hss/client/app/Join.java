package nz.ac.aut.hss.client.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
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
	}

	public void onJoin(View view) {
		final EditText phoneNoInput = (EditText) findViewById(R.id.phoneText);
		instance.setPhoneNo(phoneNoInput.getText().toString());
		KeyStore keyStore = instance.getKeyStore();
		Toast.makeText(this, "generating key", Toast.LENGTH_LONG).show();
		try {
			keyStore.loadOrCreateAndSaveKeyPair();
			Log.i("KEY_CREATED", "Keys loaded");
		} catch (KeyStoreException e) {
			instance.displayError("Could not load keys: " + e.getMessage());
			return;
		}

		new Thread() {
			public void run() {
				instance.initCommunications("localhost", 61001);
				instance.setPhoneNo(phoneNoInput.getText().toString());
			}
		}.start();

		Intent intent = new Intent(this, OnetimePasswordInput.class);
		startActivity(intent);
	}


}
