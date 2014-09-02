package com.example.ecc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import android.content.Context;
import android.widget.Toast;

public class SaveLoadKeys {
	
	public static boolean savePrivateKey(ECPrivateKey privateKey ,Context context){
    	boolean toReturn = false;
    	try{
    		String data = ObjectSerializer.serialize(privateKey);
    		FileOutputStream fos;
    		toReturn = true;
    		fos = context.openFileOutput("private_key.txt", 0);
    		OutputStreamWriter osw = new OutputStreamWriter(fos);
    		osw.write(data);
    		Toast.makeText(context, "Key saved", Toast.LENGTH_LONG).show();
    		osw.close();
    	} catch (Exception e){
    		Toast.makeText(context, "Cannot save private key", Toast.LENGTH_LONG).show();
    	}
    	return toReturn;
    }
	
	public static ECPrivateKey readPrivateKey(Context context) throws ClassNotFoundException{
		ECPrivateKey key = null;
		try {
			InputStream inputStream = context.openFileInput("private_key.txt");
			
			if(inputStream != null){
				InputStreamReader isr = new InputStreamReader(inputStream);
				BufferedReader br = new BufferedReader(isr);
				String fullText = ""; 
				String linetext = br.readLine();
				while(linetext != null){
					fullText += linetext;
					linetext = br.readLine();
				}
				key = (ECPrivateKey) ObjectSerializer.deserialize(fullText);
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
	
	public static boolean savePublicKey(ECPublicKey publicKey ,Context context){
    	boolean toReturn = false;
    	try{
    		String data = ObjectSerializer.serialize(publicKey);
    		FileOutputStream fos;
    		toReturn = true;
    		fos = context.openFileOutput("public_key.txt", 0);
    		OutputStreamWriter osw = new OutputStreamWriter(fos);
    		osw.write(data);
    		Toast.makeText(context, "Key saved", Toast.LENGTH_LONG).show();
    		osw.close();
    	} catch (Exception e){
    		Toast.makeText(context, "Cannot save private key", Toast.LENGTH_LONG).show();
    	}
    	return toReturn;
    }
	
	public static ECPublicKey readPublicKey(Context context){
		ECPublicKey key = null;
		try {
			InputStream inputStream = context.openFileInput("public_key.txt");
			if(inputStream != null){
				InputStreamReader isr = new InputStreamReader(inputStream);
				BufferedReader br = new BufferedReader(isr);
				String fullText = ""; 
				String linetext = br.readLine();
				while(linetext != null){
					fullText += linetext;
					linetext = br.readLine();
				}
				key = (ECPublicKey) ObjectSerializer.deserialize(fullText);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;	 	 
	}
	
	public static boolean doesFileExist(String fileName){
		File f = new File("data/data/com.example.ecc/files/"+fileName);
		if(f.exists()){
			return true;
		}
		
		return false;
	}

}
