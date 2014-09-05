package com.example.ecc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class OnetimePasswordInput extends Activity{
	
	private ClientApplication instance;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onetime_password_input);
		
		instance = ClientApplication.getInstance();
	}
	
	public void loginAction(View view){
		EditText passInput = (EditText) findViewById(R.id.passwordText);
		
		Intent intent = new Intent(this, SendMessage.class);
		startActivity(intent);
		
	}
}
