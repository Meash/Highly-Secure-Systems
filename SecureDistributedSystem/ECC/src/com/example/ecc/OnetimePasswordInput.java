package com.example.ecc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class OnetimePasswordInput extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onetime_password_input);
	}
	
	public void loginAction(View view){
		Intent intent = new Intent(this, SendMessage.class);
		startActivity(intent);
		
	}
}
