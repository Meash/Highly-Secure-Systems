package com.example.ecc;

import nz.ac.aut.hss.client.communication.CommunicationDisplay;
import nz.ac.aut.hss.client.communication.MobileApp;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class ClientApplication extends Application implements MobileApp{
	
	private String oneTimePasseword;
	private String phoneNo;
	private static ClientApplication instance = null;
	
	@Override
	public void onCreate(){
		super.onCreate();
		instance = this;
	}

	@Override
	public String getOneTimePassword() {
		return oneTimePasseword;
	}
	

	@Override
	public String getPhoneNumber() {
		return phoneNo;
	}

	@Override
	public void displayError(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}

	@Override
	public CommunicationDisplay accept(String phoneNumber) {
		return null;
	}
	
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	public static ClientApplication getInstance(){
		return instance;
	}
	


}
