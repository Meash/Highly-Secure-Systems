package nz.ac.aut.hss.client.app;

import android.app.Activity;
import android.os.Bundle;
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

		final Map<String, PublicKey> userMap;
		try {
			userMap = clientApplication.serverComm.requestList();
		} catch (CommunicationException | InterruptedException e) {
			clientApplication
					.displayError(e.getClass().getSimpleName() + " while requesting client list: " + e.getMessage());
			return;
		}

		Spinner spinner = (Spinner) findViewById(R.id.phoneList);
		ArrayAdapter<String> adapter =
				new ArrayAdapter<>(this, R.layout.send_message, (String[]) userMap.keySet().toArray());
		spinner.setAdapter(adapter);

	}

	public void onSend(View view) {

		EditText messageBodyText = (EditText) findViewById(R.id.messageBody);
		CheckBox authCheck = (CheckBox) findViewById(R.id.checkbox_auth_send);
		CheckBox confCheck = (CheckBox) findViewById(R.id.checkbox_conf_send);
		String messageBody = messageBodyText.getText().toString();

		try {
			ClientCommunication communication = communications.getOrCreate(phone);
			communication.sendMessage(messageBody, confCheck.isChecked(), authCheck.isChecked());
		} catch (Throwable t) {
			clientApplication.displayError(t.getClass().getSimpleName() + " sending message: " + t.getMessage());
		}
	}


}
