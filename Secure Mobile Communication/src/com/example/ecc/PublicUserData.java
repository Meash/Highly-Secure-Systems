package com.example.ecc;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import nz.ac.aut.hss.client.communication.MobileApp;

public class PublicUserData implements MobileApp{
	
	private ECPublicKey publicKey;
    private ECPrivateKey privateKey;
    private String nonce;
    private int phoneNo;
    
	@Override
	public String getOneTimePassword() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getPhoneNumber() {
		// TODO Auto-generated method stub
		return null;
	}

}
