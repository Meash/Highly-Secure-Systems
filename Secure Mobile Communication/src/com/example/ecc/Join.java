package com.example.ecc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class Join extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join);
	}
	
	public void onJoin(View view) {
		Intent intent = new Intent(this, OnetimePasswordInput.class);
		startActivity(intent);
	}


}
