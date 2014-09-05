package com.example.ecc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ReceiveMessage extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recive_message);
		
		Intent intent = getIntent();
		String message = intent.getStringExtra("MESSAGEBODY");
		TextView messageBody = (EditText) findViewById(R.id.recMessageBody);
		messageBody.setText(message);
		messageBody.setFocusable(false);
		
		
	}

}
