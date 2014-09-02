package nz.ac.aut.hss.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.widget.Toast;

public class HTTPRequests extends Thread{
	private String URL;
	private String response;
	private MainActivity context;
	private boolean requestFinished = false; 
	
	public HTTPRequests(String URL, MainActivity context) {
		this.URL = URL;
		this.context = context;
	}
	
	public String getRequest(){
		String responseString = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(URL));
			StatusLine status = response.getStatusLine();
			if(status.getStatusCode() == HttpStatus.SC_OK){
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
		        out.close();
		        responseString = out.toString();
			} else {
				response.getEntity().getContent().close();
				return "Status Error";
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseString;
	}
	
	public String getRequest(List<NameValuePair> values){
		String responseString = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(URL);
			httppost.setEntity(new UrlEncodedFormEntity(values));
			HttpResponse response = httpclient.execute(httppost);
			StatusLine status = response.getStatusLine();
			if(status.getStatusCode() == HttpStatus.SC_OK){
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
		        out.close();
		        responseString = out.toString();
			} else {
				response.getEntity().getContent().close();
				return "Status Error";
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseString;
	}

	public void run() {
		requestFinished = false;
    	response = getRequest();
    	requestFinished = true;
    }
	
	public String getResponse(){
		return response;
	}
	
	public boolean isFinished(){
		return requestFinished;
	}

}
