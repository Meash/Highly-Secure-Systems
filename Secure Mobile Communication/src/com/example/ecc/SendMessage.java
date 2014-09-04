package com.example.ecc;

import java.util.TooManyListenersException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SendMessage extends Activity{
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_message);
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
		
		SmsSender sender = new SmsSender("02040527278", messageBody, this);
		sender.sendMessage();
		
		
	}

	
}
