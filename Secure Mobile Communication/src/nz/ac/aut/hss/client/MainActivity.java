package nz.ac.aut.hss.client;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import com.example.ecc.R;

public class MainActivity extends Activity {

	HTTPRequests request;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public void httpTest(View view) {
		request = new HTTPRequests("http://en.wikipedia.org/wiki/Main_Page", this);
		request.start();
		CheckRequest check = new CheckRequest();
		check.start();

	}

	public void showToast(final String toast) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(MainActivity.this, toast, Toast.LENGTH_LONG).show();
			}
		});
	}

	public class CheckRequest extends Thread {

		public void run() {
			while (!request.isFinished()) {
			}
			;
			showToast(request.getRequest());
		}

	}


}
