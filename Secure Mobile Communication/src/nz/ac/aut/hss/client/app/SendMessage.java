package nz.ac.aut.hss.client.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import nz.ac.aut.hss.client.communication.ClientCommunication;
import nz.ac.aut.hss.client.communication.ClientCommunications;
import nz.ac.aut.hss.client.communication.CommunicationException;

import java.security.PublicKey;
import java.util.Map;
import java.util.Set;

public class SendMessage extends Activity {
	private ClientCommunications communications;
	private String phone;
	private ClientApplication clientApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_message);
		clientApplication = ClientApplication.getInstance();

		this.phone = clientApplication.getPhoneNumber();
		this.communications = clientApplication.getCommunications();

		try {
			updateClientList();
		} catch (CommunicationException | InterruptedException e) {
			clientApplication.displayError(
					e.getClass().getSimpleName() + " while requesting client list: " + e.getMessage(), e);
			return;
		}

		Log.i("LIST", "created dropdown list");
	}

	public void updateClientList() throws CommunicationException, InterruptedException {
		final Map<String, PublicKey> userMap;
		Log.i("LIST", "retrieving client list");
		userMap = clientApplication.serverComm.requestList();
		Log.i("LIST", "got clients list");

		Spinner spinner = (Spinner) findViewById(R.id.phoneList);
		final Set<String> keySet = userMap.keySet();
		ArrayAdapter<String> adapter =
				new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
						keySet.toArray(new String[keySet.size()]));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	public void onSend(View view) {
		EditText messageBodyText = (EditText) findViewById(R.id.messageBody);
		CheckBox authCheck = (CheckBox) findViewById(R.id.checkbox_auth_send);
		CheckBox confCheck = (CheckBox) findViewById(R.id.checkbox_conf_send);
		final String messageBody = messageBodyText.getText().toString();

		Spinner spinner = (Spinner) findViewById(R.id.phoneList);
		String partnerPhone = (String) spinner.getSelectedItem();

		try {
			ClientCommunication communication = communications.getOrCreate(partnerPhone);
			communication.sendMessage(messageBody, confCheck.isChecked(), authCheck.isChecked());
		} catch (Throwable t) {
			clientApplication.displayError(t.getClass().getSimpleName() + " sending message: " + t.getMessage(), t);
		}
	}


}
