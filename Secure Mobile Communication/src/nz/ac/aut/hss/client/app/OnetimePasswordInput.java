package nz.ac.aut.hss.client.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class OnetimePasswordInput extends Activity {

	private ClientApplication instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onetime_password_input);

		instance = ClientApplication.getInstance();
	}

	public void loginAction(View view) {
		Log.i("ACTION", "Login action");
		EditText passInput = (EditText) findViewById(R.id.passwordText);
		instance.setOneTimePasseword(passInput.getText().toString());

		try {
			Log.i("SERVER", "Requesting join (2/2)");
			instance.serverComm.requestJoin2();
		} catch (Throwable e) {
			instance.displayError("Could not join server", e);
			return;
		}
		Log.i("SERVER", "Join successful");

		Intent intent = new Intent(this, SendMessage.class);
		startActivity(intent);

	}
}
