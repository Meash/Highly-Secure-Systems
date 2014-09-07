package nz.ac.aut.hss.client.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ReceiveMessage extends Activity {
	public static final String BODY = "MESSAGEBODY";
	public static final String CONFIDENTIAL = "CONFIDENTIAL";
	public static final String AUTHENTICATED = "AUTHENTICATED";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.receive_message);

		Intent intent = getIntent();
		String message = intent.getStringExtra(BODY);
		final boolean confidential = intent.getBooleanExtra(CONFIDENTIAL, false);
		final boolean authenticated = intent.getBooleanExtra(AUTHENTICATED, false);

		TextView messageBody = (EditText) findViewById(R.id.recMessageBody);
		messageBody.setText(message);
		messageBody.setFocusable(false);
		CheckBox confidentialCheckBox = (CheckBox) findViewById(R.id.checkbox_conf_rec);
		CheckBox authenticatedCheckBox = (CheckBox) findViewById(R.id.checkbox_auth_rec);
		confidentialCheckBox.setChecked(confidential);
		authenticatedCheckBox.setChecked(authenticated);
	}
	
	public void onClick(View view){
		Intent intent = new Intent(this, SendMessage.class);
		startActivity(intent);
	}

}
