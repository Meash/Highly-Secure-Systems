package nz.ac.aut.hss.client.app;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;
import nz.ac.aut.hss.client.communication.*;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.PrivateKey;

public class ClientApplication extends Application implements MobileApp {

	private String oneTimePasseword;
	private String phoneNo;
	private AndroidKeyStore keyStore;
	private static ClientApplication instance = null;
	public ServerCommunication serverComm;
	private ClientCommunications communications;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		keyStore = new AndroidKeyStore(getApplicationContext());
	}

	@Override
	public String getOneTimePassword() {
		return oneTimePasseword;
	}


	@Override
	public String getPhoneNumber() {
		return phoneNo;
	}

	public void initCommunications(String server, int port) {
		try {
			final KeyPair keyPair = keyStore.loadOrCreateAndSaveKeyPair();
			final PrivateKey privateKey = keyPair.getPrivate();

			serverComm = new ServerCommunication(server, port, this, keyPair);
			Toast.makeText(getApplicationContext(), "server connected", Toast.LENGTH_LONG).show();

			final SMSSender smsSender = new SmsSender(this);
			this.communications = new ClientCommunications(serverComm, smsSender, privateKey);
		} catch (KeyStoreException | IOException | InterruptedException e) {
			displayError(e.getClass().getSimpleName() + " while initializing server communication: " + e.getMessage(),
					e);
//			throw new RuntimeException(e);
		}
	}

	@Override
	public void displayError(String message, Throwable tr) {
		if (tr != null) Log.i("GENERAL", message, tr);
		else Log.i("GENERAL", message);
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public void setOneTimePasseword(String oneTimePasseword) {
		this.oneTimePasseword = oneTimePasseword;
	}

	public static ClientApplication getInstance() {
		return instance;
	}

	public AndroidKeyStore getKeyStore() {
		return keyStore;
	}


	public ClientCommunications getCommunications() {
		return communications;
	}
}
