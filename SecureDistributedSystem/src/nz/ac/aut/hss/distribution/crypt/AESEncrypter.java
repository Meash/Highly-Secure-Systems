package nz.ac.aut.hss.distribution.crypt;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Martin Schrimpf
 * @created 27.08.2014
 */
public class AESEncrypter {
	private final Cipher cipher;

	public AESEncrypter(final SecretKey secret) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secret);
		AlgorithmParameters params = cipher.getParameters();
	}

	public String encrypt(final String plaintext)
			throws UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
		byte[] ciphertext = cipher.doFinal(plaintext.getBytes("UTF-8"));
		return new String(ciphertext);
	}
}
