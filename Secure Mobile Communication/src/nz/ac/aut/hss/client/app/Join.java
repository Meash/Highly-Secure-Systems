package nz.ac.aut.hss.client.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.ecc.R;

public class Join extends Activity{
	
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
		EditText phoneNoInput = (EditText) findViewById(R.id.phoneText);
		instance.setPhoneNo(phoneNoInput.getText().toString());
		Intent intent = new Intent(this, OnetimePasswordInput.class);
		startActivity(intent);
	}


}
