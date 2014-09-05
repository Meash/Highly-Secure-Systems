package com.example.ecc;

import android.app.Application;
import android.widget.Toast;
import nz.ac.aut.hss.client.communication.MobileApp;

public class ClientApplication extends Application implements MobileApp {

	private String oneTimePasseword;
	private String phoneNo;
	private static ClientApplication instance = null;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	@Override
	public String getOneTimePassword() {
		return oneTimePasseword; // TODO read user input
	}


	@Override
	public String getPhoneNumber() {
		return phoneNo;
	}

	public void displayError(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public static ClientApplication getInstance() {
		return instance;
	}


}
