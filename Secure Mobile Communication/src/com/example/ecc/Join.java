package com.example.ecc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class Join extends Activity{
	
	//SMSReciver smsrec = new SMSReciver();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join);
		Intent intent = new Intent();
		intent.setAction("android.provider.Telephony.SMS_RECEIVED");
		sendBroadcast(intent);
		
		TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
		String number = tm.getLine1Number();
	    
	    Toast.makeText(this, number, Toast.LENGTH_LONG).show();
	}
	
	public void onJoin(View view) {
		Intent intent = new Intent(this, OnetimePasswordInput.class);
		startActivity(intent);
	}


}
