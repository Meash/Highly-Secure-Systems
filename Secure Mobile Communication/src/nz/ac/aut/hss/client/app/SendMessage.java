package nz.ac.aut.hss.client.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.example.ecc.R;
import nz.ac.aut.hss.client.communication.ClientCommunication;
import nz.ac.aut.hss.client.communication.ClientCommunications;
import nz.ac.aut.hss.client.communication.SMSSender;
import nz.ac.aut.hss.client.communication.ServerCommunication;

import java.security.PrivateKey;

public class SendMessage extends Activity {
	private final ClientCommunications communications;
	private final String phone;
	private ClientApplication clientApplication;

	public SendMessage(final String phoneNumber, ServerCommunication serverCommunication, SMSSender smsSender,
					   PrivateKey privateKey) {
		this.phone = phoneNumber;
		communications = new ClientCommunications(serverCommunication, smsSender, privateKey);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_message);
		clientApplication.getInstance();
//		clientApplication = ClientApplication.getInstance();
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
