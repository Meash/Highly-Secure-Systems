package nz.ac.aut.hss.client.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.example.ecc.R;

public class SendMessage extends Activity{
	
	private ClientApplication instance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_message);
		instance.getInstance();
	}
	
	public void onSend(View view){
		
		EditText messageBodyText = (EditText) findViewById(R.id.messageBody);
		CheckBox authCheck = (CheckBox) findViewById(R.id.checkbox_auth_send);
		CheckBox confCheck = (CheckBox) findViewById(R.id.checkbox_conf_send);
		String messageBody = messageBodyText.getText().toString();
		String authConf = "";
		if(authCheck.isChecked()){
			authConf += "auth";
		}
		if(confCheck.isChecked()){
			authConf += "conf";
		}
		
		SmsSender sender = new SmsSender(this);
		sender.send("02040527278", messageBody);
		
		
	}

	
}
