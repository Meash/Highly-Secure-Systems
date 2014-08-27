package nz.ac.aut.hss.distribution.crypt;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Martin Schrimpf
 * @created 27.08.2014
 */
public class AESDecrypter {
	private final Cipher cipher;

	public AESDecrypter(final SecretKey secret)
			throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secret);
	}

	public String decrypt(final String ciphertext)
			throws UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
		return new String(cipher.doFinal(ciphertext.getBytes("UTF-8")), "UTF-8");
	}
}
