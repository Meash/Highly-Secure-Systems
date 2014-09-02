package com.example.ecc;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	EccKeyGen ECC;
	HTTPRequests request;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(SaveLoadKeys.doesFileExist("public_key.txt") && SaveLoadKeys.doesFileExist("private_key.txt")){
			ECC = new EccKeyGen(this);
		} else {
			ECC = new EccKeyGen();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	public void keyGenAction(View view) {
		SaveLoadKeys.savePrivateKey(ECC.getPrivateKey(), this);
		SaveLoadKeys.savePublicKey(ECC.getPublicKey(), this);
	}
	
	public void getPrivateKey(View view){
		
		try {
			ECPublicKey pubk = SaveLoadKeys.readPublicKey(this);
			ECPrivateKey prik = SaveLoadKeys.readPrivateKey(this);
			showToast("Public key x:" + pubk.getW().getAffineX().toString() + "\n\n Public key y:" + pubk.getW().getAffineY().toString());
			showToast("Private Key :" + prik.getS().toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void httpTest(View view) {
		request = new HTTPRequests("http://en.wikipedia.org/wiki/Main_Page", this);
		request.start();
		CheckRequest check = new CheckRequest();
		check.start();
		
	}
	
	public void fileTest(View view) {
		showToast(SaveLoadKeys.doesFileExist("private_key.txt")+"");
		
	}
	
	public void showToast(final String toast)
	{
	    runOnUiThread(new Runnable() {
	        public void run()
	        {
	            Toast.makeText(MainActivity.this, toast, Toast.LENGTH_LONG).show();
	        }
	    });
	}
	
	public class CheckRequest extends Thread{
		
		public void run(){
			while(!request.isFinished()){};
			showToast(request.getRequest());
		}
		
	}
	

}
