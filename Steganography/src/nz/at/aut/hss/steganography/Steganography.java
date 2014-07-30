package nz.at.aut.hss.steganography;

import java.io.File;

public class Steganography {
	public static void main(String[] args) throws Exception {
		String message = "Hello World";
		File source = new File("resources/rainbow.png");
		File dest = new File("resources/rainbow.encrypted.png");
		System.out.println("Encrypting...");
		new SteganographyEncrypter().encrypt(message, source, dest);
		System.out.println("Decrypting...");
		String decryptedMessage = new SteganographyDecrypter().decrypt(dest);
		System.out.println(decryptedMessage.substring(0,
				message.length())); // this is kind of cheating but we would print a lot of byte-trash if we would not know the message length
	}
}
