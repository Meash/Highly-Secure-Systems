package nz.ac.aut.hss.distribution.client;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
		if(doesFileExist()){
			ECC = new EccKeyGen(readPrivateKey());
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
	
	public boolean doesFileExist(){
		File f = new File("data/data/com.example.ecc/files/private_key.txt");
		if(f.exists()){
			return true;
		}
		
		return false;
	}
	
	public String readPrivateKey(){
		String key = "Not read";
		try {
			InputStream inputStream = openFileInput("private_key.txt");
			if(inputStream != null){
				InputStreamReader isr = new InputStreamReader(inputStream);
				BufferedReader br = new BufferedReader(isr);
				key = br.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;	 	 
	}
	
	public void keyGenAction(View view) {
		ECC.savePrivateKey(this);
	}
	
	public void getPrivateKey(View view){
		showToast(readPrivateKey());
	}
	
	public void httpTest(View view) {
		request = new HTTPRequests("http://en.wikipedia.org/wiki/Main_Page", this);
		request.start();
		CheckRequest check = new CheckRequest();
		check.start();
		
	}
	
	public void fileTest(View view) {
		showToast(this.getFilesDir().getAbsolutePath() + "\n" + doesFileExist());
		
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
